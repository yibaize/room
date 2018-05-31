package org.bql.hall_connection;

import org.bql.hall_connection.dto.RoomWeathDto;
import org.bql.hall_connection.dto.RoomWeathDtos;
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
import org.bql.rooms.three_cards.three_cards_1.dto.PlayerRoomDto;
import org.bql.rooms.three_cards.three_cards_1.manage.FRoomManager;
import org.bql.rooms.three_cards.three_cards_2.TRoomManager;
import org.bql.rooms.three_cards.three_cards_3.THRRoomManager;
import org.bql.utils.JsonUtils;
import org.bql.utils.builder_clazz.ann.Protocol;

import java.util.ArrayList;
import java.util.List;

/**
 * 返回大厅
 */
@Protocol("10002")
public class BackHall extends OperateCommandAbstract {
    @Override
    public PlayerRoomDto execute() {
        PlayerRoom playerRoom = (PlayerRoom) getSession().getAttachment();

        //先通知大厅财富更新先
        PlayerInfoDto playerInfoDto = playerRoom.getPlayer();
        List<RoomWeathDto> weathDtos = new ArrayList<>(1);
        weathDtos.add(new RoomWeathDto(playerInfoDto.getAccount(),playerInfoDto.getGold(),playerInfoDto.getDiamond(),playerInfoDto.getIntegral(),false,-1));
        RoomWeathDtos notifyMessage = new RoomWeathDtos(weathDtos);
        //通知大厅财富更新到数据库
        HttpClient.getInstance().asyncPost(NotifyCode.REQUEST_HALL_UPDATE_WEATH,JsonUtils.jsonSerialize(notifyMessage)+","+PlayerFactory.SYSTEM_PLAYER_ID);
        RoomAbs room = playerRoom.getRoom();
        if(room == null)
            return null;
        room.exitRoom(playerRoom);
        if(room.getPlayerNum() <= 0){
            switch (playerInfoDto.getScenesId()){
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
        playerRoom.setRoom(null);
        return null;
    }
}

