package org.bql.rooms.three_cards.three_cards_1.model;

import java.util.Arrays;

public class HandCard {
    private int cardType;//牌型
    private Integer[] cardFaces;
    private Integer[] cardIds;
    private int max;//最大的牌面（如果是对子就是其中一个）
    private int min;//最小的牌面（如果是对子就是散的那个）
    private boolean compareResult;//比较后的结果，输还是赢
    private int position;//持有手牌的位置
    public HandCard() {
    }

    public HandCard(int cardType, Integer[] cardFaces, Integer[] cardIds) {
        this.cardType = cardType;
        this.cardFaces = cardFaces;
        this.cardIds = cardIds;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public Integer[] getCardFaces() {
        return cardFaces;
    }

    public void setCardFaces(Integer[] cardFaces) {
        this.cardFaces = cardFaces;
    }

    public Integer[] getCardIds() {
        return cardIds;
    }

    public void setCardIds(Integer[] cardIds) {
        this.cardIds = cardIds;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public boolean isCompareResult() {
        return compareResult;
    }

    public void setCompareResult(boolean compareResult) {
        this.compareResult = compareResult;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "HandCard{" +
                "cardType=" + cardType +
                ", cardFaces=" + (cardFaces == null ? null : Arrays.asList(cardFaces)) +
                ", cardIds=" + (cardIds == null ? null : Arrays.asList(cardIds)) +
                ", max=" + max +
                ", min=" + min +
                ", compareResult=" + compareResult +
                ", position=" + position +
                '}';
    }
}
