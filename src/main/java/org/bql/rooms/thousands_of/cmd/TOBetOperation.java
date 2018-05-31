package org.bql.rooms.thousands_of.cmd;

import org.bql.error.AppErrorCode;
import org.bql.error.GenaryAppError;
import org.bql.net.server.manage.OperateCommandAbstract;
import org.bql.player.PlayerInfoDto;
import org.bql.rooms.thousands_of.dto.BetUpdateDto;
import org.bql.rooms.thousands_of.model.TOPlayer;
import org.bql.rooms.thousands_of.model.TORoom;
import org.bql.rooms.type.RoomStateType;
import org.bql.rooms.type.ScenesType;
import org.bql.utils.builder_clazz.ann.Protocol;

/**
 * 下注
 */
@Protocol("1005")
public class TOBetOperation extends OperateCommandAbstract {
    private final int betPostion;
    private final int betChip;
    public TOBetOperation(int betPostion, int betChip) {
        this.betPostion = betPostion;
        this.betChip = betChip;
    }
    @Override
    public Object execute() {
        TOPlayer toPlayer = (TOPlayer) getSession().getAttachment();
        PlayerInfoDto infoDto = toPlayer.getPlayer();
        if(infoDto.getGold() < 30000)
            new GenaryAppError(AppErrorCode.GOLD_NEED_30000);//金币大于30000才能下注
        int[] CHIP = ScenesType.get(infoDto.getScenesId()).chip();
        if(CHIP == null || betPostion <= 0)
            new GenaryAppError(AppErrorCode.SERVER_ERR);//下注位置异常
        if(betChip > CHIP.length || betChip < 0)
            new GenaryAppError(AppErrorCode.SERVER_ERR);//筹码位置一场
        TORoom room = (TORoom) toPlayer.getRoom();
        TOPlayer banker = room.getPlayerSet().getBanker();
        if(banker != null && banker.equals(toPlayer))
            new GenaryAppError(AppErrorCode.BANKER_CAN_NOT_BET);//庄家不能下注
        if(room.getRoomState() != RoomStateType.READY)
            new GenaryAppError(AppErrorCode.BET_TIME_OUT);//房间已经停止下注等待下一句开始吧
        int moneyNum = CHIP[betChip];
        if(toPlayer.canBet() < moneyNum){
            new GenaryAppError(AppErrorCode.IN_AH_ROOM_BET_NIMIETY);
        }
        if(!infoDto.reduceGold(moneyNum)) {//减少金币
            new GenaryAppError(AppErrorCode.GOLD_NOT);//金币不足
        }
        room.getGamblingParty().bet(betPostion,moneyNum,toPlayer);
        toPlayer.bet(moneyNum);
        System.out.println("回发数据");

        return new BetUpdateDto(infoDto.getAccount(),infoDto.getGold(),moneyNum,betPostion);
    }
}
