package org.bql.player;

import org.bql.utils.builder_clazz.ann.Protostuff;

@Protostuff
public class PlayerRoomBaseInfoDto {
    private String account;
    private String userName;
    private long gold;
    private String headIcon;
    private int vipLv;
    private long bottomNum;
    private boolean hasReady;
    private int postion;
    private int autoId;

    public PlayerRoomBaseInfoDto() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getBottomNum() {
        return bottomNum;
    }

    public void setBottomNum(long bottomNum) {
        this.bottomNum = bottomNum;
    }

    public long getGold() {
        return gold;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public int getVipLv() {
        return vipLv;
    }

    public void setVipLv(int vipLv) {
        this.vipLv = vipLv;
    }

    public boolean isHasReady() {
        return hasReady;
    }

    public void setHasReady(boolean hasReady) {
        this.hasReady = hasReady;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getPostion() {
        return postion;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }

    public int getAutoId() {
        return autoId;
    }

    public void setAutoId(int autoId) {
        this.autoId = autoId;
    }
}
