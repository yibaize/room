package org.bql.rooms.type;

import org.bql.player.PlayerFactory;
import org.bql.player.PlayerRoom;
import org.bql.rooms.RoomAbs;
import org.bql.rooms.RoomFactory;
import org.bql.rooms.thousands_of.model.TOPlayer;
import org.bql.rooms.three_cards.three_cards_1.manage.FirstPlayerRoom;
import java.util.HashMap;
import java.util.Map;

public enum ScenesType {
    HALL(0,1){
        @Override
        public RoomAbs enterRoom(PlayerRoom player) {

            return null;
        }

        @Override
        public PlayerRoom newPlayerForInit() {
            return PlayerFactory.getInstance().playerRoom();
        }

        @Override
        public int[] chip() {
            return new int[0];
        }
    },
    ROOM_FIRST(1,2) {
        @Override
        public RoomAbs enterRoom(PlayerRoom player) {
            return RoomFactory.getInstance().fRoom((FirstPlayerRoom) player);
        }

        @Override
        public PlayerRoom newPlayerForInit() {
            return PlayerFactory.getInstance().fPlayer();
        }

        @Override
        public int[] chip() {
            return Chip.FIRST_BOTTOM_NUM;
        }
    },
    ROOM_TWO(2,2) {
        @Override
        public RoomAbs enterRoom(PlayerRoom player) {
            return RoomFactory.getInstance().tRoom((FirstPlayerRoom) player);
        }

        @Override
        public PlayerRoom newPlayerForInit() {
            return PlayerFactory.getInstance().fPlayer();
        }

        @Override
        public int[] chip() {
            return Chip.TWO_BOTTOM_NUM;
        }
    },
    ROOM_LAST(3,2) {
        @Override
        public RoomAbs enterRoom(PlayerRoom player) {
            return RoomFactory.getInstance().thrRoom((FirstPlayerRoom) player);
        }

        @Override
        public PlayerRoom newPlayerForInit() {
            return PlayerFactory.getInstance().fPlayer();
        }

        @Override
        public int[] chip() {
            return Chip.THREE_BOTTOM_NUM;
        }
    },
    /**筛子场*/
    DICE(4,1) {
        @Override
        public RoomAbs enterRoom(PlayerRoom player) {
            return null;
        }

        @Override
        public PlayerRoom newPlayerForInit() {
            return PlayerFactory.getInstance().dicePlayer();
        }

        @Override
        public int[] chip() {
            return new int[0];
        }
    },
    /**万人场*/
    TO_ROOM(5,1) {
        @Override
        public RoomAbs enterRoom(PlayerRoom player) {
            return RoomFactory.getInstance().toRoom((TOPlayer) player);
        }

        @Override
        public PlayerRoom newPlayerForInit() {
            return PlayerFactory.getInstance().toPlayer();
        }

        @Override
        public int[] chip() {
            return Chip.TO_ROOM;
        }
    },
    /**千王场*/
    GP_ROOM_1(6,2) {
        @Override
        public RoomAbs enterRoom(PlayerRoom player) {
            return RoomFactory.getInstance().gp1Room((FirstPlayerRoom) player);
        }

        @Override
        public PlayerRoom newPlayerForInit() {
            return PlayerFactory.getInstance().fPlayer();
        }

        @Override
        public int[] chip() {
            return Chip.GP_1_BOTTOM_NUM;
        }
    },
    GP_ROOM_2(7,2) {
        @Override
        public RoomAbs enterRoom(PlayerRoom player) {
            return RoomFactory.getInstance().gp2Room((FirstPlayerRoom) player);
        }

        @Override
        public PlayerRoom newPlayerForInit() {
            return PlayerFactory.getInstance().fPlayer();
        }

        @Override
        public int[] chip() {
            return Chip.GP_2_BOTTOM_NUM;
        }
    },
    GP_ROOM_3(8,2) {
        @Override
        public RoomAbs enterRoom(PlayerRoom player) {
            return RoomFactory.getInstance().gp3Room((FirstPlayerRoom) player);
        }

        @Override
        public PlayerRoom newPlayerForInit() {
            return PlayerFactory.getInstance().fPlayer();
        }

        @Override
        public int[] chip() {
            return Chip.GP_3_BOTTOM_NUM;
        }
    },
    /**时时乐*/
    AH_ROOM(9,1) {
        @Override
        public RoomAbs enterRoom(PlayerRoom player) {
            return RoomFactory.getInstance().getAhRoom();
        }

        @Override
        public PlayerRoom newPlayerForInit() {
            return new PlayerRoom();
        }

        @Override
        public int[] chip() {
            return new int[0];
        }
    };
    private int id;
    private int type;

    private ScenesType(int id, int type) {
        this.id = id;
        this.type = type;
    }

    public int id(){
        return id;
    }
    public int type(){
        return type;
    }
    private static final Map<Integer,ScenesType> map;
    static {
        map = new HashMap<>(ScenesType.values().length);
        for(ScenesType s : ScenesType.values()){
            map.putIfAbsent(s.id,s);
        }
    }
    public static ScenesType get(int id){
        if(map.containsKey(id))
            return map.get(id);
        return null;
    }

    /**
     * 进入房间
     * @param player
     * @return
     */
    public abstract RoomAbs enterRoom(PlayerRoom player);

    /**
     * 初始化房间的玩家
     * @return
     */
    public abstract PlayerRoom newPlayerForInit();

    /**
     * 去哦去筹码
     * @return
     */
    public abstract int[] chip();
}
