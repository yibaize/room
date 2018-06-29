package org.zgl.rooms.dice.cmd;

import org.zgl.error.AppErrorCode;
import org.zgl.error.GenaryAppError;
import org.zgl.net.server.manage.OperateCommandAbstract;
import org.zgl.player.PlayerInfoDto;
import org.zgl.rooms.RoomAbs;
import org.zgl.rooms.dice.model.DicePlayer;
import org.zgl.rooms.dice.model.DiceRoom;
import org.zgl.rooms.thousands_of.dto.BetUpdateDto;
import org.zgl.rooms.type.RoomStateType;
import org.zgl.rooms.type.ScenesType;
import org.zgl.utils.builder_clazz.ann.Protocol;

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
        if(player.getBetNum().get() >= RoomAbs.betLimit)
            new GenaryAppError(AppErrorCode.BET_LIMIT);
        PlayerInfoDto infoDto = player.getPlayer();
        int[] CHIP = ScenesType.get(infoDto.getScenesId()).chip();
        if(CHIP == null || betPosition <= 0)
            new GenaryAppError(AppErrorCode.SERVER_ERR);//下注位置异常
        if(chipPosition > CHIP.length || chipPosition < 0)
            new GenaryAppError(AppErrorCode.SERVER_ERR);//筹码位置一场
        DiceRoom room = (DiceRoom) player.getRoom();
        if(room.getRoomState() != RoomStateType.READY)
            new GenaryAppError(AppErrorCode.BET_TIME_OUT);//房间已经停止下注等待下一句开始吧
        int moneyNum = CHIP[chipPosition];
        long m = infoDto.getGold();
        if(!infoDto.reduceGold(moneyNum)) {//减少金币
            new GenaryAppError(AppErrorCode.GOLD_NOT);//金币不足
        }
        room.getGamblingParty().addBeforeBet(infoDto.getUid(),m);
        room.getGamblingParty().bet(player,moneyNum,betPosition);
        infoDto.addDiceRoomBetNum(moneyNum);
//        player.bet(moneyNum);
        return new BetUpdateDto(infoDto.getAccount(),infoDto.getGold(),moneyNum,betPosition);
    }
}
