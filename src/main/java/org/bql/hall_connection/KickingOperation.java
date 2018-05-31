package org.bql.hall_connection;

import org.bql.net.server.manage.OperateCommandAbstract;
import org.bql.player.PlayerRoom;
import org.bql.rooms.RoomAbs;
import org.bql.utils.builder_clazz.ann.Protocol;

/**
 * @作者： big
 * @创建时间： 2018/5/19
 * @文件描述：踢人下位置
 */
@Protocol("1011")
public class KickingOperation extends OperateCommandAbstract {
    private final int position;

    public KickingOperation(int position) {
        this.position = position;
    }

    @Override
    public Object execute() {
        PlayerRoom player = (PlayerRoom) getSession().getAttachment();
        RoomAbs room = player.getRoom();
        room.kicking(player,position);
        return null;
    }
}
