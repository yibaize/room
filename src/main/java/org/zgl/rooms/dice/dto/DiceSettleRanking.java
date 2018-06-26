package org.zgl.rooms.dice.dto;

import org.zgl.rooms.thousands_of.dto.BetUpdateDto;
import org.zgl.utils.builder_clazz.ann.Protostuff;

import java.util.List;

/**
 * @作者： big
 * @创建时间： 2018/6/16
 * @文件描述：
 */
@Protostuff
public class DiceSettleRanking {
    private int one;
    private int tow;
    /**本局四个位置赢钱最多的三个人*/
    private List<BetUpdateDto> ranking;

    public DiceSettleRanking() {
    }

    public DiceSettleRanking(int one, int tow, List<BetUpdateDto> ranking) {
        this.one = one;
        this.tow = tow;
        this.ranking = ranking;
    }

    public int getOne() {
        return one;
    }

    public void setOne(int one) {
        this.one = one;
    }

    public int getTow() {
        return tow;
    }

    public void setTow(int tow) {
        this.tow = tow;
    }

    public List<BetUpdateDto> getRanking() {
        return ranking;
    }

    public void setRanking(List<BetUpdateDto> ranking) {
        this.ranking = ranking;
    }
}
