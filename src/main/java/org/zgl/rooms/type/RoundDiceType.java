package org.zgl.rooms.type;

import java.util.HashMap;
import java.util.Map;

/**
 * @作者： big
 * @创建时间： 2018/6/14
 * @文件描述：
 */
public enum RoundDiceType {
    NONE(0,0,0),
    //围骰
    ROUND_FOUR(14,30,4),//围4
    ROUND_SIX(15,30,6),//围6
    ROUND(16,5,16),//围10
    ROUND_EIGHT(17,30,8),//围8
    ROUND_TEN(18,30,10);//围10
    private int id;
    private float rate;
    private int count;

    private RoundDiceType(int id, int rate, int count) {
        this.id = id;
        this.rate = rate;
        this.count = count;
    }

    private static final Map<Integer,RoundDiceType> roundMap;
    static {
        roundMap = new HashMap<>(DiceCountType.values().length);
        for(RoundDiceType t:RoundDiceType.values()){
            roundMap.put(t.count,t);
        }
    }
    public static RoundDiceType get(int count){
        return roundMap.get(count);
    }
    public int id(){
        return id;
    }
    public float rate(){
        return rate;
    }
    public int count(){
        return count;
    }
}
