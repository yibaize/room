package org.zgl.rooms;


import org.zgl.player.IPlayer;
import org.zgl.player.PlayerRoom;
import org.zgl.rooms.type.RoomStateType;
import org.zgl.utils.executer.pool.Worker;

import java.util.List;

public abstract class RoomAbs {
    public static int betLimit = 50000000;
    private int roomId;
    private long createAccount;
    private int scenecId;
    private Worker worker;
    /**房间状态*/
    /**
     * 创建房间
     * @param roomId
     * @param account 创建人
     */
    public RoomAbs(int scenesId,int roomId, long account,Worker worker) {
        this.roomId = roomId;
        this.createAccount = account;
        this.scenecId = scenesId;
        this.worker = worker;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getScenecId() {
        return scenecId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public abstract Object roomInfo(String account);
    public abstract void enterRoom(IPlayer player);
    public abstract void exitRoom(IPlayer player);
    public abstract IPlayer getPlayerForAccount(String account);
    public abstract int getPlayerNum();
    /**获取除了某个玩家以外的*/
    public abstract List<PlayerRoom> getNotAccountAllPlayer(String account);
    /**获取房间所有人*/
    public abstract List<PlayerRoom> getAllPlayer();
    public abstract void timer();
    /**踢人下线*/
    public abstract void kicking(PlayerRoom player,int position);
    public void submin(Runnable runnable){
        worker.registerNewChannelTask(runnable);
    }
    public abstract RoomStateType roomState();
}
