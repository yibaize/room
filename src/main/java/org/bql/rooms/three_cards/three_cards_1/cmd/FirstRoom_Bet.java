package org.bql.rooms.three_cards.three_cards_1.cmd;

import org.bql.error.AppErrorCode;
import org.bql.error.GenaryAppError;
import org.bql.net.builder_clazz.NotifyCode;
import org.bql.net.message.ServerResponse;
import org.bql.net.server.manage.OperateCommandAbstract;
import org.bql.player.PlayerInfoDto;
import org.bql.rooms.three_cards.three_cards_1.dto.RoomBetDto;
import org.bql.rooms.three_cards.three_cards_1.dto.SettleModelDto;
import org.bql.rooms.three_cards.three_cards_1.manage.FirstGamblingParty;
import org.bql.rooms.three_cards.three_cards_1.manage.FirstPlayerRoom;
import org.bql.rooms.three_cards.three_cards_1.manage.FirstRooms;
import org.bql.rooms.three_cards.three_cards_1.manage.MyPlayerSet;
import org.bql.rooms.type.ScenesType;
import org.bql.utils.builder_clazz.ann.Protocol;
import org.bql.utils.logger.LoggerUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 玩家下注
 */
@Protocol("1002")
public class FirstRoom_Bet extends OperateCommandAbstract {
    /**筹码（底筹码1 = 100，2 = 500 ....）*/
    private final int chip;

    public FirstRoom_Bet(int chip) {
        this.chip = chip;
    }

    private long moneyChange;//变更的财富值
    private String nextAccount;//下一玩家的账号
    private String account;//自己的账号（财富发生变更的账号）
    private FirstRooms room;
    private PlayerInfoDto p;
    @Override
    public Object execute() {
        FirstPlayerRoom playerRoom = (FirstPlayerRoom) getSession().getAttachment();
        p = playerRoom.getPlayer();
        room = (FirstRooms) playerRoom.getRoom();
        MyPlayerSet playerSet = room.getPlayerSet();
        boolean isPlay = playerSet.isPlayForAccount(p.getAccount());
        if(!isPlay)
            new GenaryAppError(AppErrorCode.DATA_ERR);//这个玩家没有在玩

        FirstGamblingParty gamblingParty = room.getGamblingParty();
        int nowBottomChip = gamblingParty.getNowBottomChip().get();
        if(gamblingParty.isBottomTimeOut())
            new GenaryAppError(AppErrorCode.BET_TIME_OUT);//下注超时

        if(gamblingParty.getNowBottomPos().get() != p.getRoomPosition())
            new GenaryAppError(AppErrorCode.NOT_IS_YOU_BET);//不是该玩家下注
        if(gamblingParty.getBetState()){
            new GenaryAppError(AppErrorCode.HASH_PLAYER_BET_ALL);
        }
        int scenesId = playerRoom.getPlayer().getScenesId();
        int[] bottom = ScenesType.get(scenesId).chip();
        if(bottom == null)
            new GenaryAppError(AppErrorCode.DATA_ERR);
        if(chip >= bottom.length)
            new GenaryAppError(AppErrorCode.DATA_ERR);
        if(nowBottomChip > chip)//已经加注
            new GenaryAppError(AppErrorCode.BET_MIN_ERR);
        moneyChange = bottom[chip];
        //看牌后倍数*2

        if(playerRoom.isHasLookCard())
            moneyChange *= gamblingParty.MULTIPLE;
        if(!p.reduceGold(moneyChange))
            new GenaryAppError(AppErrorCode.GOLD_NOT);//金币不足
        gamblingParty.setStartTime(System.currentTimeMillis());
        account = p.getAccount();
        room.bottom(account,moneyChange);//更新房间金币
        //下一玩家下注位置
        nextAccount = playerSet.getNextPositionAccount(p.getRoomPosition());
        gamblingParty.addOparetionCount();
        return new SettleModelDto(account,p.getGold(),room.getAllMoneyNum());
    }

    @Override
    public void broadcast() {
        //通知所有玩家改用户财富变更
        FirstPlayerRoom nextPlayer = room.getPlayerSet().getPlayerForPosition(nextAccount);
        room.getGamblingParty().setNowBottomPos(new AtomicInteger(nextPlayer.getPlayer().getRoomPosition()));
        //通知下一个下注玩家
        nextPlayer.getSession().write(new ServerResponse(NotifyCode.ROOM_PLAYER_BOTTOM,null));
        RoomBetDto dto = new RoomBetDto(account,nextAccount,moneyChange,p.getGold(),room.getAllMoneyNum());
        //通知所有玩家财富变更，下一玩家下注
        room.broadcast(room.getPlayerSet().getNotAccountPlayer(account),NotifyCode.ROOM_PLAYER_BOTTOM_NEXT,dto);
    }
}
