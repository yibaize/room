package org.bql.rooms.dice.cmd;

import org.bql.error.AppErrorCode;
import org.bql.error.GenaryAppError;
import org.bql.net.server.manage.OperateCommandAbstract;
import org.bql.rooms.RoomPlayerBaseDto;
import org.bql.rooms.dice.model.DicePlayer;
import org.bql.rooms.dice.model.DiceRoom;
import org.bql.rooms.type.RoomStateType;
import org.bql.utils.builder_clazz.ann.Protocol;

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
}
