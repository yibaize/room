package org.bql.rooms;

import org.bql.player.PlayerInfoDto;
import org.bql.utils.builder_clazz.ann.Protostuff;

/**
 * @作者： big
 * @创建时间： 2018/5/21
 * @文件描述：
 */
@Protostuff
public class RoomPlayerBaseDto {
    private int id;
    private String username;
    private String account;
    private long gold;
    private int vipLv;
    private String headIcon;

    public RoomPlayerBaseDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public long getGold() {
        return gold;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }

    public int getVipLv() {
        return vipLv;
    }

    public void setVipLv(int vipLv) {
        this.vipLv = vipLv;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }
    public RoomPlayerBaseDto baseDto(PlayerInfoDto dto){
        this.id = dto.getId();
        this.account = dto.getAccount();
        this.headIcon = dto.getHeadIcon();
        this.username  =dto.getUsername();
        this.vipLv = dto.getVipLv();
        this.gold = dto.getGold();
        return this;
    }
}
