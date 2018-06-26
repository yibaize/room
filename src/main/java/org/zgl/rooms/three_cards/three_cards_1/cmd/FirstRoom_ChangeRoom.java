package org.zgl.rooms.three_cards.three_cards_1.cmd;

import org.zgl.error.AppErrorCode;
import org.zgl.error.GenaryAppError;
import org.zgl.net.server.manage.OperateCommandAbstract;
import org.zgl.player.PlayerInfoDto;
import org.zgl.player.PlayerRoom;
import org.zgl.rooms.RoomAbs;
import org.zgl.rooms.great_pretenders.manager.GP_1RoomManager;
import org.zgl.rooms.great_pretenders.manager.GP_2RoomManager;
import org.zgl.rooms.great_pretenders.manager.GP_3RoomManager;
import org.zgl.rooms.three_cards.three_cards_1.manage.FRoomManager;
import org.zgl.rooms.three_cards.three_cards_1.manage.FirstPlayerRoom;
import org.zgl.rooms.three_cards.three_cards_2.TRoomManager;
import org.zgl.rooms.three_cards.three_cards_3.THRRoomManager;
import org.zgl.utils.builder_clazz.ann.Protocol;

/**
 * 交换房间
 */
@Protocol("1006")
public class FirstRoom_ChangeRoom extends OperateCommandAbstract {
    @Override
    public Object execute() {
        FirstPlayerRoom playerRoom = (FirstPlayerRoom) getSession().getAttachment();
        if((System.currentTimeMillis() - playerRoom.getFirstExchangeTime())/1000 <= 5)
            new GenaryAppError(AppErrorCode.EXCHANGE_ROOM_OFTEN);
        PlayerInfoDto player = playerRoom.getPlayer();
        RoomAbs room = playerRoom.getRoom();
        room.exitRoom(playerRoom);
        if(room.getPlayerNum() <= 0){
            switch (player.getScenesId()){
                case 1:
                    FRoomManager.getInstance().remove(room.getRoomId());
                    break;
                case 2:
                    TRoomManager.getInstance().remove(room.getRoomId());
                    break;
                case 3:
                    THRRoomManager.getInstance().remove(room.getRoomId());
                    break;
                case 6:
                    GP_1RoomManager.getInstance().remove(room.getRoomId());
                    break;
                case 7:
                    GP_2RoomManager.getInstance().remove(room.getRoomId());
                    break;
                case 8:
                    GP_3RoomManager.getInstance().remove(room.getRoomId());
                    break;
            }
        }
        switch (player.getScenesId()){
            case 1:
                room = FRoomManager.getInstance().changeRoom(playerRoom);
                break;
            case 2:
                room = TRoomManager.getInstance().changeRoom(playerRoom);
                break;
            case 3:
                room = THRRoomManager.getInstance().changeRoom(playerRoom);
                break;
            case 6:
                room = GP_1RoomManager.getInstance().changeRoom(playerRoom);
                break;
            case 7:
                room = GP_2RoomManager.getInstance().changeRoom(playerRoom);
                break;
            case 8:
                room = GP_3RoomManager.getInstance().changeRoom(playerRoom);
                break;
        }
        playerRoom.setFirstExchangeTime(System.currentTimeMillis());
//        room = FRoomManager.getInstance().changeRoom((FirstPlayerRoom) playerRoom);
        playerRoom.setRoom(room);
        return room.roomInfo(player.getAccount());
    }
}
