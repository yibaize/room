package org.bql.rooms.thousands_of.cmd;

import org.bql.error.AppErrorCode;
import org.bql.error.GenaryAppError;
import org.bql.net.server.manage.OperateCommandAbstract;
import org.bql.rooms.thousands_of.model.TOPlayer;
import org.bql.rooms.thousands_of.model.TOPlayerSet;
import org.bql.rooms.thousands_of.model.TORoom;
import org.bql.utils.builder_clazz.ann.Protocol;

/**
 * 自己上位置
 */
@Protocol("1007")
public class TOUpPositionOperation extends OperateCommandAbstract {
    private final int position;

    public TOUpPositionOperation(int position) {
        this.position = position;
    }

    @Override
    public Object execute() {
        TOPlayer player = (TOPlayer) getSession().getAttachment();
        if(player.getPosition() != TOPlayer.DEFAULT_POS)
            new GenaryAppError(AppErrorCode.TO_ROOM_HASH_POSITION);//当前已经有位置了
        TORoom room = (TORoom) player.getRoom();
        if(room.getPlayerSet().getNowPositionNum() > TOPlayerSet.MAX_POSITION)
            new GenaryAppError(AppErrorCode.POSITION_NOT);
        if(!room.getPlayerSet().upPosition(position,player))
            new GenaryAppError(AppErrorCode.POSITION_NOT);
        return null;
    }
}
