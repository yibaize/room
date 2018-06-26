package org.zgl.rooms.thousands_of.cmd;

import org.zgl.net.server.manage.OperateCommandAbstract;
import org.zgl.player.PlayerRoom;
import org.zgl.rooms.thousands_of.dto.JackpotDto;
import org.zgl.rooms.thousands_of.dto.JackpotDtoList;
import org.zgl.rooms.thousands_of.model.TORoom;
import org.zgl.utils.builder_clazz.ann.Protocol;

import java.util.List;

/**
 * @作者： big
 * @创建时间： 2018/6/21
 * @文件描述：
 */
@Protocol("1031")
public class TORoomJackpotOperation extends OperateCommandAbstract {
    @Override
    public Object execute() {
        PlayerRoom player = (PlayerRoom) getSession().getAttachment();
        TORoom room = (TORoom) player.getRoom();
        List<JackpotDto> jackpotDtos = room.getGamblingParty().getJackpotDtos();
        return new JackpotDtoList(jackpotDtos);
    }
}
