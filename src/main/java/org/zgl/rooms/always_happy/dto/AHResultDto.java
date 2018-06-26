package org.zgl.rooms.always_happy.dto;

import org.zgl.utils.builder_clazz.ann.Protostuff;

import java.util.List;

/**
 * @作者： big
 * @创建时间： 2018/5/24
 * @文件描述：
 */
@Protostuff
public class AHResultDto {
    /**第几场*/
    private long num;
    /**出牌结果*/
    private int result;
    /**本局单双 8双 9单*/
    private int oddEnven;
    /**上期奖励*/
    private long lastTimeGrantAward;
    /**牌的id*/
    private List<Integer> cardIds;

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getOddEnven() {
        return oddEnven;
    }

    public void setOddEnven(int oddEnven) {
        this.oddEnven = oddEnven;
    }

    public long getLastTimeGrantAward() {
        return lastTimeGrantAward;
    }

    public void setLastTimeGrantAward(long lastTimeGrantAward) {
        this.lastTimeGrantAward = lastTimeGrantAward;
    }

    public List<Integer> getCardIds() {
        return cardIds;
    }

    public void setCardIds(List<Integer> cardIds) {
        this.cardIds = cardIds;
    }
}
