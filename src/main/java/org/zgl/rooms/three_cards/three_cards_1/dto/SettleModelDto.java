package org.zgl.rooms.three_cards.three_cards_1.dto;

import org.zgl.utils.builder_clazz.ann.Protostuff;

@Protostuff
public class SettleModelDto {
    private String account;
    private long money;
    private long roomAllGold;
    public SettleModelDto() {
    }

    public SettleModelDto(String account, long money, long roomAllGold) {
        this.account = account;
        this.money = money;
        this.roomAllGold = roomAllGold;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public long getRoomAllGold() {
        return roomAllGold;
    }

    public void setRoomAllGold(long roomAllGold) {
        this.roomAllGold = roomAllGold;
    }
}
