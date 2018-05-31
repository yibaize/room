package org.bql.rooms.three_cards.three_cards_1.cmd;

import org.bql.error.AppErrorCode;
import org.bql.error.GenaryAppError;
import org.bql.net.builder_clazz.NotifyCode;
import org.bql.net.message.ServerResponse;
import org.bql.net.server.manage.OperateCommandAbstract;
import org.bql.player.PlayerInfoDto;
import org.bql.rooms.card.CardManager;
import org.bql.rooms.three_cards.three_cards_1.dto.CompareCardResultDto;
import org.bql.rooms.three_cards.three_cards_1.dto.FirstRoomSettleDto;
import org.bql.rooms.three_cards.three_cards_1.dto.SettleModelDto;
import org.bql.rooms.three_cards.three_cards_1.manage.FirstPlayerRoom;
import org.bql.rooms.three_cards.three_cards_1.manage.FirstRooms;
import org.bql.rooms.three_cards.three_cards_1.manage.MyPlayerSet;
import org.bql.rooms.three_cards.three_cards_1.model.HandCard;
import org.bql.utils.ArrayUtils;
import org.bql.utils.DateUtils;
import org.bql.utils.builder_clazz.ann.Protocol;
import org.bql.utils.logger.LoggerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    FirstPlayerRoom player;
    @Override
    public Object execute() {
        player = (FirstPlayerRoom) getSession().getAttachment();
        PlayerInfoDto p = player.getPlayer();
        account = p.getAccount();
        room = (FirstRooms) player.getRoom();
        playerSet = room.getPlayerSet();

        if(!playerSet.isPlayForAccount(account))
            new GenaryAppError(AppErrorCode.SERVER_ERR);//服务器异常，没这玩家
        FirstPlayerRoom target = playerSet.getPlayerForPosition(targetAccount);
        selfCardIds = player.getHandCard();
        otherCardIds = target.getHandCard();
        if(target == null)
            new GenaryAppError(AppErrorCode.SERVER_ERR);//服务器异常，没这玩家
        if(!playerSet.isPlayForAccount(targetAccount))
            new GenaryAppError(AppErrorCode.SERVER_ERR);//服务器异常，没这玩家
        if(playerSet.isLose(target.getPlayer().getRoomPosition()))
            new GenaryAppError(AppErrorCode.PLAYER_IS_COMPARE);
        if(room.getGamblingParty().getNowBottomPos().get() != p.getRoomPosition())
            new GenaryAppError(AppErrorCode.NOT_IS_YOU_BET);//不是该玩家操作
        if(room.getGamblingParty().isForbidCompare(account,targetAccount)){
            new GenaryAppError(AppErrorCode.TARGET_IS_FORBID);//禁比当中
        }
        CardManager.getInstance().compareCard(selfCardIds,otherCardIds);
        if(selfCardIds.isCompareResult()){
            playerSet.losePlayer(targetAccount);
        }else {
            playerSet.losePlayer(account);
        }
         playerSet.addCompareNum();
        return null;
    }

    @Override
    public void broadcast() {
        HandCard lowCards = selfCardIds;
        String lowAccount = account;
        String winAccount = targetAccount;
        if(selfCardIds.isCompareResult()) {
            lowCards = otherCardIds;
            lowAccount = targetAccount;
            winAccount = account;
        }
        CompareCardResultDto resultDto = new CompareCardResultDto();/**new CompareCardResultDto(lowAccount,lowCards.getCardType(),ArrayUtils.arrayToList(lowCards.getCardIds()),winAccount);*/
        resultDto.setTargetAccount(targetAccount);
        resultDto.setCardType(lowCards.getCardType());
        resultDto.setAccount(lowAccount);
        List<Integer> cardsIds = ArrayUtils.arrayToList(lowCards.getCardIds());
        resultDto.setCardIds(cardsIds);
        room.broadcast(playerSet.getAllPlayer(),NotifyCode.ROOM_LOWER_PLAYER,resultDto);//通知所有玩家这家伙输了

        playerSet.losePlayer(account);//这个玩家输了
        //在这里通知本场结束
        if(playerSet.getCompareNum() >= playerSet.playNum()-1){
            FirstRoomSettleDto settleDto = new FirstRoomSettleDto();
            int winPosition = -1;
            List<Integer> cardIds;
            int cardType;
            if(!selfCardIds.isCompareResult()){//自己赢了 那就发自己的牌回去 否则发别人的牌
                winPosition = playerSet.getPlayerPos(targetAccount);
                cardIds = ArrayUtils.arrayToList(otherCardIds.getCardIds());
                cardType = otherCardIds.getCardType();
            }else {
                winPosition = playerSet.getPlayerPos(account);
                cardIds = ArrayUtils.arrayToList(selfCardIds.getCardIds());
                cardType = selfCardIds.getCardType();
            }
            settleDto.setAccount(winAccount);
            settleDto.setCardType(cardType);
            settleDto.setCardIds(cardIds);
            settleDto.setWinPlayerGetNum(room.getAllMoneyNum());
            Map<String,Long> moneys = room.getBottomMoney();
            List<SettleModelDto> modelDtos = new ArrayList<>(moneys.size());

            //输的玩家
            for(Map.Entry<String,Long> e:moneys.entrySet()){
                if(!e.getKey().equals(winAccount))
                    modelDtos.add(new SettleModelDto(e.getKey(),e.getValue()));
            }
            settleDto.setSettleModelDtos(modelDtos);

            FirstPlayerRoom winPlayer = playerSet.getPlayerForPosition(winAccount);
            winPlayer.getPlayer().insertGold(room.getAllMoneyNum());//赢家财富

            room.broadcast(playerSet.getAllPlayer(),NotifyCode.ROOM_SETTLE_ACCOUNT,settleDto);//通知所有玩家这家伙赢了

            room.getGamblingParty().setWinPosition(winPosition);//设置这把赢家的位置
            //TODO 牌局结束
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                LoggerUtils.getPlatformLog().info("等待比牌结束异常",e);
            }
            room.end();
        }else {
            //下一个位置的玩家
            String nextAccount = playerSet.getNextPositionAccount(player.getPlayer().getRoomPosition());
            FirstPlayerRoom nextPlayer = room.getPlayerSet().getPlayerForPosition(nextAccount);
            nextPlayer.getSession().write(new ServerResponse(NotifyCode.ROOM_PLAYER_BOTTOM,null));
            //通知下一个位置玩家做动作
        }
        room.getGamblingParty().setStartTime(DateUtils.currentTime());
    }
}
