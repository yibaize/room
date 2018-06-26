package org.zgl.rooms.always_happy.dto;

import org.zgl.utils.builder_clazz.ann.Protostuff;

import java.util.List;

/**
 * @作者： big
 * @创建时间： 2018/5/24
 * @文件描述：
 */
@Protostuff
public class AHHistoryDto {
    /**第几场*/
    private long num;
    /**出牌结果*/
    private int result;
    /**本局单双 8双 9单*/
    private int oddEnven;
    /**牌的id*/
    private List<Integer> cardIds;

    public AHHistoryDto() {
    }

    public AHHistoryDto(long num, int result, int oddEnven, List<Integer> cardIds) {
        this.num = num;
        this.result = result;
        this.oddEnven = oddEnven;
        this.cardIds = cardIds;
    }

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

    public List<Integer> getCardIds() {
        return cardIds;
    }

    public int getOddEnven() {
        return oddEnven;
    }

    public void setOddEnven(int oddEnven) {
        this.oddEnven = oddEnven;
    }

    public void setCardIds(List<Integer> cardIds) {
        this.cardIds = cardIds;
    }

    @Override
    public String toString() {
        return "AHHistoryDto{" +
                "num=" + num +
                ", result=" + result +
                ", oddEnven=" + oddEnven +
                ", cardIds=" + cardIds +
                '}';
    }
}
