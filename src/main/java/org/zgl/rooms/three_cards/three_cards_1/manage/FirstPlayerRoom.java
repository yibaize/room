package org.zgl.rooms.three_cards.three_cards_1.manage;

import org.zgl.player.PlayerInfoDto;
import org.zgl.player.PlayerRoom;
import org.zgl.player.PlayerRoomBaseInfoDto;
import org.zgl.rooms.three_cards.three_cards_1.model.HandCard;

public class FirstPlayerRoom extends PlayerRoom {
    private long startTime;
    private boolean hasLookCard;//是否已经看牌
    private int exchangeCardCount;
    private HandCard handCard;
    private long firstExchangeTime;//上次交换房间的时间 间隔不能少于5秒
    public int getExchangeCardCount() {
        return exchangeCardCount;
    }

    public long getFirstExchangeTime() {
        return firstExchangeTime;
    }

    public void setFirstExchangeTime(long firstExchangeTime) {
        this.firstExchangeTime = firstExchangeTime;
    }

    public void setExchangeCardCount(int num) {
        this.exchangeCardCount = num;
    }
    public void addExchangeCardCount(int num){
        this.exchangeCardCount += num;
    }
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
        roomPlayerDto.setUid(player.getUid());
        roomPlayerDto.setGold(player.getGold());
        roomPlayerDto.setHeadIcon(player.getHeadIcon());
        roomPlayerDto.setUserName(player.getUsername());
        roomPlayerDto.setVipLv(player.getVipLv());
        roomPlayerDto.setAccount(player.getAccount());
        roomPlayerDto.setAutoId(player.getNowUserAutos());
        roomPlayerDto.setGender(player.getGender());
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
