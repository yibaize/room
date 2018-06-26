package org.zgl.rooms.three_cards.three_cards_1.cmd;

import org.zgl.error.AppErrorCode;
import org.zgl.error.GenaryAppError;
import org.zgl.net.builder_clazz.NotifyCode;
import org.zgl.net.message.ServerResponse;
import org.zgl.net.server.manage.OperateCommandAbstract;
import org.zgl.rooms.three_cards.three_cards_1.dto.RoomPlayerAccountDto;
import org.zgl.rooms.three_cards.three_cards_1.manage.FirstGamblingParty;
import org.zgl.rooms.three_cards.three_cards_1.manage.FirstPlayerRoom;
import org.zgl.rooms.three_cards.three_cards_1.manage.FirstRooms;
import org.zgl.rooms.three_cards.three_cards_1.manage.MyPlayerSet;
import org.zgl.utils.builder_clazz.ann.Protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 玩家主动放弃牌
 */
@Protocol("1010")
public class FirstRoom_GiveUpCard extends OperateCommandAbstract {
    private FirstRooms room;
    private String account;
    private MyPlayerSet playerSet;
    @Override
    public Object execute() {
        FirstPlayerRoom player = (FirstPlayerRoom) getSession().getAttachment();
        account = player.getPlayer().getAccount();
        room = (FirstRooms) player.getRoom();
        playerSet = room.getPlayerSet();
        playerSet.losePlayer(account);
        player.getHandCard().setCompareResult(false);
        FirstGamblingParty gamblingParty = room.getGamblingParty();
        if(playerSet.playNum() <= 1){
            List<FirstPlayerRoom> list = new ArrayList<>(playerSet.getNowPlay().values());
            if(list.size() <= 0)
                new GenaryAppError(AppErrorCode.NOT_GIVE_UP);
            FirstPlayerRoom winPlayer = list.get(0);
            gamblingParty.setWinPlayer(winPlayer);
            gamblingParty.setWinPosition(winPlayer.getPlayer().getRoomPosition());//设置这把赢家的位置
            gamblingParty.setRoomEnd();
            gamblingParty.setEndTime();
        }else {
            if(room.getGamblingParty().getNowBottomPos().get() == player.getPlayer().getRoomPosition()) {//不是该玩家操作
                //下一个位置的玩家
                String nextAccount = playerSet.getNextPositionAccount(player.getPlayer().getRoomPosition());
                FirstPlayerRoom nextPlayer = room.getPlayerSet().getPlayerForPosition(nextAccount);
                nextPlayer.getSession().write(new ServerResponse(NotifyCode.ROOM_PLAYER_BOTTOM, null));
                gamblingParty.setNowBottomPos(new AtomicInteger(nextPlayer.getPlayer().getRoomPosition()));
                //通知下一个位置玩家做动作
                gamblingParty.setStartTime(System.currentTimeMillis());
            }
        }
        return null;
    }

    @Override
    public void broadcast() {
        room.broadcast(playerSet.getNotAccountPlayer(account),NotifyCode.ROOM_PLAYER_GIVE_UP_CARD,new RoomPlayerAccountDto(account));
    }
}
