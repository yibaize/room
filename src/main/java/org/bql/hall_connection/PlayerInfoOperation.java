package org.bql.hall_connection;
import org.bql.error.AppErrorCode;
import org.bql.error.GenaryAppError;
import org.bql.net.builder_clazz.NotifyCode;
import org.bql.net.http.HttpClient;
import org.bql.net.server.manage.OperateCommandAbstract;
import org.bql.net.server.session.SessionManager;
import org.bql.player.PlayerFactory;
import org.bql.player.PlayerInfoDto;
import org.bql.player.PlayerRoom;
import org.bql.rooms.RoomAbs;
import org.bql.rooms.type.ScenesType;
import org.bql.utils.builder_clazz.ann.Protocol;
/**
 * 从大厅获取玩家信息
 * 场景id 1：初级场 2：中级场 3：高级场 4：骰子场 5：万人场
 *      * 1，万人场类型 2，开房间类型
 */
@Protocol("10000")
public class PlayerInfoOperation extends OperateCommandAbstract {
    private final int scenesId;
    private final String account;

    public PlayerInfoOperation(int scenesId, String account) {
        this.scenesId = scenesId;
        this.account = account;
    }

    @Override
    public Object execute() {
        if(account == null)
            new GenaryAppError(AppErrorCode.ACCOUNT_IS_NULL);
        PlayerInfoDto player = HttpClient.getInstance().syncPost(NotifyCode.REQUEST_HALL_PLAYER_INFO,account+","+PlayerFactory.SYSTEM_PLAYER_ID,PlayerInfoDto.class);
        if(player.getAccount() == null)
            new GenaryAppError(AppErrorCode.ACCOUNT_IS_NULL);
        player.setScenesId(scenesId);
        ScenesType scenesType = ScenesType.get(scenesId);
        if(scenesType == null)
            new GenaryAppError(AppErrorCode.DATA_ERR);
        PlayerRoom roomPlayer = scenesType.newPlayerForInit();
        roomPlayer.setPlayer(player);
        roomPlayer.setSession(getSession());
        RoomAbs r = scenesType.enterRoom(roomPlayer);
        roomPlayer.setRoom(r);
        getSession().setAttachment(roomPlayer);//绑定会话
        SessionManager.putSession(account,getSession());
        if(r == null)
            return null;
        return r.roomInfo(account);
    }
}
