package org.bql.rooms.dice.model;

import org.bql.utils.builder_clazz.ann.Protostuff;

@Protostuff
public class DiceDto {
    private int one;
    private int two;
    /**场次*/
    private long battleCount;
    public DiceDto() {
    }

    public DiceDto(int one, int two, long battleCount) {
        this.one = one;
        this.two = two;
        this.battleCount = battleCount;
    }

    public int getOne() {
        return one;
    }

    public void setOne(int one) {
        this.one = one;
    }

    public int getTwo() {
        return two;
    }

    public long getBattleCount() {
        return battleCount;
    }

    public void setBattleCount(long battleCount) {
        this.battleCount = battleCount;
    }

    public void setTwo(int two) {
        this.two = two;
    }
}
