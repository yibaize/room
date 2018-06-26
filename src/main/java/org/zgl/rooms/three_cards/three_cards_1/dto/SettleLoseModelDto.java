package org.zgl.rooms.three_cards.three_cards_1.dto;

import org.zgl.utils.builder_clazz.ann.Protostuff;

import java.util.List;

/**
 * @作者： big
 * @创建时间： 2018/6/2
 * @文件描述：最后结算输的所有玩家的手牌信息
 */
@Protostuff
public class SettleLoseModelDto {
    /**账号*/
    private String account;
    /**输了多少钱*/
    private long loseMoney;
    /**牌id*/
    private List<Integer> cardIds;
    /**牌类型*/
    private int cardType;

    public SettleLoseModelDto() {
    }

    public SettleLoseModelDto(String account, long money, List<Integer> cardIds, int cardType) {
        this.account = account;
        this.loseMoney = money;
        this.cardIds = cardIds;
        this.cardType = cardType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public long getLoseMoney() {
        return loseMoney;
    }

    public void setLoseMoney(long loseMoney) {
        this.loseMoney = loseMoney;
    }

    public List<Integer> getCardIds() {
        return cardIds;
    }

    public void setCardIds(List<Integer> cardIds) {
        this.cardIds = cardIds;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }
}
