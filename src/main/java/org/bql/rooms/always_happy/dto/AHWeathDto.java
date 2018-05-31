package org.bql.rooms.always_happy.dto;

import org.bql.utils.builder_clazz.ann.Protostuff;

@Protostuff
public class AHWeathDto {
    private int result;
    private long gold;
    private long resultMoney;

    public AHWeathDto() {
    }

    public AHWeathDto(int result, long gold, long resultMoney) {
        this.result = result;
        this.gold = gold;
        this.resultMoney = resultMoney;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public long getGold() {
        return gold;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }

    public long getResultMoney() {
        return resultMoney;
    }

    public void setResultMoney(long resultMoney) {
        this.resultMoney = resultMoney;
    }
}
