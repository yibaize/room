package org.zgl.rooms.type;

import java.util.HashMap;
import java.util.Map;

/**
 * @作者： big
 * @创建时间： 2018/6/5
 * @文件描述：骰子点数
 */
public enum DiceCountType {
    NONE(0,0,0),
    LITTLE(1,1,1),//小
    TWO(2,30,1),
    THREE(3,13,1),
    FOUR(4,10,1),
    FIVE(5,8,1),
    SIX(6,5,1),
    SEVEN(7,5,13),
    EIGHT(8,5,13),
    NINE(9,8,13),
    TEN(10,10,13),
    ELEVEN(11,16,13),
    TWELVE(12,30,13),
    BIG(13,1,13);//大

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
