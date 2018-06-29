package org.zgl.redenvelope.dto;

/**
 * @作者： big
 * @创建时间： 2018/6/28
 * @文件描述：
 */
public class FriendRedEnvelopesDto implements Comparable<FriendRedEnvelopesDto>{
    private int id;//红包的id
    private long uid;//发红包的人uid
    private String userName;//发红包人的昵称
    private String headIcon;
    private long gold;//发了多少
    private boolean hasGet;//是否已经领取
    private long createTime;
    private long lastEditTime;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getGold() {
        return gold;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }

    public boolean isHasGet() {
        return hasGet;
    }

    public void setHasGet(boolean hasGet) {
        this.hasGet = hasGet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(long lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    @Override
    public int compareTo(FriendRedEnvelopesDto o) {
        if(hasGet)
            return -1;
        return o.getId() - id;
    }
}
