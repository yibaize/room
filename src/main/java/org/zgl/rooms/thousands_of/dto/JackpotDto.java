package org.zgl.rooms.thousands_of.dto;

import org.zgl.utils.builder_clazz.ann.Protostuff;
/**
 * @作者： big
 * @创建时间： 2018/6/21
 * @文件描述：
 */
@Protostuff
public class JackpotDto {
    private String userName;
    private long winGold;//中了多少奖
    private int awardType;//中奖类型

    public JackpotDto() {
    }

    public JackpotDto(String userName, long winGold, int awardType) {
        this.userName = userName;
        this.winGold = winGold;
        this.awardType = awardType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getWinGold() {
        return winGold;
    }

    public void setWinGold(long winGold) {
        this.winGold = winGold;
    }

    public int getAwardType() {
        return awardType;
    }

    public void setAwardType(int awardType) {
        this.awardType = awardType;
    }
}
