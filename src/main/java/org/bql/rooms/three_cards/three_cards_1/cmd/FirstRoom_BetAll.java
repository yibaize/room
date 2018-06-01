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
 * @文件描述：
 */
@Protocol("")
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
        if(playerSet.isPlayForAccount(account)){
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

        for (int i = 0,j = num;i < j;i++){
            long nowMoney = nowPLay.get(i).getPlayer().getGold();
            if(gold != -1){
                gold = nowMoney < gold ? nowMoney : gold;
            }else {
                gold = nowMoney;
            }
        }
        p.reduceGold(gold);
        gamblingParty.setStartTime(System.currentTimeMillis());
        room.bottom(account,gold);//更新房间金币
        //下一玩家下注位置
        nextAccount = playerSet.getNextPositionAccount(p.getRoomPosition());
        gamblingParty.addOparetionCount();
        return null;
    }

    /**
     * 通知所有人全压
     */
    @Override
    public void broadcast() {
        //是否是最后一个
        if(!gamblingParty.getBetState()){
            gamblingParty.setBetState();
            //通知所有玩家改用户财富变更
            FirstPlayerRoom nextPlayer = room.getPlayerSet().getPlayerForPosition(nextAccount);
            gamblingParty.setBetAllPlayer(player);
            room.getGamblingParty().setNowBottomPos(new AtomicInteger(nextPlayer.getPlayer().getRoomPosition()));
            //通知下一个下注玩家
            nextPlayer.getSession().write(new ServerResponse(NotifyCode.NEXT_BET_ALL,null));
            //通知所有人这个玩家全压了
            RoomBetDto dto = new RoomBetDto(account,nextAccount,gold);
            room.broadcast(room.getPlayerSet().getNotAccountPlayer(account),NotifyCode.BET_ALL,dto);
        }else {
            //已经是最后一个，那么牌局结束
            //比牌 结束
            HandCard selfCardIds = player.getHandCard();
            HandCard otherCardIds = gamblingParty.getBetAllPlayer().getHandCard();
            CardManager.getInstance().compareCard(selfCardIds,otherCardIds);
            //通知谁输谁赢
            BetAllDto dto = null;
            if(selfCardIds.isCompareResult()){
                dto = winOrLose(player,gamblingParty.getBetAllPlayer());
            }else {
                dto = winOrLose(gamblingParty.getBetAllPlayer(),player);
            }
            //通知玩家结束
            room.broadcast(room.getPlayerSet().getAllPlayer(),NotifyCode.FIRST_ROOM_GAME_OVER,dto);
            //TODO 牌局结束
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                LoggerUtils.getPlatformLog().info("等待比牌结束异常",e);
            }
            room.end();
        }
    }
    private BetAllDto winOrLose(FirstPlayerRoom win,FirstPlayerRoom lose){
        win.getPlayer().insertGold(room.getAllMoneyNum());
        BetAllDto dto = new BetAllDto();
        dto.setLoseAccount(lose.getPlayer().getAccount());
        dto.setLoseCardIds(ArrayUtils.arrayToList(lose.getHandCard().getCardIds()));
        dto.setLoseCardType(lose.getHandCard().getCardType());
        dto.setWinAccount(win.getPlayer().getAccount());
        dto.setWinCardType(win.getHandCard().getCardType());
        dto.setWinPlayerGetNum(room.getAllMoneyNum());
        dto.setWinCardIds(ArrayUtils.arrayToList(win.getHandCard().getCardIds()));
        Map<String,Long> moneys = room.getBottomMoney();
        List<SettleModelDto> modelDtos = new ArrayList<>(moneys.size());
        room.getGamblingParty().setWinPosition(win.getPlayer().getRoomPosition());//设置这把赢家的位置
        //输的玩家
        for(Map.Entry<String,Long> e:moneys.entrySet()){
            if(!e.getKey().equals(win.getPlayer().getAccount()))
                modelDtos.add(new SettleModelDto(e.getKey(),e.getValue()));
        }
        dto.setSettleModelDtos(modelDtos);
        return dto;
    }
}
