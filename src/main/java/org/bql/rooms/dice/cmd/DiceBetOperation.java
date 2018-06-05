package org.bql.rooms.dice.cmd;

import org.bql.error.AppErrorCode;
import org.bql.error.GenaryAppError;
import org.bql.net.server.manage.OperateCommandAbstract;
import org.bql.player.PlayerInfoDto;
import org.bql.rooms.dice.model.DicePlayer;
import org.bql.rooms.dice.model.DiceRoom;
import org.bql.rooms.thousands_of.dto.BetUpdateDto;
import org.bql.rooms.type.RoomStateType;
import org.bql.rooms.type.ScenesType;
import org.bql.utils.builder_clazz.ann.Protocol;

/**
 * @作者： big
 * @创建时间： 2018/6/5
 * @文件描述：下注
 */
@Protocol("1024")
public class DiceBetOperation extends OperateCommandAbstract {
    private final int betPosition;
    private final int chipPosition;

    public DiceBetOperation(int betPosition, int chipPosition) {
        this.betPosition = betPosition;
        this.chipPosition = chipPosition;
    }

    @Override
    public Object execute() {
        DicePlayer player = (DicePlayer) getSession().getAttachment();
        PlayerInfoDto infoDto = player.getPlayer();
        int[] CHIP = ScenesType.get(infoDto.getScenesId()).chip();
        if(CHIP == null || betPosition <= 0)
            new GenaryAppError(AppErrorCode.SERVER_ERR);//下注位置异常
        if(chipPosition > CHIP.length || chipPosition < 0)
            new GenaryAppError(AppErrorCode.SERVER_ERR);//筹码位置一场
        DiceRoom room = (DiceRoom) player.getRoom();
        room.getGamblingParty().bet(player,chipPosition,betPosition);
        if(room.getRoomState() != RoomStateType.READY)
            new GenaryAppError(AppErrorCode.BET_TIME_OUT);//房间已经停止下注等待下一句开始吧
        int moneyNum = CHIP[chipPosition];
        if(player.canBet() < moneyNum){
            new GenaryAppError(AppErrorCode.IN_AH_ROOM_BET_NIMIETY);
        }
        if(!infoDto.reduceGold(moneyNum)) {//减少金币
            new GenaryAppError(AppErrorCode.GOLD_NOT);//金币不足
        }
        room.getGamblingParty().bet(player,moneyNum,betPosition);
        player.bet(moneyNum);
        return new BetUpdateDto(infoDto.getAccount(),infoDto.getGold(),moneyNum,betPosition);
    }
}
