package org.bql.rooms.three_cards.three_cards_1.manage;

import org.bql.error.AppErrorCode;
import org.bql.error.GenaryAppError;
import org.bql.rooms.RoomAbs;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FRoomManager {
    private  final Object lock = new Object();
    private  Map<Integer,FirstRooms> roomsMap = new ConcurrentHashMap<>();
    private static FRoomManager instance;

    public static FRoomManager getInstance() {
        if(instance == null)
            instance = new FRoomManager();
        return instance;
    }

    private  FirstRooms createRoom(int roomId, FirstPlayerRoom player){
        FirstRooms r = new FirstRooms(player.getPlayer().getScenesId(),roomId,player.getPlayer().getAccount());
        roomsMap.put(roomId,r);
        return r;
    }
    public  RoomAbs hasRoom(int roomId){
        return roomsMap.getOrDefault(roomId,null);
    }

    public  RoomAbs intoRoom(FirstPlayerRoom p){
        synchronized (lock) {
            int roomId = p.getPlayer().getRoomId();
            FirstRooms r = (FirstRooms) roomsMap.getOrDefault(roomId, null);
            if (r != null) {
                r.enterRoom(p);
            } else {
                r = createRoom(roomId, p);
                intoRoom(p);
            }
            return r;
        }
    }

    /**
     * 交换房间
     * @param playerRoom
     * @return
     */
    public RoomAbs changeRoom(FirstPlayerRoom playerRoom){
        int roomId = playerRoom.getPlayer().getRoomId();
        RoomAbs r = roomsMap.getOrDefault(roomId,null);
        if(r != null){
            if(r.getPlayerNum() >= MyPlayerSet.MAX_SIZE ){
                new GenaryAppError(AppErrorCode.SERVER_ERR);//房间人数已经满了说明有线程安全问题
            }
            r.enterRoom(playerRoom);
            return r;
        }
        return intoRoom(playerRoom);
    }
    public void remove(int roomId){
        roomsMap.remove(roomId);
    }
    public List<FirstRooms> getAllRoom(){
        return new ArrayList<>(roomsMap.values());
    }
}
