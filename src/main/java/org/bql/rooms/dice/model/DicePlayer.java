package org.bql.rooms.dice.model;

import org.bql.player.PlayerRoom;

public class DicePlayer extends PlayerRoom {
    public static final int DEFAULT_POSITION = -1179841351;
    private long betNum;//下注数量
    private int roomPosition = DEFAULT_POSITION;
    public boolean bet(long num){
        if(getPlayer().reduceGold(num))
            betNum += num;
        return false;
    }

    public int getRoomPosition() {
        return roomPosition;
    }

    public void setRoomPosition(int roomPosition) {
        this.roomPosition = roomPosition;
    }

    public long getBetNum() {
        return betNum;
    }

    public void setBetNum(long betNum) {
        this.betNum = betNum;
    }


}
