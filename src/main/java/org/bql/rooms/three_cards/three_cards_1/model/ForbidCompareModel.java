package org.bql.rooms.three_cards.three_cards_1.model;

/**
 * @作者： big
 * @创建时间： 2018/5/22
 * @文件描述：禁比卡
 */
public class ForbidCompareModel {
    /**被禁比的玩家账号*/
    private String account;
    /**禁止比几轮了*/
    private int count;

    public ForbidCompareModel() {
    }

    public ForbidCompareModel(String account, int count) {
        this.account = account;
        this.count = count;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    public void add(){
        count++;
    }
    public void reduce(){
        count--;
    }
}
