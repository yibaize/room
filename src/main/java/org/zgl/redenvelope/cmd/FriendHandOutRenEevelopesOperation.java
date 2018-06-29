package org.zgl.redenvelope.cmd;

import org.zgl.error.AppErrorCode;
import org.zgl.error.GenaryAppError;
import org.zgl.net.server.manage.OperateCommandAbstract;
import org.zgl.net.server.session.SessionManager;
import org.zgl.player.PlayerInfoDto;
import org.zgl.player.PlayerRoom;
import org.zgl.remote.HttpProxyOutboundHandler;
import org.zgl.remote.IBackHall;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者： big
 * @创建时间： 2018/6/28
 * @文件描述：发红包给好友
 */
public class FriendHandOutRenEevelopesOperation extends OperateCommandAbstract {
    private final long targetUid;
    private final int money;

    public FriendHandOutRenEevelopesOperation(long targetUid, int money) {
        this.targetUid = targetUid;
        this.money = money;
    }

    @Override
    public Object execute() {
        PlayerRoom playerRoom = (PlayerRoom) getSession().getAttachment();
        PlayerInfoDto self = playerRoom.getPlayer();
        if(!self.reduceGold(money))
            new GenaryAppError(AppErrorCode.GOLD_NOT);
        PlayerInfoDto target = null;
        if(!SessionManager.isOnlinePlayer(targetUid)){
            //到数据库中去找
        }else {
            target = ((PlayerRoom) SessionManager.getSession(targetUid).getAttachment()).getPlayer();
            target.insertGold(money);
        }
        List<PlayerInfoDto> list = new ArrayList<>(2);
        list.add(self);
        list.add(target);
        IBackHall backHall = HttpProxyOutboundHandler.getRemoteProxyObj(IBackHall.class);
        backHall.backHall(list);
        return null;
    }
}
