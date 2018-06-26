package org.zgl.rooms.three_cards.three_cards_1.cmd;

import org.zgl.error.AppErrorCode;
import org.zgl.error.GenaryAppError;
import org.zgl.net.builder_clazz.NotifyCode;
import org.zgl.net.message.ServerResponse;
import org.zgl.net.server.manage.OperateCommandAbstract;
import org.zgl.player.PlayerInfoDto;
import org.zgl.rooms.card.CardManager;
import org.zgl.rooms.three_cards.three_cards_1.dto.CompareCardResultDto;
import org.zgl.rooms.three_cards.three_cards_1.manage.FirstGamblingParty;
import org.zgl.rooms.three_cards.three_cards_1.manage.FirstPlayerRoom;
import org.zgl.rooms.three_cards.three_cards_1.manage.FirstRooms;
import org.zgl.rooms.three_cards.three_cards_1.manage.MyPlayerSet;
import org.zgl.rooms.three_cards.three_cards_1.model.HandCard;
import org.zgl.utils.ArrayUtils;
import org.zgl.utils.builder_clazz.ann.Protocol;
import org.zgl.utils.logger.LoggerUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 比牌
 */
@Protocol("1003")
public class FirstRoom_Compare extends OperateCommandAbstract {
    /**要比牌的目标对象*/
    private final String targetAccount;
    public FirstRoom_Compare(String targetAccount) {
        this.targetAccount = targetAccount;
    }

    private HandCard selfCardIds;
    private HandCard otherCardIds;
    private FirstRooms room;
    private String account;
    private MyPlayerSet playerSet;
    private FirstPlayerRoom player;
    private FirstPlayerRoom winPlayer;
    private FirstPlayerRoom losePlayer;

    @Override
    public Object execute() {
        player = (FirstPlayerRoom) getSession().getAttachment();
        PlayerInfoDto p = player.getPlayer();
        account = p.getAccount();
        room = (FirstRooms) player.getRoom();
        playerSet = room.getPlayerSet();
        if(!playerSet.isPlayForAccount(account))
            new GenaryAppError(AppErrorCode.SERVER_ERR);//服务器异常，没这玩家
        FirstPlayerRoom other = playerSet.getPlayerForPosition(targetAccount);
        selfCardIds = player.getHandCard();
        otherCardIds = other.getHandCard();
        if(other == null)
            new GenaryAppError(AppErrorCode.SERVER_ERR);//服务器异常，没这玩家
        if(!playerSet.isPlayForAccount(targetAccount))
            new GenaryAppError(AppErrorCode.SERVER_ERR);//服务器异常，没这玩家
        if(playerSet.isLose(other.getPlayer().getRoomPosition()))
            new GenaryAppError(AppErrorCode.PLAYER_IS_COMPARE);
        if(room.getGamblingParty().getNowBottomPos().get() != p.getRoomPosition())
            new GenaryAppError(AppErrorCode.NOT_IS_YOU_BET);//不是该玩家操作
        //整理牌型
        CardManager.getInstance().getCardType(selfCardIds);
        CardManager.getInstance().getCardType(otherCardIds);
        CardManager.getInstance().compareCard(selfCardIds,otherCardIds);
        LoggerUtils.getLogicLog().info(selfCardIds+":"+otherCardIds);
        if(selfCardIds.isCompareResult()){
            playerSet.losePlayer(targetAccount);
            winPlayer = player;
            losePlayer = other;
        }else {
            playerSet.losePlayer(account);
            winPlayer = other;
            losePlayer = player;
        }
         playerSet.addCompareNum();
        return null;
    }

    @Override
    public void broadcast() {
        CompareCardResultDto resultDto = new CompareCardResultDto();/**new CompareCardResultDto(lowAccount,lowCards.getCardType(),ArrayUtils.arrayToList(lowCards.getCardIds()),winAccount);*/

        resultDto.setTargetAccount(targetAccount);
        resultDto.setCardType(losePlayer.getHandCard().getCardType());
        resultDto.setAccount(losePlayer.getPlayer().getAccount());
        List<Integer> cardsIds = ArrayUtils.arrayToList(losePlayer.getHandCard().getCardIds());
        resultDto.setCardIds(cardsIds);
        room.broadcast(playerSet.getAllPlayer(),NotifyCode.ROOM_LOWER_PLAYER,resultDto);//通知所有玩家这家伙输了
        FirstGamblingParty gamblingParty = room.getGamblingParty();
        //在这里通知本场结束
        if(playerSet.getCompareNum() >= playerSet.playNum()){
            gamblingParty.setWinPlayer(winPlayer);
            gamblingParty.setWinPosition(winPlayer.getPlayer().getRoomPosition());//设置这把赢家的位置
            gamblingParty.setRoomEnd();
            gamblingParty.setEndTime();
        }else {
            //下一个位置的玩家
            String nextAccount = playerSet.getNextPositionAccount(player.getPlayer().getRoomPosition());
            FirstPlayerRoom nextPlayer = room.getPlayerSet().getPlayerForPosition(nextAccount);
            gamblingParty.setNowBottomPos(new AtomicInteger(nextPlayer.getPlayer().getRoomPosition()));
            nextPlayer.getSession().write(new ServerResponse(NotifyCode.ROOM_PLAYER_BOTTOM,null));
            //通知下一个位置玩家做动作
        }
        gamblingParty.setStartTime(System.currentTimeMillis());
    }

}
