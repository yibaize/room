package org.zgl.rooms.thousands_of.cmd;

import org.zgl.error.AppErrorCode;
import org.zgl.error.GenaryAppError;
import org.zgl.net.server.manage.OperateCommandAbstract;
import org.zgl.rooms.thousands_of.model.TOPlayer;
import org.zgl.rooms.thousands_of.model.TORoom;
import org.zgl.utils.builder_clazz.ann.Protocol;

/**
 * @作者： big
 * @创建时间： 2018/5/23
 * @文件描述：申请上庄
 */
@Protocol("1014")
public class ToUpBanker extends OperateCommandAbstract {
    @Override
    public Object execute() {
        TOPlayer player = (TOPlayer) getSession().getAttachment();
        TORoom room = (TORoom) player.getRoom();
        if(!room.getPlayerSet().bankerUp(player))
            new GenaryAppError(AppErrorCode.NOW_IN_BANKER_LIST);
        return null;
    }
}
