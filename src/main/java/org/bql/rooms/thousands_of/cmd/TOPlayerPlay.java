package org.bql.rooms.thousands_of.cmd;

import org.bql.net.server.manage.OperateCommandAbstract;
import org.bql.player.PlayerRoom;
import org.bql.player.PlayerRoomBaseInfoDto;
import org.bql.rooms.RoomPlayerBaseDto;
import org.bql.rooms.RoomPlayerBaseDtos;
import org.bql.rooms.thousands_of.dto.TORoomInfoDto;
import org.bql.rooms.thousands_of.model.TOPlayer;
import org.bql.rooms.thousands_of.model.TOPlayerSet;
import org.bql.rooms.thousands_of.model.TORoom;
import org.bql.utils.builder_clazz.ann.Protocol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @作者： big
 * @创建时间： 2018/5/21
 * @文件描述：万人场里的人
 */
@Protocol("1013")
public class TOPlayerPlay extends OperateCommandAbstract {
    @Override
    public Object execute() {
        PlayerRoom player = (PlayerRoom) getSession().getAttachment();
        TORoom room = (TORoom) player.getRoom();
        TOPlayerSet playerSet = room.getPlayerSet();
        List<TOPlayer> toPlayerList = playerSet.getAllPlayer();
        List<RoomPlayerBaseDto> baseInfoDtos = new ArrayList<>(toPlayerList.size());
        for(TOPlayer tp:toPlayerList){
            if(tp.getPosition() == TOPlayer.DEFAULT_POS){
                baseInfoDtos.add(new RoomPlayerBaseDto().baseDto(tp.getPlayer()));
            }
        }
        Collections.sort(baseInfoDtos);
        return new RoomPlayerBaseDtos(baseInfoDtos);
    }
}
