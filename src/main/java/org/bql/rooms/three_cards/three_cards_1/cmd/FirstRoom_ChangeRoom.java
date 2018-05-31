package org.bql.rooms.three_cards.three_cards_1.cmd;

import org.bql.net.builder_clazz.NotifyCode;
import org.bql.net.http.HttpClient;
import org.bql.net.server.manage.OperateCommandAbstract;
import org.bql.player.PlayerFactory;
import org.bql.player.PlayerInfoDto;
import org.bql.player.PlayerRoom;
import org.bql.rooms.RoomAbs;
import org.bql.rooms.great_pretenders.manager.GP_1RoomManager;
import org.bql.rooms.great_pretenders.manager.GP_2RoomManager;
import org.bql.rooms.great_pretenders.manager.GP_3RoomManager;
import org.bql.rooms.three_cards.three_cards_1.dto.IntoRoomDto;
import org.bql.rooms.three_cards.three_cards_1.manage.FRoomManager;
import org.bql.rooms.three_cards.three_cards_1.manage.FirstPlayerRoom;
import org.bql.rooms.three_cards.three_cards_2.TRoomManager;
import org.bql.rooms.three_cards.three_cards_3.THRRoomManager;
import org.bql.utils.builder_clazz.ann.Protocol;

/**
 * 交换房间
 */
@Protocol("1006")
public class FirstRoom_ChangeRoom extends OperateCommandAbstract {
    @Override
    public Object execute() {
        PlayerRoom playerRoom = (PlayerRoom) getSession().getAttachment();
        PlayerInfoDto player = playerRoom.getPlayer();
        RoomAbs room = playerRoom.getRoom();
        room.exitRoom(playerRoom);
        //TODO... 这边以后要判断是初中高，千王。。。等
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
        IntoRoomDto intoRoomDto = HttpClient.getInstance().syncPost(NotifyCode.EXCHANGE_ROOM,
                player.getAccount()+","+PlayerFactory.SYSTEM_PLAYER_ID,IntoRoomDto.class);
        player.setRoomId(intoRoomDto.getRoomId());
        //TODO... 这边以后要判断是初中高，千王。。。等
        room = FRoomManager.getInstance().changeRoom((FirstPlayerRoom) playerRoom);
        playerRoom.setRoom(room);
        return room.roomInfo(player.getAccount());
    }
}
