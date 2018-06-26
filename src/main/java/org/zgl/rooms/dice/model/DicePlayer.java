package org.zgl.rooms.dice.model;

import org.zgl.player.PlayerRoom;

public class DicePlayer extends PlayerRoom {
    public static final int DEFAULT_POSITION = -1179841351;
    private long diceBet;//下注数量
    private int roomPosition = DEFAULT_POSITION;
    public boolean bet(long num){
        if(getPlayer().reduceGold(num))
            diceBet += num;
        return false;
    }

    public int getRoomPosition() {
        return roomPosition;
    }

    public void setRoomPosition(int roomPosition) {
        this.roomPosition = roomPosition;
    }

    public long getDiceBet() {
        return diceBet;
    }

    public void setDiceBet(long diceBet) {
        this.diceBet = diceBet;
    }

}
