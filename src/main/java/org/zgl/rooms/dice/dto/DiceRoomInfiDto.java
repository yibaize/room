package org.zgl.rooms.dice.dto;

import org.zgl.player.PlayerInfoDto;
import org.zgl.player.PlayerRoomBaseInfoDto;
import org.zgl.rooms.RoomAbs;
import org.zgl.rooms.dice.model.DicePlayer;
import org.zgl.rooms.dice.model.DiceRoom;
import org.zgl.utils.builder_clazz.ann.Protostuff;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者： big
 * @创建时间： 2018/6/11
 * @文件描述：
 */
@Protostuff
public class DiceRoomInfiDto {
    private int betPlayerNum;
    private long betAllNum;
    private int roomNum;
    private int roomState;
    private int roomTimer;
    private int selfPosition;
    private long betLimit;
    //座位上的人
    List<PlayerRoomBaseInfoDto> positionInfo;

    public int getBetPlayerNum() {
        return betPlayerNum;
    }

    public void setBetPlayerNum(int betPlayerNum) {
        this.betPlayerNum = betPlayerNum;
    }

    public long getBetAllNum() {
        return betAllNum;
    }

    public void setBetAllNum(long betAllNum) {
        this.betAllNum = betAllNum;
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

    public int getSelfPosition() {
        return selfPosition;
    }

    public long getBetLimit() {
        return betLimit;
    }

    public void setBetLimit(long betLimit) {
        this.betLimit = betLimit;
    }

    public void setSelfPosition(int selfPosition) {
        this.selfPosition = selfPosition;
    }

    public List<PlayerRoomBaseInfoDto> getPositionInfo() {
        return positionInfo;
    }

    public void setPositionInfo(List<PlayerRoomBaseInfoDto> positionInfo) {
        this.positionInfo = positionInfo;
    }

    public DiceRoomInfiDto dto(DiceRoom room, String account){
        roomNum = room.getPlayerSet().getAllPlayer().size();
        selfPosition = room.getPlayerSet().getPlayerForAccount(account).getRoomPosition();
        roomState = room.getRoomState().id();
        roomTimer = room.getGamblingParty().getResidueTime();
        betPlayerNum = room.getGamblingParty().getBetPlayerNum();
        betAllNum = room.getGamblingParty().getAllMoney();
        betLimit = RoomAbs.betLimit;
        List<DicePlayer> toPlayers = room.getPlayerSet().postionAllPlayer();
        positionInfo = new ArrayList<>(toPlayers.size());
        for(DicePlayer t : toPlayers){
            positionInfo.add(base(t));
        }
        return this;
    }
    private PlayerRoomBaseInfoDto base(DicePlayer toPlayer){
        PlayerInfoDto playerInfoDto = toPlayer.getPlayer();
        PlayerRoomBaseInfoDto base = new PlayerRoomBaseInfoDto();
        base.setUid(playerInfoDto.getUid());
        base.setVipLv(playerInfoDto.getVipLv());
        base.setHeadIcon(playerInfoDto.getHeadIcon());
        base.setUserName(playerInfoDto.getUsername());
        base.setGold(playerInfoDto.getGold());
        base.setBottomNum(toPlayer.getDiceBet());
        base.setPostion(toPlayer.getRoomPosition());
        base.setAccount(playerInfoDto.getAccount());
        base.setAutoId(playerInfoDto.getNowUserAutos());
        base.setHasReady(false);
        base.setGender(playerInfoDto.getGender());
        return base;
    }
}
