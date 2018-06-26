package org.zgl.rooms.card;

import java.util.HashMap;
import java.util.Map;

public enum CardType {
    HIGH_CARD(1,1,1F,0),//散牌
    PAIR(2,1,100000,0),//对子
    STRAIGHT(3,2,250000,0),//顺子
    SAME_COLOR(4,3,350000,0),//同花
    STRAIGHT_FLUSH(5,4,3800000,3),//同花顺
    LEOPARD(6,5,5000000,10),//豹子
    AAA(7,5,30000000,25);//aaa
    private int id;
    /**万人场倍率*/
    private int rate;
    /**时时乐倍率*/
    private float ahRate;
    private int jackpot;//奖池倍率
    private CardType(int id,int rate,float ahRate,int jackpot) {
        this.id = id;
        this.rate = rate;
        this.ahRate = ahRate;
        this.jackpot = jackpot;
    }
    public int id(){
        return id;
    }
    public int rate(){
        return rate;
    }
    public int jackpot(){return jackpot;}
    public float ahRate(){
        return ahRate;
    }
    private static final Map<Integer,CardType> map;
    static {
        map = new HashMap<>();
        for(CardType c:CardType.values()){
            map.putIfAbsent(c.id(),c);
        }
    }
    public static CardType getType(int id){
        return map.get(id);
    }
}
