package org.zgl.redenvelope.model;

/**
 * @作者： big
 * @创建时间： 2018/6/22
 * @文件描述：
 */
public class RedEnvelopesModel {
    private int redEnvelopsCount;
    private long redEnvelops;

    public RedEnvelopesModel() {
    }

    public RedEnvelopesModel(int redEnvelopsCount, long redEnvelops) {
        this.redEnvelopsCount = redEnvelopsCount;
        this.redEnvelops = redEnvelops;
    }

    public int getRedEnvelopsCount() {
        return redEnvelopsCount;
    }

    public void setRedEnvelopsCount(int redEnvelopsCount) {
        this.redEnvelopsCount = redEnvelopsCount;
    }

    public long getRedEnvelops() {
        return redEnvelops;
    }

    public void setRedEnvelops(long redEnvelops) {
        this.redEnvelops = redEnvelops;
    }
    public void reduceCount(){
        redEnvelopsCount--;
    }
}
