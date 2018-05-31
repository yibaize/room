package org.bql.rooms.three_cards.three_cards_1.dto;

import org.bql.player.PlayerRoomBaseInfoDto;
import org.bql.utils.builder_clazz.ann.Protostuff;

import java.util.List;
@Protostuff
public class PlayerRoomDto {
    private int roomId;
    private int roomState;
    private int selfPosition;
    /**房间其他人的信息*/
    private List<PlayerRoomBaseInfoDto> players;

    public PlayerRoomDto() {
    }

    public PlayerRoomDto(int roomId, int roomState, int selfPosition, List<PlayerRoomBaseInfoDto> players) {
        this.roomId = roomId;
        this.roomState = roomState;
        this.selfPosition = selfPosition;
        this.players = players;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public List<PlayerRoomBaseInfoDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerRoomBaseInfoDto> players) {
        this.players = players;
    }

    public int getRoomState() {
        return roomState;
    }

    public void setRoomState(int roomState) {
        this.roomState = roomState;
    }

    public int getSelfPosition() {
        return selfPosition;
    }

    public void setSelfPosition(int selfPosition) {
        this.selfPosition = selfPosition;
    }
}
