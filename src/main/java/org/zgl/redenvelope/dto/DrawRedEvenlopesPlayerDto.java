package org.zgl.redenvelope.dto;

import org.zgl.utils.builder_clazz.ann.Protostuff;

/**
 * @作者： big
 * @创建时间： 2018/6/26
 * @文件描述：
 */
@Protostuff
public class DrawRedEvenlopesPlayerDto {
    private long uid;
    private String userNmae;
    private int drawGold;
    private long gold;
    public DrawRedEvenlopesPlayerDto() {
    }

    public DrawRedEvenlopesPlayerDto(long uid, String userNmae, int drawGold,long gold) {
        this.uid = uid;
        this.userNmae = userNmae;
        this.drawGold = drawGold;
        this.gold = gold;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUserNmae() {
        return userNmae;
    }

    public void setUserNmae(String userNmae) {
        this.userNmae = userNmae;
    }

    public int getDrawGold() {
        return drawGold;
    }

    public void setDrawGold(int drawGold) {
        this.drawGold = drawGold;
    }

    public long getGold() {
        return gold;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }
}
