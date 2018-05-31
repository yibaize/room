package org.bql.rooms.three_cards.three_cards_1.dto;

import org.bql.utils.builder_clazz.ann.Protostuff;

@Protostuff
public class SettleModelDto {
    private String account;
    private long money;

    public SettleModelDto() {
    }

    public SettleModelDto(String account, long money) {
        this.account = account;
        this.money = money;
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
}
