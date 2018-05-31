package org.bql.hall_connection.dto;

/**
 * @作者： big
 * @创建时间： 18-5-31
 * @文件描述：
 */

public class ShopBuySyncDto {
    private String account;
    private long gold;
    private long diamond;
    private long integral;
    private int vipLv;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public int getVipLv() {
        return vipLv;
    }

    public void setVipLv(int vipLv) {
        this.vipLv = vipLv;
    }
}
