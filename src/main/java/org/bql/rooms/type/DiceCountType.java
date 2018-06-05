package org.bql.rooms.type;

import java.util.HashMap;
import java.util.Map;

/**
 * @作者： big
 * @创建时间： 2018/6/5
 * @文件描述：骰子点数
 */
public enum DiceCountType {
    TWO(2,1,13),
    THREE(3,1,13),
    FOUR(4,1,13),
    FIVE(5,1,13),
    SIX(6,1,13),
    SEVEN(7,1,14),
    EIGHT(8,1,14),
    NINE(9,1,14),
    TEN(10,1,14),
    ELEVEN(11,1,14),
    TWELVE(12,1,14);//小
    //点数
    private int count;
    //倍率
    private float rate;
    //大小
    private int size;

    DiceCountType(int count, int rate, int size) {
        this.count = count;
        this.rate = rate;
        this.size = size;
    }
    private static final Map<Integer,DiceCountType> map;
    static {
        map = new HashMap<>(DiceCountType.values().length);
        for(DiceCountType t:DiceCountType.values()){
            map.putIfAbsent(t.count,t);
        }
    }
    public static DiceCountType getDiceType(int count){
        return map.get(count);
    }
    public int getCount() {
        return count;
    }

    public float getRate() {
        return rate;
    }

    public int getSize() {
        return size;
    }
}
