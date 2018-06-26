package org.zgl.rooms.type;

import java.util.HashMap;
import java.util.Map;

public enum RoomStateType {
    READY(1),//准备中
    START(2),//开始
    DEAL(3),//发牌
    STOP_BOTTOM(4),//停止下注
    SETTLE(5),
    END(6);//结算
    private int id;

    private RoomStateType(int id) {
        this.id = id;
    }
    public int id(){
        return id;
    }
    private static final Map<Integer,RoomStateType> map;
    static {
        map = new HashMap<>(RoomStateType.values().length);
        for(RoomStateType t:RoomStateType.values()){
            map.putIfAbsent(t.id,t);
        }
    }
    public static RoomStateType type(int id){
        return map.get(id);
    }
}
