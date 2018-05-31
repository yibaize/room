package org.bql.hall_connection;

import org.bql.error.AppErrorCode;
import org.bql.error.CloseConnectionError;
import org.bql.error.GenaryAppError;
import org.bql.hall_connection.dto.RoomWeathDto;
import org.bql.hall_connection.dto.RoomWeathDtos;
import org.bql.net.builder_clazz.NotifyCode;
import org.bql.net.client.GameClient;
import org.bql.net.http.HttpClient;
import org.bql.net.message.ClientRequest;
import org.bql.net.message.ClientResponse;
import org.bql.net.message.Msg;
import org.bql.net.server.manage.OperateCommandAbstract;
import org.bql.player.PlayerFactory;
import org.bql.player.PlayerInfoDto;
import org.bql.player.PlayerRoom;
import org.bql.rooms.RoomAbs;
import org.bql.rooms.RoomFactory;
import org.bql.rooms.three_cards.three_cards_1.manage.FRoomManager;
import org.bql.rooms.three_cards.three_cards_2.TRoomManager;
import org.bql.rooms.three_cards.three_cards_3.THRRoomManager;
import org.bql.utils.JsonUtils;
import org.bql.utils.ProtostuffUtils;
import org.bql.utils.builder_clazz.ann.Protocol;

import java.util.ArrayList;
import java.util.List;

/**
 * 玩家异常下线
 */
@Protocol("10003")
public class ErrorLigout extends OperateCommandAbstract {
    @Override
    public Object execute() {
        PlayerRoom playerRoom = (PlayerRoom) getSession().getAttachment();
        //先通知大厅财富更新先
        PlayerInfoDto playerInfoDto = playerRoom.getPlayer();
        List<RoomWeathDto> weathDtos = new ArrayList<>(1);
        weathDtos.add(new RoomWeathDto(playerInfoDto.getAccount(),playerInfoDto.getGold(),playerInfoDto.getDiamond(),playerInfoDto.getIntegral(),false,-1));
        RoomWeathDtos notifyMessage = new RoomWeathDtos(weathDtos);

        HttpClient.getInstance().asyncPost(NotifyCode.REQUEST_HALL_UPDATE_WEATH,JsonUtils.jsonSerialize(notifyMessage)+","+PlayerFactory.SYSTEM_PLAYER_ID);
        RoomFactory.getInstance().getAhRoom().exitRoom(playerRoom);
        RoomAbs room = playerRoom.getRoom();
        if(room != null) {
            room.exitRoom(playerRoom);
            if (room.getPlayerNum() <= 0) {
                switch (playerInfoDto.getScenesId()) {
                    case 1:
                        FRoomManager.getInstance().remove(room.getRoomId());
                        break;
                    case 2:
                        TRoomManager.getInstance().remove(room.getRoomId());
                        break;
                    case 3:
                        THRRoomManager.getInstance().remove(room.getRoomId());
                        break;
                }
            }

        }
        HttpClient.getInstance().asyncPost(NotifyCode.ERROR_LOG_OUT, playerInfoDto.getAccount() + "," + PlayerFactory.SYSTEM_PLAYER_ID);
        //进入大厅关闭房间链接
        new CloseConnectionError(AppErrorCode.EXCHANGE_SCENES_SUCCEED);
        return null;
    }
}
