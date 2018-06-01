package org.bql.rooms.dice.model;

import org.bql.player.PlayerRoom;

public class DicePlayer extends PlayerRoom {
    private long betNum;//下注数量
    public boolean bet(long num){
        if(getPlayer().reduceGold(num))
            betNum += num;
        return false;
    }
    public long getBetNum() {
        return betNum;
    }

    public void setBetNum(long betNum) {
        this.betNum = betNum;
    }
}
