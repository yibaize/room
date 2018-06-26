package org.zgl.rooms.three_cards.three_cards_1.dto;

import org.zgl.utils.builder_clazz.ann.Protostuff;

/**
 * @作者： big
 * @创建时间： 2018/6/2
 * @文件描述：
 */
@Protostuff
public class BetAllResponseDto {
    private long money;
    private long roomAllGold;
    private long betGold;

    public BetAllResponseDto() {
    }

    public BetAllResponseDto(long money, long roomAllGold, long betGold) {
        this.money = money;
        this.roomAllGold = roomAllGold;
        this.betGold = betGold;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public long getRoomAllGold() {
        return roomAllGold;
    }

    public void setRoomAllGold(long roomAllGold) {
        this.roomAllGold = roomAllGold;
    }

    public long getBetGold() {
        return betGold;
    }

    public void setBetGold(long betGold) {
        this.betGold = betGold;
    }
}
