package org.zgl.rooms.always_happy.model;

/**
 * @作者： big
 * @创建时间： 2018/6/25
 * @文件描述：
 */
public class AwardModel {
    private long uid;
    private long holdGold;
    private long winGold;

    public AwardModel() {
    }

    public AwardModel(long uid, long holdGold, long winGold) {
        this.uid = uid;
        this.holdGold = holdGold;
        this.winGold = winGold;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getHoldGold() {
        return holdGold;
    }

    public void setHoldGold(long holdGold) {
        this.holdGold = holdGold;
    }

    public long getWinGold() {
        return winGold;
    }

    public void setWinGold(long winGold) {
        this.winGold = winGold;
    }
}
