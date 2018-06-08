package org.bql.rooms.three_cards.three_cards_1.dto;

import org.bql.player.PlayerRoomBaseInfoDto;
import org.bql.utils.builder_clazz.ann.Protostuff;

import java.util.List;
@Protostuff
public class PlayerRoomDto {
    private int roomId;
    private int roomState;
    private int selfPosition;
    /**换牌卡数量*/
    private int exchangeCardCount;
    /**房间其他人的信息*/
    private List<PlayerRoomBaseInfoDto> players;

    public PlayerRoomDto() {
    }

    public PlayerRoomDto(int roomId, int roomState, int selfPosition, int exchangeCardCount, List<PlayerRoomBaseInfoDto> players) {
        this.roomId = roomId;
        this.roomState = roomState;
        this.selfPosition = selfPosition;
        this.exchangeCardCount = exchangeCardCount;
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

    public int getExchangeCardCount() {
        return exchangeCardCount;
    }

    public void setExchangeCardCount(int exchangeCardCount) {
        this.exchangeCardCount = exchangeCardCount;
    }
}
