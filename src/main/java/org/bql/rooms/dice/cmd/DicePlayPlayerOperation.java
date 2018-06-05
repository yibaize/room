package org.bql.rooms.dice.cmd;

import org.bql.net.server.manage.OperateCommandAbstract;
import org.bql.rooms.RoomPlayerBaseDto;
import org.bql.rooms.RoomPlayerBaseDtos;
import org.bql.rooms.dice.model.DicePlayer;
import org.bql.rooms.dice.model.DicePlayerSet;
import org.bql.rooms.dice.model.DiceRoom;
import org.bql.rooms.thousands_of.model.TOPlayer;
import org.bql.utils.builder_clazz.ann.Protocol;

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
            if(tp.getRoomPosition() != TOPlayer.DEFAULT_POS){
                baseInfoDtos.add(new RoomPlayerBaseDto().baseDto(tp.getPlayer()));
            }
        }
        Collections.sort(baseInfoDtos);
        return new RoomPlayerBaseDtos(baseInfoDtos);
    }
}
