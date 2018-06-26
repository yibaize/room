package org.zgl.rooms.thousands_of.cmd;

import org.zgl.error.AppErrorCode;
import org.zgl.error.GenaryAppError;
import org.zgl.net.builder_clazz.NotifyCode;
import org.zgl.net.server.manage.OperateCommandAbstract;
import org.zgl.rooms.thousands_of.model.TOPlayer;
import org.zgl.rooms.thousands_of.model.TORoom;
import org.zgl.rooms.three_cards.three_cards_1.dto.RoomPlayerAccountDto;
import org.zgl.utils.builder_clazz.ann.Protocol;

/**
 * @作者： big
 * @创建时间： 2018/6/11
 * @文件描述：下位置
 */
@Protocol("1029")
public class TODownPositionOperation extends OperateCommandAbstract {
    private TORoom room;
    private String account;
    @Override
    public Object execute() {
        TOPlayer toPlayer = (TOPlayer) getSession().getAttachment();
        if(toPlayer.getPosition() == TOPlayer.DEFAULT_POS)
            new GenaryAppError(AppErrorCode.PLAYER_NOT_POSITION);
        account = toPlayer.getPlayer().getAccount();
        room = (TORoom) toPlayer.getRoom();
        room.getPlayerSet().downPos(toPlayer);
        return null;
    }

    @Override
    public void broadcast() {
        room.broadcast(room.getPlayerSet().allPlayerNotId(account), NotifyCode.DOWN_POSITION, new RoomPlayerAccountDto(account)); //有人离开房间
    }
}
