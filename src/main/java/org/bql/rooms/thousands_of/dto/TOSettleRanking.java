package org.bql.rooms.thousands_of.dto;

import org.bql.utils.builder_clazz.ann.Protostuff;

import java.util.List;

/**
 * @作者： big
 * @创建时间： 2018/5/19
 * @文件描述：四个下注位置中赢钱最多的那三个人
 */
@Protostuff
public class TOSettleRanking {
    /**本局四个位置赢钱最多的三个人*/
    private List<BetUpdateDto> ranking;

    public TOSettleRanking() {
    }

    public TOSettleRanking( List<BetUpdateDto> ranking) {
        this.ranking = ranking;
    }

    public List<BetUpdateDto> getRanking() {
        return ranking;
    }

    public void setRanking(List<BetUpdateDto> ranking) {
        this.ranking = ranking;
    }
}
