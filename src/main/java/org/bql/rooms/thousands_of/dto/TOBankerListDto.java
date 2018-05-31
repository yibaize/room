package org.bql.rooms.thousands_of.dto;

import org.bql.utils.builder_clazz.ann.Protostuff;

/**
 * @作者： big
 * @创建时间： 2018/5/25
 * @文件描述：
 */
@Protostuff
public class TOBankerListDto {
    /**昵称*/
    private String userName;
    /**金币*/
    private long gold;

    public TOBankerListDto() {
    }

    public TOBankerListDto(String userName, long gold) {
        this.userName = userName;
        this.gold = gold;
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
}
