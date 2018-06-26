package org.zgl.rooms.always_happy.cmd;

import org.zgl.net.server.manage.OperateCommandAbstract;
import org.zgl.player.PlayerRoom;
import org.zgl.rooms.RoomFactory;
import org.zgl.rooms.always_happy.model.AHRoom;
import org.zgl.rooms.dice.model.DicePlayer;
import org.zgl.rooms.dice.model.DiceRoom;
import org.zgl.rooms.type.RoomStateType;
import org.zgl.utils.builder_clazz.ann.Protocol;

/**
 * @作者： big
 * @创建时间： 2018/6/14
 * @文件描述：
 */
@Protocol("8880")
public class AHStartTest extends OperateCommandAbstract {
    @Override
    public Object execute() {
        AHRoom room = RoomFactory.getInstance().getAhRoom();
        room.setRoomState(RoomStateType.START);
        room.getGamblingParty().shuffle();
        return null;
    }
}
