package org.bql.rooms.thousands_of.dto;

import org.bql.player.PlayerInfoDto;
import org.bql.player.PlayerRoomBaseInfoDto;
import org.bql.rooms.thousands_of.model.TOPlayer;
import org.bql.rooms.thousands_of.model.TORoom;
import org.bql.utils.builder_clazz.ann.Protostuff;

import java.util.ArrayList;
import java.util.List;

/**
 * 当前庄家信息
 * 房间人数
 * 房间状态
 * 房间剩余时间
 * 位置信息
 * 历史记录
 * @return
 */
@Protostuff
public class TORoomInfoDto {
    /**庄家 null为系统庄家*/
    private PlayerRoomBaseInfoDto banker;
    /**房间人数*/
    private int roomNum;
    /**房间状态*/
    private int roomState;
    /**房间剩余时间*/
    private int roomTimer;
    /**自己的位置*/
    private int selfPosition;
    /**房间的6个位置的信息*/
    private List<PlayerRoomBaseInfoDto> positionInfo;

    public TORoomInfoDto() {
    }

    public int getSelfPosition() {
        return selfPosition;
    }

    public void setSelfPosition(int selfPosition) {
        this.selfPosition = selfPosition;
    }

    public PlayerRoomBaseInfoDto getBanker() {
        return banker;
    }

    public void setBanker(PlayerRoomBaseInfoDto banker) {
        this.banker = banker;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    public int getRoomState() {
        return roomState;
    }

    public void setRoomState(int roomState) {
        this.roomState = roomState;
    }

    public int getRoomTimer() {
        return roomTimer;
    }

    public void setRoomTimer(int roomTimer) {
        this.roomTimer = roomTimer;
    }

    public List<PlayerRoomBaseInfoDto> getPositionInfo() {
        return positionInfo;
    }

    public void setPositionInfo(List<PlayerRoomBaseInfoDto> positionInfo) {
        this.positionInfo = positionInfo;
    }
    public TORoomInfoDto dto(TORoom room,String account){
        TOPlayer player = room.getPlayerSet().getBanker();
        if(player != null) {
            banker = base(room.getPlayerSet().getBanker());
        }
        roomNum = room.getPlayerSet().allPlayerNum();
        selfPosition = room.getPlayerSet().getPlayerForAccount(account).getPosition();
        roomState = room.getRoomState().id();
        roomTimer = room.getGamblingParty().getResidueTime();
        List<TOPlayer> toPlayers = room.getPlayerSet().positionPlayer();
        positionInfo = new ArrayList<>(toPlayers.size());
        for(TOPlayer t : toPlayers){
            positionInfo.add(base(t));
        }
        return this;
    }
    public PlayerRoomBaseInfoDto base(TOPlayer toPlayer){
        PlayerInfoDto playerInfoDto = toPlayer.getPlayer();
        PlayerRoomBaseInfoDto base = new PlayerRoomBaseInfoDto();
        base.setVipLv(playerInfoDto.getVipLv());
        base.setHeadIcon(playerInfoDto.getHeadIcon());
        base.setUserName(playerInfoDto.getUsername());
        base.setGold(playerInfoDto.getGold());
        base.setBottomNum(toPlayer.getBetNum());
        base.setPostion(toPlayer.getPosition());
        base.setAccount(playerInfoDto.getAccount());
        base.setAutoId(playerInfoDto.getNowUserAutos());
        base.setHasReady(false);
        return base;
    }
}
