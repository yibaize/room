package org.zgl.redenvelope.model;

import org.zgl.utils.builder_clazz.ann.Protostuff;

/**
 * @作者： big
 * @创建时间： 2018/6/23
 * @文件描述：已经领取红包的人的信息
 */
@Protostuff
public class RedEvenlopesGivePlayerModel {
    private long uid;
    private String userName;
    private String headIcon;
    private int getCount;
    private String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public int getGetCount() {
        return getCount;
    }

    public void setGetCount(int getCount) {
        this.getCount = getCount;
    }
}
