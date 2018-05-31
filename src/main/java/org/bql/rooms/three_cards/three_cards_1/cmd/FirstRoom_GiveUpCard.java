package org.bql.rooms.three_cards.three_cards_1.cmd;

import org.bql.net.builder_clazz.NotifyCode;
import org.bql.net.server.manage.OperateCommandAbstract;
import org.bql.rooms.three_cards.three_cards_1.dto.RoomPlayerAccountDto;
import org.bql.rooms.three_cards.three_cards_1.manage.FirstPlayerRoom;
import org.bql.rooms.three_cards.three_cards_1.manage.FirstRooms;
import org.bql.rooms.three_cards.three_cards_1.manage.MyPlayerSet;
import org.bql.utils.builder_clazz.ann.Protocol;

/**
 * 玩家主动放弃牌
 */
@Protocol("1010")
public class FirstRoom_GiveUpCard extends OperateCommandAbstract {
    private FirstRooms room;
    private String account;
    private MyPlayerSet playerSet;
    private boolean isEnd = false;
    @Override
    public Object execute() {
        FirstPlayerRoom player = (FirstPlayerRoom) getSession().getAttachment();
        account = player.getPlayer().getAccount();
        room = (FirstRooms) player.getRoom();
        playerSet = room.getPlayerSet();
        playerSet.losePlayer(account);
        player.getHandCard().setCompareResult(false);
        if(playerSet.playNum() <= 1){
            room.end();
            isEnd = true;
        }
        return null;
    }

    @Override
    public void broadcast() {
        if(!isEnd)
            return;
        room.broadcast(playerSet.getNotAccountPlayer(account),NotifyCode.ROOM_PLAYER_GIVE_UP_CARD,new RoomPlayerAccountDto(account));
    }
}
