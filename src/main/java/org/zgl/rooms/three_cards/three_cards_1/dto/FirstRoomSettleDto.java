package org.zgl.rooms.three_cards.three_cards_1.dto;

import org.zgl.utils.builder_clazz.ann.Protostuff;

import java.util.List;
@Protostuff
public class FirstRoomSettleDto {
    private String account;//赢的玩家的账号
    private int cardType;//牌型
    private long winPlayerGetNum;//赢的玩家获得多少钱
    private List<Integer> cardIds;//赢的玩家的牌
    private List<SettleLoseModelDto> settleModelDtos;//所有玩的玩家每个人输了多少

    public long getWinPlayerGetNum() {
        return winPlayerGetNum;
    }

    public void setWinPlayerGetNum(long winPlayerGetNum) {
        this.winPlayerGetNum = winPlayerGetNum;
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

    public List<SettleLoseModelDto> getSettleModelDtos() {
        return settleModelDtos;
    }

    public void setSettleModelDtos(List<SettleLoseModelDto> settleModelDtos) {
        this.settleModelDtos = settleModelDtos;
    }
}
