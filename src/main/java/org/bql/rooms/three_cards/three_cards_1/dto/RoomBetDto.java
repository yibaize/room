package org.bql.rooms.three_cards.three_cards_1.dto;

import org.bql.utils.builder_clazz.ann.Protostuff;

@Protostuff
public class RoomBetDto {
    /**财富发生变更的玩家*/
    private String updateAccount;
    /**下一位下注的玩家*/
    private String nextAccount;
    /**变更的财富值*/
    private long gold;
    /**下注的玩家爱剩余多少金币*/
    private long residueGold;
    private long roomAllGOld;
    public RoomBetDto() {
    }

    public RoomBetDto(String updateAccount, String nextAccount, long gold, long residueGold, long roomAllGOld) {
        this.updateAccount = updateAccount;
        this.nextAccount = nextAccount;
        this.gold = gold;
        this.residueGold = residueGold;
        this.roomAllGOld = roomAllGOld;
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

    public long getResidueGold() {
        return residueGold;
    }

    public void setResidueGold(long residueGold) {
        this.residueGold = residueGold;
    }

    public long getRoomAllGOld() {
        return roomAllGOld;
    }

    public void setRoomAllGOld(long roomAllGOld) {
        this.roomAllGOld = roomAllGOld;
    }
}
