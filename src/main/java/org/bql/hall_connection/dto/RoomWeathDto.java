package org.bql.hall_connection.dto;

import org.bql.utils.builder_clazz.ann.Protostuff;

@Protostuff
public class RoomWeathDto {
    private String account;
    private long gold;
    private long diamond;
    private long integral;
    /**是否赢了*/
    private boolean hasWin;
    /**赢钱类型*/
    private int winCardType;
    public RoomWeathDto() {
    }

    public RoomWeathDto(String account, long gold, long diamond, long integral, boolean hasWin, int winCardType) {
        this.account = account;
        this.gold = gold;
        this.diamond = diamond;
        this.integral = integral;
        this.hasWin = hasWin;
        this.winCardType = winCardType;
    }

    public long getGold() {
        return gold;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }

    public long getDiamond() {
        return diamond;
    }

    public void setDiamond(long diamond) {
        this.diamond = diamond;
    }

    public long getIntegral() {
        return integral;
    }

    public void setIntegral(long integral) {
        this.integral = integral;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public boolean isHasWin() {
        return hasWin;
    }

    public void setHasWin(boolean hasWin) {
        this.hasWin = hasWin;
    }

    public int getWinCardType() {
        return winCardType;
    }

    public void setWinCardType(int winCardType) {
        this.winCardType = winCardType;
    }
}
