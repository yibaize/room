package org.bql.rooms;
import org.bql.rooms.always_happy.model.AHRoom;
import org.bql.rooms.great_pretenders.manager.GP_1RoomManager;
import org.bql.rooms.great_pretenders.manager.GP_2RoomManager;
import org.bql.rooms.great_pretenders.manager.GP_3RoomManager;
import org.bql.rooms.thousands_of.model.TOPlayer;
import org.bql.rooms.thousands_of.model.TORoom;
import org.bql.rooms.three_cards.three_cards_1.manage.FRoomManager;
import org.bql.rooms.three_cards.three_cards_1.manage.FirstPlayerRoom;
import org.bql.rooms.three_cards.three_cards_1.manage.FirstRooms;
import org.bql.rooms.three_cards.three_cards_2.TRoomManager;
import org.bql.rooms.three_cards.three_cards_3.THRRoomManager;
import org.bql.utils.executer.pool.NioSelectorRunnablePool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class RoomFactory {
    private static RoomFactory instance;
    private final TORoom ROOMS;
    private final FRoomManager F_ROOM;
    private final TRoomManager T_ROOM;
    private final THRRoomManager THR_ROOM;
    private final GP_2RoomManager gp_2RoomManager;
    private final GP_1RoomManager gp_1RoomManager;
    private final GP_3RoomManager gp_3RoomManager;
    private final AHRoom ahRoom;
    private RoomFactory() {
        ROOMS =  new TORoom();
        F_ROOM = FRoomManager.getInstance();
        T_ROOM = TRoomManager.getInstance();
        THR_ROOM = THRRoomManager.getInstance();
        gp_1RoomManager = GP_1RoomManager.getInstance();
        gp_2RoomManager = GP_2RoomManager.getInstance();
        gp_3RoomManager = GP_3RoomManager.getInstance();
        ahRoom = new AHRoom();
    }

    public static RoomFactory getInstance() {
        if(instance == null){
            instance = new RoomFactory();
        }
        return instance;
    }
    public TORoom toRoom(TOPlayer player){
        ROOMS.enterRoom(player);
       return ROOMS;
    }
    public FirstRooms fRoom(FirstPlayerRoom p){
        return (FirstRooms) F_ROOM.intoRoom(p);
    }
    public FirstRooms tRoom(FirstPlayerRoom p){
        return (FirstRooms) T_ROOM.intoRoom(p);
    }
    public FirstRooms thrRoom(FirstPlayerRoom p){
        return (FirstRooms) THR_ROOM.intoRoom(p);
    }

    public FirstRooms gp1Room(FirstPlayerRoom p){
        return (FirstRooms) gp_1RoomManager.intoRoom(p);
    }
    public FirstRooms gp2Room(FirstPlayerRoom p){
        return (FirstRooms) gp_2RoomManager.intoRoom(p);
    }
    public FirstRooms gp3Room(FirstPlayerRoom p){
        return (FirstRooms) gp_3RoomManager.intoRoom(p);
    }
    public AHRoom getAhRoom() {
        return ahRoom;
    }

    public List<RoomAbs> allRoom(){
        List<RoomAbs> allRoom = new ArrayList<>();
        allRoom.addAll(F_ROOM.getAllRoom());
        allRoom.addAll(T_ROOM.getAllRoom());
        allRoom.addAll(THR_ROOM.getAllRoom());
        allRoom.addAll(gp_1RoomManager.getAllRoom());
        allRoom.addAll(gp_2RoomManager.getAllRoom());
        allRoom.addAll(gp_3RoomManager.getAllRoom());
        allRoom.add(ROOMS);
        allRoom.add(ahRoom);
        return allRoom;
    }
}
