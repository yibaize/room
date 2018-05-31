package org.bql.rooms.great_pretenders.dto;

import org.bql.utils.builder_clazz.ann.Protostuff;

@Protostuff
public class GPRoomExchangeCardDto {
    private String account;
    private int cardId;

    public GPRoomExchangeCardDto() {
    }

    public GPRoomExchangeCardDto(String account, int cardId) {
        this.account = account;
        this.cardId = cardId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }
}
