package org.zgl.rooms.three_cards.three_cards_1.manage;

import org.zgl.rooms.RoomAbs;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FRoomManager {
    private  final Object lock = new Object();
    private  Map<Integer,FirstRooms> roomsMap = new ConcurrentHashMap<>();
    private static FRoomManager instance;
    private AtomicInteger roomId;
    public static FRoomManager getInstance() {
        if(instance == null)
            instance = new FRoomManager();
        return instance;
    }

    private FRoomManager() {
        roomId = new AtomicInteger(0);
    }

    private  FirstRooms createRoom(FirstPlayerRoom player){
        int id = roomId.incrementAndGet();
        if(id > 999999999){
            roomId.set(0);
        }
        FirstRooms r = new FirstRooms(player.getPlayer().getScenesId(),id,player.getPlayer().getUid());
        roomsMap.put(id,r);
        return r;
    }
    public  RoomAbs hasRoom(int roomId){
        return roomsMap.getOrDefault(roomId,null);
    }

    public  RoomAbs intoRoom(FirstPlayerRoom p){
        synchronized (lock) {
            FirstRooms r = enterRoom(p);
            if (r == null) {
                r = createRoom(p);
                r.enterRoom(p);
            }
            return r;
        }
    }
    private FirstRooms enterRoom(FirstPlayerRoom p){
        for (Map.Entry<Integer,FirstRooms> e:roomsMap.entrySet()){
            FirstRooms r = e.getValue();
            if(r.getPlayerNum() < MyPlayerSet.MAX_SIZE){
                r.enterRoom(p);
                return r;
            }
        }
        return null;
    }
    /**
     * 交换房间
     * @param playerRoom
     * @return
     */
    public RoomAbs changeRoom(FirstPlayerRoom playerRoom){
        int roomId = playerRoom.getPlayer().getRoomId();
        for(Map.Entry<Integer,FirstRooms> e:roomsMap.entrySet()){
            FirstRooms r = e.getValue();
            if(e.getKey() != roomId && r.getPlayerNum() < MyPlayerSet.MAX_SIZE){
                r.enterRoom(playerRoom);
                return r;
            }
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
