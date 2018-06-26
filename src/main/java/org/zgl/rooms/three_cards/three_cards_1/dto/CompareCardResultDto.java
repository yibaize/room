package org.zgl.rooms.three_cards.three_cards_1.dto;

import org.zgl.utils.builder_clazz.ann.Protostuff;

import java.util.List;

@Protostuff
public class CompareCardResultDto {
    private String account;//输的玩家的账号
    private String targetAccount;
    private int cardType;
    private List<Integer> cardIds;
    public CompareCardResultDto() {
    }

    public CompareCardResultDto(String account, String targetAccount, int cardType, List<Integer> cardIds) {
        this.account = account;
        this.targetAccount = targetAccount;
        this.cardType = cardType;
        this.cardIds = cardIds;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public List<Integer> getCardIds() {
        return cardIds;
    }

    public void setCardIds(List<Integer> cardIds) {
        this.cardIds = cardIds;
    }

    public String getTargetAccount() {
        return targetAccount;
    }

    public void setTargetAccount(String targetAccount) {
        this.targetAccount = targetAccount;
    }
}
