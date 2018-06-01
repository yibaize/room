package org.bql.rooms.three_cards.three_cards_1.dto;

import org.bql.utils.builder_clazz.ann.Protostuff;

import java.util.List;

/**
 * @作者： big
 * @创建时间： 18-6-1
 * @文件描述：
 */
@Protostuff
public class BetAllDto {
    private String winAccount;//赢的玩家的账号
    private int winCardType;//牌型
    private long winPlayerGetNum;//赢的玩家获得多少钱
    private List<Integer> winCardIds;//赢的玩家的牌
    private String loseAccount;
    private int loseCardType;
    private List<Integer> loseCardIds;
    private List<SettleModelDto> settleModelDtos;//所有玩的玩家每个人输了多少

    public String getWinAccount() {
        return winAccount;
    }

    public void setWinAccount(String winAccount) {
        this.winAccount = winAccount;
    }

    public int getWinCardType() {
        return winCardType;
    }

    public void setWinCardType(int winCardType) {
        this.winCardType = winCardType;
    }

    public long getWinPlayerGetNum() {
        return winPlayerGetNum;
    }

    public void setWinPlayerGetNum(long winPlayerGetNum) {
        this.winPlayerGetNum = winPlayerGetNum;
    }

    public List<Integer> getWinCardIds() {
        return winCardIds;
    }

    public void setWinCardIds(List<Integer> winCardIds) {
        this.winCardIds = winCardIds;
    }

    public String getLoseAccount() {
        return loseAccount;
    }

    public void setLoseAccount(String loseAccount) {
        this.loseAccount = loseAccount;
    }

    public int getLoseCardType() {
        return loseCardType;
    }

    public void setLoseCardType(int loseCardType) {
        this.loseCardType = loseCardType;
    }

    public List<Integer> getLoseCardIds() {
        return loseCardIds;
    }

    public void setLoseCardIds(List<Integer> loseCardIds) {
        this.loseCardIds = loseCardIds;
    }

    public List<SettleModelDto> getSettleModelDtos() {
        return settleModelDtos;
    }

    public void setSettleModelDtos(List<SettleModelDto> settleModelDtos) {
        this.settleModelDtos = settleModelDtos;
    }
}
