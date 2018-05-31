package org.bql.rooms.three_cards.three_cards_1.dto;

import org.bql.rooms.three_cards.three_cards_1.model.HandCard;
import org.bql.utils.ArrayUtils;
import org.bql.utils.builder_clazz.ann.Protostuff;

import java.util.List;

@Protostuff
public class CardsDto {
    private int cardType;
    private List<Integer> cardIds;
    public CardsDto() {
    }

    public CardsDto(int cardType, List<Integer> cardIds) {
        this.cardType = cardType;
        this.cardIds = cardIds;
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

    public CardsDto dto(HandCard f){
        this.cardIds = ArrayUtils.arrayToList(f.getCardIds());
        this.cardType = f.getCardType();
        return this;
    }
}
