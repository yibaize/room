package org.zgl.rooms.three_cards.three_cards_1.cmd;

import org.zgl.error.AppErrorCode;
import org.zgl.error.GenaryAppError;
import org.zgl.net.builder_clazz.NotifyCode;
import org.zgl.net.server.manage.OperateCommandAbstract;
import org.zgl.player.PlayerRoom;
import org.zgl.rooms.three_cards.three_cards_1.dto.RoomPlayerAccountDto;
import org.zgl.rooms.three_cards.three_cards_1.manage.FirstPlayerRoom;
import org.zgl.rooms.three_cards.three_cards_1.manage.FirstRooms;
import org.zgl.rooms.type.RoomStateType;
import org.zgl.utils.builder_clazz.ann.Protocol;

/**
 * 准备
 */
@Protocol("1000")
public class FirstRoom_Ready extends OperateCommandAbstract {
    private String account;
    private FirstRooms room;
    @Override
    public Object execute() {
        PlayerRoom player = (PlayerRoom) getSession().getAttachment();
        if(player.getPlayer().getGold() < 500)
            new GenaryAppError(AppErrorCode.GOLD_NOT);//金币不足
        this.account = player.getPlayer().getAccount();
        this.room = (FirstRooms) player.getRoom();
        if(room.getRoomState() != RoomStateType.READY)
            new GenaryAppError(AppErrorCode.ROOM_IS_START);//房间已经开局
        room.getPlayerSet().addPlayerChoice(player.getPlayer().getAccount(),true);
        room.getPlayerSet().getNowPlay().putIfAbsent(account, (FirstPlayerRoom) player);
        room.setRoomState(RoomStateType.READY);
        room.getGamblingParty().setStartTime();
        return null;
    }

    @Override
    public void broadcast() {
        //通知所有房间玩家这个玩家准备了
        room.broadcast(room.getPlayerSet().getNotAccountPlayer(account),NotifyCode.ROOM_HAS_PLAYER_READY,new RoomPlayerAccountDto(account));
        //牌局开始
    }
}
