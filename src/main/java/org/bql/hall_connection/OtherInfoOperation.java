package org.bql.hall_connection;

import org.bql.error.AppErrorCode;
import org.bql.error.GenaryAppError;
import org.bql.net.server.manage.OperateCommandAbstract;
import org.bql.net.server.session.ISession;
import org.bql.net.server.session.SessionManager;
import org.bql.player.PlayerRoom;
import org.bql.utils.builder_clazz.ann.Protocol;

/**
 * 获取房间玩家信息
 */
@Protocol("1004")
public class OtherInfoOperation extends OperateCommandAbstract {
    private final String targetAccount;
    public OtherInfoOperation(String targetAccount) {
        this.targetAccount = targetAccount;
    }

    @Override
    public Object execute() {
        ISession session = SessionManager.getSession(targetAccount);
        if(session == null)
            new GenaryAppError(AppErrorCode.DATA_ERR);
        PlayerRoom player = (PlayerRoom) session.getAttachment();
        return player.getPlayer();
    }
}
