package org.bql.rooms.three_cards.three_cards_1.dto;

import org.bql.utils.builder_clazz.ann.Protostuff;

@Protostuff
public class FirstRoomStartDto {
    /**轮到该账号的玩家位置下注*/
    private String account;

    public FirstRoomStartDto() {
    }

    public FirstRoomStartDto(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
