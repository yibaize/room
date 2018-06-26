package org.zgl.givegift;

import org.zgl.utils.builder_clazz.ann.Protostuff;

/**
 * @作者： big
 * @创建时间： 2018/6/23
 * @文件描述：
 */
@Protostuff
public class GiveGiftDto {
    private String userName;
    private int giftId;

    public GiveGiftDto() {
    }

    public GiveGiftDto(String userName, int giftId) {
        this.userName = userName;
        this.giftId = giftId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }
}
