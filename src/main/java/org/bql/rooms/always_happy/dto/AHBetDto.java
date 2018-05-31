package org.bql.rooms.always_happy.dto;

import org.bql.utils.builder_clazz.ann.Protostuff;

/**
 * @作者： big
 * @创建时间： 2018/5/24
 * @文件描述：
 */
@Protostuff
public class AHBetDto {
    private int nowBetPlayerNum;
    private long allMoney;
    public AHBetDto() {
    }
    public AHBetDto(int nowBetPlayerNum, long allMoney) {
        this.nowBetPlayerNum = nowBetPlayerNum;
        this.allMoney = allMoney;
    }

    public long getAllMoney() {
        return allMoney;
    }

    public void setAllMoney(long allMoney) {
        this.allMoney = allMoney;
    }

    public int getNowBetPlayerNum() {
        return nowBetPlayerNum;
    }

    public void setNowBetPlayerNum(int nowBetPlayerNum) {
        this.nowBetPlayerNum = nowBetPlayerNum;
    }
}
