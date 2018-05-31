package org.bql.rooms.thousands_of.cmd;

import org.bql.error.AppErrorCode;
import org.bql.error.GenaryAppError;
import org.bql.net.server.manage.OperateCommandAbstract;
import org.bql.rooms.thousands_of.model.TOPlayer;
import org.bql.rooms.thousands_of.model.TORoom;
import org.bql.utils.builder_clazz.ann.Protocol;

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
