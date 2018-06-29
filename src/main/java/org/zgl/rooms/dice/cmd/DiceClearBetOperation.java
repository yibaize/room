package org.zgl.rooms.dice.cmd;

import org.zgl.error.AppErrorCode;
import org.zgl.error.GenaryAppError;
import org.zgl.net.builder_clazz.NotifyCode;
import org.zgl.net.server.manage.OperateCommandAbstract;
import org.zgl.rooms.RoomPlayerBaseDto;
import org.zgl.rooms.dice.model.DicePlayer;
import org.zgl.rooms.dice.model.DiceRoom;
import org.zgl.rooms.thousands_of.dto.BetUpdateDto;
import org.zgl.rooms.type.RoomStateType;
import org.zgl.utils.builder_clazz.ann.Protocol;

/**
 * @作者： big
 * @创建时间： 2018/6/5
 * @文件描述：清楚下注 把钱退回来
 */
@Protocol("1025")
public class DiceClearBetOperation extends OperateCommandAbstract {
    @Override
    public Object execute() {
        DicePlayer player = (DicePlayer) getSession().getAttachment();
        DiceRoom room = (DiceRoom) player.getRoom();
        if(room.getRoomState() != RoomStateType.READY)
            new GenaryAppError(AppErrorCode.NOW_IS_START_BETTLE);
        room.getGamblingParty().clearPlayerBet(player);
        return new RoomPlayerBaseDto().baseDto(player.getPlayer());
    }

    @Override
    public void broadcast() {

    }
}
