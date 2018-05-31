package org.bql.rooms.three_cards.three_cards_1.manage;

import org.bql.player.PlayerInfoDto;
import org.bql.player.PlayerRoom;
import org.bql.player.PlayerRoomBaseInfoDto;
import org.bql.rooms.three_cards.three_cards_1.model.HandCard;

public class FirstPlayerRoom extends PlayerRoom {
    private long startTime;
    private boolean hasLookCard;//是否已经看牌
    private HandCard handCard;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public HandCard getHandCard() {
        return handCard;
    }

    public void setHandCard(HandCard handCard) {
        this.handCard = handCard;
    }

    public boolean isHasLookCard() {
        return hasLookCard;
    }

    public void setHasLookCard(boolean hasLookCard) {
        this.hasLookCard = hasLookCard;
    }

    public PlayerRoomBaseInfoDto roomPlayerDto(){
        PlayerRoomBaseInfoDto roomPlayerDto = new PlayerRoomBaseInfoDto();
        PlayerInfoDto player = getPlayer();
        roomPlayerDto.setGold(player.getGold());
        roomPlayerDto.setHeadIcon(player.getHeadIcon());
        roomPlayerDto.setUserName(player.getUsername());
        roomPlayerDto.setVipLv(player.getVipLv());
        roomPlayerDto.setAccount(player.getAccount());
        roomPlayerDto.setAutoId(player.getNowUserAutos());
        return roomPlayerDto;
    }

    /**
     * 房间财富变更
     * @param isNotifyHall 是否需要同步到大厅
     */
//    public void updateWeath(boolean isNotifyHall){
//        ByteBuf buf = NettySerializable.getBuffer();
//        buf.writeLong(getPlayer().getGold());
//        byte[] bytes = new byte[buf.readableBytes()];
//        buf.readBytes(bytes);
//        Response response = new Response(NotifyCode.ROOM_WEATH_UPDATE,bytes);
//        getSession().write(response);
//        if(isNotifyHall){
//            //同步大厅
//        }
//    }
}
