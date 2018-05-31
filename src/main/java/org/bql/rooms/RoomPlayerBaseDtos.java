package org.bql.rooms;

import org.bql.utils.builder_clazz.ann.Protostuff;

import java.util.List;

/**
 * @作者： big
 * @创建时间： 2018/5/21
 * @文件描述：
 */
@Protostuff
public class RoomPlayerBaseDtos {
    List<RoomPlayerBaseDto> playerBaseDtos;

    public RoomPlayerBaseDtos() {
    }

    public RoomPlayerBaseDtos(List<RoomPlayerBaseDto> playerBaseDtos) {
        this.playerBaseDtos = playerBaseDtos;
    }

    public List<RoomPlayerBaseDto> getPlayerBaseDtos() {
        return playerBaseDtos;
    }

    public void setPlayerBaseDtos(List<RoomPlayerBaseDto> playerBaseDtos) {
        this.playerBaseDtos = playerBaseDtos;
    }
}
