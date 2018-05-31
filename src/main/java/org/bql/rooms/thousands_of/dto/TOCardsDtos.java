package org.bql.rooms.thousands_of.dto;

import org.bql.utils.builder_clazz.ann.Protostuff;

import java.util.List;
@Protostuff
public class TOCardsDtos {
    List<TOCardsDto> cards;

    public TOCardsDtos() {
    }

    public TOCardsDtos(List<TOCardsDto> cards) {
        this.cards = cards;
    }

    public List<TOCardsDto> getCards() {
        return cards;
    }

    public void setCards(List<TOCardsDto> cards) {
        this.cards = cards;
    }
}
