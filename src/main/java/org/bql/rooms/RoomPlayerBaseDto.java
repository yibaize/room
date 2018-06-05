package org.bql.rooms;

import org.bql.player.PlayerInfoDto;
import org.bql.utils.builder_clazz.ann.Protostuff;

/**
 * @作者： big
 * @创建时间： 2018/5/21
 * @文件描述：
 */
@Protostuff
public class RoomPlayerBaseDto implements Comparable<RoomPlayerBaseDto>{
    private String username;
    private long gold;

    public RoomPlayerBaseDto() {
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getGold() {
        return gold;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }

    public RoomPlayerBaseDto baseDto(PlayerInfoDto dto){
        this.username  =dto.getUsername();
        this.gold = dto.getGold();
        return this;
    }

    @Override
    public int compareTo(RoomPlayerBaseDto o) {
        return (int) (this.gold - o.getGold());
    }
}
