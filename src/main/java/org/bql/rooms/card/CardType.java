package org.bql.rooms.card;

import java.util.HashMap;
import java.util.Map;

public enum CardType {
    HIGH_CARD(1,1,1F),//散牌
    PAIR(2,1,10F),//对子
    STRAIGHT(3,2,12.5F),//顺子
    SAME_COLOR(4,3,17.5F),//同花
    STRAIGHT_FLUSH(5,4,190F),//同花顺
    LEOPARD(6,5,250F),//豹子
    AAA(7,5,1500F);//aaa
    private int id;
    /**万人场倍率*/
    private int rate;
    /**时时乐倍率*/
    private float ahRate;
    private CardType(int id,int rate,float ahRate) {
        this.id = id;
        this.rate = rate;
        this.ahRate = ahRate;
    }
    public int id(){
        return id;
    }
    public int rate(){
        return rate;
    }
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
