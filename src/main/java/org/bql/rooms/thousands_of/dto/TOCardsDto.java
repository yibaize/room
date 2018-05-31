package org.bql.rooms.thousands_of.dto;

import org.bql.rooms.three_cards.three_cards_1.model.HandCard;
import org.bql.utils.ArrayUtils;
import org.bql.utils.builder_clazz.ann.Protostuff;

import java.util.List;
@Protostuff
public class TOCardsDto {
    /**哪个位置*/
    private int position;
    /**牌型*/
    private int cardType;
    /**输或者赢*/
    private boolean result;
    /**牌的id*/
    private List<Integer> cardIds;

    public TOCardsDto() {
    }

    public int getCardType() {
        return cardType;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public List<Integer> getCardIds() {
        return cardIds;
    }

    public void setCardIds(List<Integer> cardIds) {
        this.cardIds = cardIds;
    }
    public TOCardsDto dto(int position,HandCard handCard){
        this.position = position;
        this.cardIds = ArrayUtils.arrayToList(handCard.getCardIds());
        this.cardType = handCard.getCardType();
        this.result = handCard.isCompareResult();
        return this;
    }

    @Override
    public String toString() {
        return "TOCardsDto{" +
                "position=" + position +
                ", cardType=" + cardType +
                ", result=" + result +
                ", cardIds=" + cardIds +
                '}';
    }
}
