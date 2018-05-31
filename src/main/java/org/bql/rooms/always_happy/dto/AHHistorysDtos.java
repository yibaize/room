package org.bql.rooms.always_happy.dto;

import org.bql.utils.builder_clazz.ann.Protostuff;

import java.util.List;

/**
 * @作者： big
 * @创建时间： 2018/5/24
 * @文件描述：
 */
@Protostuff
public class AHHistorysDtos {
    /**当前在线人数*/
    private int playerNum;
    /**上期奖励*/
    private long lastTimeGrantAward;
    /**当前下注所有金额*/
    private long nowBetMoney;
    /**房间剩余时间*/
    private int roomTime;
    private List<AHHistoryDto> historyDtos;

    public AHHistorysDtos() {
    }
    public List<AHHistoryDto> getHistoryDtos() {
        return historyDtos;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public long getLastTimeGrantAward() {
        return lastTimeGrantAward;
    }

    public void setLastTimeGrantAward(long lastTimeGrantAward) {
        this.lastTimeGrantAward = lastTimeGrantAward;
    }

    public long getNowBetMoney() {
        return nowBetMoney;
    }

    public void setNowBetMoney(long nowBetMoney) {
        this.nowBetMoney = nowBetMoney;
    }

    public void setHistoryDtos(List<AHHistoryDto> historyDtos) {
        this.historyDtos = historyDtos;
    }

    public int getRoomTime() {
        return roomTime;
    }

    public void setRoomTime(int roomTime) {
        this.roomTime = roomTime;
    }
}
