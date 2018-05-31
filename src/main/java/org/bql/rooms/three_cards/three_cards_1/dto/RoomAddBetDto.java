package org.bql.rooms.three_cards.three_cards_1.dto;

import org.bql.utils.builder_clazz.ann.Protostuff;

@Protostuff
public class RoomAddBetDto {
    /**财富发生变更的玩家*/
    private String updateAccount;
    /**下一位下注的玩家*/
    private String nextAccount;
    /**变更的财富值*/
    private long gold;
    /**加注之后房间当前筹码位置*/
    private int addBetPOsition;
    public RoomAddBetDto() {
    }

    public RoomAddBetDto(String updateAccount, String nextAccount, long gold, int addBetPOsition) {
        this.updateAccount = updateAccount;
        this.nextAccount = nextAccount;
        this.gold = gold;
        this.addBetPOsition = addBetPOsition;
    }

    public String getUpdateAccount() {
        return updateAccount;
    }

    public void setUpdateAccount(String updateAccount) {
        this.updateAccount = updateAccount;
    }

    public String getNextAccount() {
        return nextAccount;
    }

    public void setNextAccount(String nextAccount) {
        this.nextAccount = nextAccount;
    }

    public long getGold() {
        return gold;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }

    public int getAddBetPOsition() {
        return addBetPOsition;
    }

    public void setAddBetPOsition(int addBetPOsition) {
        this.addBetPOsition = addBetPOsition;
    }
}
