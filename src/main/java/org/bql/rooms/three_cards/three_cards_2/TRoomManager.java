package org.bql.rooms.three_cards.three_cards_2;

import org.bql.error.AppErrorCode;
import org.bql.error.GenaryAppError;
import org.bql.rooms.RoomAbs;
import org.bql.rooms.three_cards.three_cards_1.manage.FirstPlayerRoom;
import org.bql.rooms.three_cards.three_cards_1.manage.FirstRooms;
import org.bql.rooms.three_cards.three_cards_1.manage.MyPlayerSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TRoomManager {
    private final Object lock = new Object();
    private Map<Integer,RoomAbs> roomsMap = new HashMap<>();
    private static TRoomManager instance;

    public static TRoomManager getInstance() {
        if(instance == null)
            instance = new TRoomManager();
        return instance;
    }

    private FirstRooms createRoom(int roomId, FirstPlayerRoom player){
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
    public  RoomAbs changeRoom(FirstPlayerRoom playerRoom){
        RoomAbs r = roomsMap.getOrDefault(playerRoom.getRoom().getRoomId(),null);
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
    public List<RoomAbs> getAllRoom(){
        return new ArrayList<>(roomsMap.values());
    }
}
