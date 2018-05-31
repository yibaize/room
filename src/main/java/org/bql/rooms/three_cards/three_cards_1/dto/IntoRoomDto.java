package org.bql.rooms.three_cards.three_cards_1.dto;

import org.bql.utils.builder_clazz.ann.Protostuff;

@Protostuff
public class IntoRoomDto {
    /**房间号*/
    private int roomId;

    public IntoRoomDto() {
    }

    public IntoRoomDto(int roomId) {
        this.roomId = roomId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

}
