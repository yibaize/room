package org.bql.rooms.thousands_of.model;

import org.bql.player.PlayerRoom;

public class TOPlayer extends PlayerRoom {
    public static final int DEFAULT_POS = -165327;
    private int position = DEFAULT_POS;//当前玩家是否有位置
    private long betNum;//下注金额
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public long getBetNum() {
        return betNum;
    }

    public void setBetNum(long betNum) {
        this.betNum = betNum;
    }

    public void bet(long num){
        betNum += num;
    }
}
