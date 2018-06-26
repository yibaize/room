package org.zgl.rooms.three_cards.three_cards_1.dto;

import org.zgl.utils.builder_clazz.ann.Protostuff;

/**
 * 房间某个玩家点击
 */
@Protostuff
public class RoomPlayerAccountDto {
    private String account;
    public RoomPlayerAccountDto() {
    }
    public RoomPlayerAccountDto(String account) {
        this.account = account;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }

}
