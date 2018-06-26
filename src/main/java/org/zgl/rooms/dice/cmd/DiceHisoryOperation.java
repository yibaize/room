package org.zgl.rooms.dice.cmd;

import org.zgl.net.server.manage.OperateCommandAbstract;
import org.zgl.rooms.dice.dto.DiceHistoryDto;
import org.zgl.rooms.dice.model.DicePlayer;
import org.zgl.rooms.dice.model.DiceRoom;
import org.zgl.utils.builder_clazz.ann.Protocol;

/**
 * @作者： big
 * @创建时间： 2018/6/5
 * @文件描述：历史记录
 */
@Protocol("1026")
public class DiceHisoryOperation extends OperateCommandAbstract {
    @Override
    public Object execute() {
        DicePlayer player = (DicePlayer) getSession().getAttachment();
        DiceRoom room = (DiceRoom) player.getRoom();
        return new DiceHistoryDto(room.getGamblingParty().getHistory());
    }
}
