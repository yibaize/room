package org.zgl.rooms.dice.cmd;

import org.zgl.net.server.manage.OperateCommandAbstract;
import org.zgl.rooms.RoomPlayerBaseDto;
import org.zgl.rooms.RoomPlayerBaseDtos;
import org.zgl.rooms.dice.model.DicePlayer;
import org.zgl.rooms.dice.model.DicePlayerSet;
import org.zgl.rooms.dice.model.DiceRoom;
import org.zgl.rooms.thousands_of.model.TOPlayer;
import org.zgl.utils.builder_clazz.ann.Protocol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @作者： big
 * @创建时间： 2018/6/5
 * @文件描述：场内所有玩家
 */
@Protocol("1027")
public class DicePlayPlayerOperation extends OperateCommandAbstract {
    @Override
    public Object execute() {
        DicePlayer player = (DicePlayer) getSession().getAttachment();
        DiceRoom room = (DiceRoom) player.getRoom();
        DicePlayerSet playerSet = room.getPlayerSet();
        List<DicePlayer> toPlayerList = playerSet.getAllPlayer();
        List<RoomPlayerBaseDto> baseInfoDtos = new ArrayList<>(toPlayerList.size());
        for(DicePlayer tp:toPlayerList){
            if(tp.getRoomPosition() == DicePlayer.DEFAULT_POSITION){
                baseInfoDtos.add(new RoomPlayerBaseDto().baseDto(tp.getPlayer()));
            }
        }
        Collections.sort(baseInfoDtos);
        return new RoomPlayerBaseDtos(baseInfoDtos);
    }
}
