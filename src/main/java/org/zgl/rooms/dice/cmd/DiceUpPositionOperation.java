package org.zgl.rooms.dice.cmd;

import org.zgl.error.AppErrorCode;
import org.zgl.error.GenaryAppError;
import org.zgl.net.server.manage.OperateCommandAbstract;
import org.zgl.rooms.dice.model.DicePlayer;
import org.zgl.rooms.dice.model.DiceRoom;
import org.zgl.utils.builder_clazz.ann.Protocol;

/**
 * @作者： big
 * @创建时间： 2018/6/5
 * @文件描述：上位置
 */
@Protocol("1028")
public class DiceUpPositionOperation extends OperateCommandAbstract {
    private final int position;

    public DiceUpPositionOperation(int position) {
        this.position = position;
    }

    @Override
    public Object execute() {
        DicePlayer player = (DicePlayer) getSession().getAttachment();
        DiceRoom room = (DiceRoom) player.getRoom();
        if(!room.getPlayerSet().upPosition(player,position)){
            new GenaryAppError(AppErrorCode.TO_ROOM_HASH_POSITION);
        }
        return null;
    }
}
