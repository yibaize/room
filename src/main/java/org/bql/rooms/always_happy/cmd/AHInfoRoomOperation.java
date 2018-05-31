package org.bql.rooms.always_happy.cmd;

import org.bql.net.server.manage.OperateCommandAbstract;
import org.bql.player.PlayerRoom;
import org.bql.rooms.RoomFactory;
import org.bql.rooms.always_happy.model.AHRoom;
import org.bql.utils.builder_clazz.ann.Protocol;

/**
 * @作者： big
 * @创建时间： 2018/5/24
 * @文件描述：房间信息
 */
@Protocol("1019")
public class AHInfoRoomOperation extends OperateCommandAbstract {
    private final int enterOrExit;

    public AHInfoRoomOperation(int enterOrExit) {
        this.enterOrExit = enterOrExit;
    }

    @Override
    public Object execute() {
        PlayerRoom player = (PlayerRoom) getSession().getAttachment();
        AHRoom room = RoomFactory.getInstance().getAhRoom();
        switch (enterOrExit){
            case 1:
                room.enterRoom(player);
                return room.roomInfo(player.getPlayer().getAccount());
            case 2:
                room.exitRoom(player);
                return null;
        }
        return null;
    }
}
