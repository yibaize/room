package org.zgl.rooms.dice.cmd;

import org.zgl.net.server.manage.OperateCommandAbstract;
import org.zgl.rooms.dice.model.DicePlayer;
import org.zgl.rooms.dice.model.DiceRoom;
import org.zgl.rooms.type.RoomStateType;
import org.zgl.utils.builder_clazz.ann.Protocol;

/**
 * @作者： big
 * @创建时间： 2018/6/14
 * @文件描述：
 */
@Protocol("8888")
public class DiceStartTest extends OperateCommandAbstract {
    @Override
    public Object execute() {
        DicePlayer player = (DicePlayer) getSession().getAttachment();
        DiceRoom room = (DiceRoom) player.getRoom();
        room.setRoomState(RoomStateType.READY);
        room.getGamblingParty().start();
        return null;
    }
}
