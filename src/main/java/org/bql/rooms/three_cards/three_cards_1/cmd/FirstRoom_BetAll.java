package org.bql.rooms.three_cards.three_cards_1.cmd;

import org.bql.error.AppErrorCode;
import org.bql.error.GenaryAppError;
import org.bql.net.builder_clazz.NotifyCode;
import org.bql.net.message.ServerResponse;
import org.bql.net.server.manage.OperateCommandAbstract;
import org.bql.player.PlayerInfoDto;
import org.bql.player.PlayerRoom;
import org.bql.rooms.card.CardManager;
import org.bql.rooms.three_cards.three_cards_1.dto.BetAllDto;
import org.bql.rooms.three_cards.three_cards_1.dto.BetAllResponseDto;
import org.bql.rooms.three_cards.three_cards_1.dto.RoomBetDto;
import org.bql.rooms.three_cards.three_cards_1.dto.SettleModelDto;
import org.bql.rooms.three_cards.three_cards_1.manage.FirstGamblingParty;
import org.bql.rooms.three_cards.three_cards_1.manage.FirstPlayerRoom;
import org.bql.rooms.three_cards.three_cards_1.manage.FirstRooms;
import org.bql.rooms.three_cards.three_cards_1.manage.MyPlayerSet;
import org.bql.rooms.three_cards.three_cards_1.model.HandCard;
import org.bql.utils.ArrayUtils;
import org.bql.utils.builder_clazz.ann.Protocol;
import org.bql.utils.logger.LoggerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @作者： big
 * @创建时间： 18-5-31
 * @文件描述：全压
 */
@Protocol("1018")
public class FirstRoom_BetAll extends OperateCommandAbstract {
    private FirstRooms room;
    private String account;
    private String nextAccount;
    private FirstGamblingParty gamblingParty;
    private long gold = -1;
    private FirstPlayerRoom player;
    @Override
    public Object execute() {
        player = (FirstPlayerRoom) getSession().getAttachment();
        room = (FirstRooms) player.getRoom();
        PlayerInfoDto p = player.getPlayer();
        account = p.getAccount();
        MyPlayerSet playerSet = room.getPlayerSet();
        if(!playerSet.isPlayForAccount(account)){
            new GenaryAppError(AppErrorCode.SERVER_ERR);
        }
        gamblingParty = room.getGamblingParty();
        if(gamblingParty.isBottomTimeOut())
            new GenaryAppError(AppErrorCode.BET_TIME_OUT);//下注超时
        if(gamblingParty.getNowBottomPos().get() != p.getRoomPosition())
            new GenaryAppError(AppErrorCode.NOT_IS_YOU_BET);//不是该玩家下注
        List<FirstPlayerRoom> nowPLay = playerSet.nowAllPayPlayer();
        int num = nowPLay.size();
        if(num != 2)
            new GenaryAppError(AppErrorCode.NOW_PLAY_PLAYER_ERR);
        if(!gamblingParty.getBetState()) {
            for (int i = 0, j = num; i < j; i++) {
                long nowMoney = nowPLay.get(i).getPlayer().getGold();
                if (gold != -1) {
                    gold = nowMoney < gold ? nowMoney : gold;
                } else {
                    gold = nowMoney;
                }
            }
        }else {
            gold = gamblingParty.getBetAllGold();
        }
        LoggerUtils.getLogicLog().error(p.getAccount()+" 下注前 "+p.getGold() + "gold" + gold);
        p.reduceGold(gold);
        gamblingParty.setStartTime(System.currentTimeMillis());
        room.bottom(account,gold);//更新房间金币
        //下一玩家下注位置
        nextAccount = playerSet.getNextPositionAccount(p.getRoomPosition());
        gamblingParty.addOparetionCount();
        LoggerUtils.getLogicLog().error(p.getAccount()+" 金币是 "+p.getGold());
        return new BetAllResponseDto(p.getGold(),room.getAllMoneyNum(),gold);
    }

    /**
     * 通知所有人全压
     */
    @Override
    public void broadcast() {
        //是否是最后一个
        if(!gamblingParty.getBetState()){
            gamblingParty.setBetState();
            gamblingParty.setBetAllPlayer(player);
            gamblingParty.setBetAllGold(gold);
            //通知所有玩家改用户财富变更
            FirstPlayerRoom nextPlayer = room.getPlayerSet().getPlayerForPosition(nextAccount);
            room.getGamblingParty().setNowBottomPos(new AtomicInteger(nextPlayer.getPlayer().getRoomPosition()));
            //通知下一个下注玩家
            nextPlayer.getSession().write(new ServerResponse(NotifyCode.NEXT_BET_ALL,null));
            //通知所有人这个玩家全压了
            RoomBetDto dto = new RoomBetDto(account,nextAccount,gold,player.getPlayer().getGold(),room.getAllMoneyNum());
            room.broadcast(room.getPlayerSet().getNotAccountPlayer(account),NotifyCode.BET_ALL,dto);
        }else {
            //已经是最后一个，那么牌局结束
            //比牌 结束
            HandCard selfCardIds = player.getHandCard();
            HandCard otherCardIds = gamblingParty.getBetAllPlayer().getHandCard();
            CardManager.getInstance().compareCard(selfCardIds,otherCardIds);
            BetAllDto dto = null;
            //通知谁输谁赢
            if(selfCardIds.isCompareResult()){
                dto = winOrLose(player,gamblingParty.getBetAllPlayer());

                LoggerUtils.getLogicLog().info(player.getPlayer().getAccount()+" (赢家)金币是 "+player.getPlayer().getGold());
                LoggerUtils.getLogicLog().info(gamblingParty.getBetAllPlayer().getPlayer().getAccount()+" (输家家)金币是 "+gamblingParty.getBetAllPlayer().getPlayer().getGold());
            }else {
                dto = winOrLose(gamblingParty.getBetAllPlayer(),player);
                LoggerUtils.getLogicLog().info(player.getPlayer().getAccount()+" (输家)金币是 "+player.getPlayer().getGold());
                LoggerUtils.getLogicLog().info(gamblingParty.getBetAllPlayer().getPlayer().getAccount()+" (赢家家)金币是 "+gamblingParty.getBetAllPlayer().getPlayer().getGold());
            }
            room.broadcast(room.getPlayerSet().getAllPlayer(),NotifyCode.FIRST_ROOM_GAME_OVER,dto);
            //通知玩家结束
            gamblingParty.setEndTime();
            gamblingParty.setRoomEnd();
        }
    }
    private BetAllDto winOrLose(FirstPlayerRoom win,FirstPlayerRoom lose){
        win.getPlayer().insertGold(room.getAllMoneyNum());
        BetAllDto dto = new BetAllDto();

        dto.setLoseAccount(lose.getPlayer().getAccount());
        dto.setLoseCardIds(ArrayUtils.arrayToList(lose.getHandCard().getCardIds()));
        dto.setLoseCardType(lose.getHandCard().getCardType());
        dto.setLosePlayerSurplusGold(lose.getPlayer().getGold());

        dto.setWinAccount(win.getPlayer().getAccount());
        dto.setWinCardType(win.getHandCard().getCardType());
        dto.setWinPlayerSurplusGold(win.getPlayer().getGold());
        dto.setWinCardIds(ArrayUtils.arrayToList(win.getHandCard().getCardIds()));
        room.getGamblingParty().setWinPosition(win.getPlayer().getRoomPosition());//设置这把赢家的位置
        room.getGamblingParty().setWinPlayer(win);
        return dto;
    }
}
