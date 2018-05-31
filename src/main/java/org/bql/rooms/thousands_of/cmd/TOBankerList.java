package org.bql.rooms.thousands_of.cmd;

import org.bql.net.server.manage.OperateCommandAbstract;
import org.bql.player.PlayerInfoDto;
import org.bql.player.PlayerRoomBaseInfoDto;
import org.bql.rooms.thousands_of.dto.TOBankerListDto;
import org.bql.rooms.thousands_of.dto.TORoomBankerListDto;
import org.bql.rooms.thousands_of.dto.TORoomInfoDto;
import org.bql.rooms.thousands_of.model.TOPlayer;
import org.bql.rooms.thousands_of.model.TORoom;
import org.bql.utils.builder_clazz.ann.Protocol;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者： big
 * @创建时间： 2018/5/23
 * @文件描述：上庄列表
 */
@Protocol("1015")
public class TOBankerList extends OperateCommandAbstract {
    @Override
    public Object execute() {
        TOPlayer player = (TOPlayer) getSession().getAttachment();
        TORoom room = (TORoom) player.getRoom();
        List<TOPlayer> toPlayers = room.getPlayerSet().getBankerUpList();
        List<TOBankerListDto> baseInfoDtos = new ArrayList<>(toPlayers.size());
        for(TOPlayer t : toPlayers){
            PlayerInfoDto infoDto = t.getPlayer();
            baseInfoDtos.add(new TOBankerListDto(infoDto.getUsername(),infoDto.getGold()));
        }
        return new TORoomBankerListDto(baseInfoDtos);
    }
}
