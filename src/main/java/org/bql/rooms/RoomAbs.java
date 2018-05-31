package org.bql.rooms;


import org.bql.player.IPlayer;
import org.bql.player.PlayerRoom;
import org.bql.rooms.dice.model.DicePlayer;
import org.bql.rooms.three_cards.three_cards_1.manage.FirstPlayerRoom;

import java.util.List;

public abstract class RoomAbs {
    private int roomId;
    private String createAccount;
    private int scenecId;
    /**房间状态*/
    /**
     * 创建房间
     * @param roomId
     * @param account 创建人
     */
    public RoomAbs(int scenesId,int roomId, String account) {
        this.roomId = roomId;
        this.createAccount = account;
        this.scenecId = scenesId;
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

    public String getCreateAccount() {
        return createAccount;
    }

    public void setCreateAccount(String createAccount) {
        this.createAccount = createAccount;
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
}
