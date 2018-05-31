package org.bql.rooms.thousands_of.dto;

import org.bql.utils.builder_clazz.ann.Protostuff;

@Protostuff
public class BetUpdateDto implements Comparable<BetUpdateDto>{
    /**下注的玩家账号 如果在位置上前端得显示这个玩家下注了多少并且扣掉这么多的金币*/
    private String account;
    /**下注了多少金币*/
    private long gold;
    /**本剧赢了多少钱*/
    private long winGld;
    /**下注的位置*/
    private int position;
    public BetUpdateDto() {
    }

    public BetUpdateDto(String account, long gold, long winGld, int position) {
        this.account = account;
        this.gold = gold;
        this.winGld = winGld;
        this.position = position;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public long getGold() {
        return gold;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public long getWinGld() {
        return winGld;
    }

    public void setWinGld(long winGld) {
        this.winGld = winGld;
    }

    @Override
    public boolean equals(Object obj) {
        BetUpdateDto dto = (BetUpdateDto) obj;
        return dto.getAccount().equals(account);
    }

    @Override
    public int compareTo(BetUpdateDto o) {
        return (int) (o.gold - this.gold);
    }
}
