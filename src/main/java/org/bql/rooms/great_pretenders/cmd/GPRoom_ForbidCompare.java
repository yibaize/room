package org.bql.rooms.great_pretenders.cmd;

import org.bql.error.AppErrorCode;
import org.bql.error.GenaryAppError;
import org.bql.net.server.manage.OperateCommandAbstract;
import org.bql.player.PlayerInfoDto;
import org.bql.player.ResourceModel;
import org.bql.rooms.three_cards.three_cards_1.manage.FirstGamblingParty;
import org.bql.rooms.three_cards.three_cards_1.manage.FirstPlayerRoom;
import org.bql.rooms.three_cards.three_cards_1.manage.FirstRooms;
import org.bql.rooms.three_cards.three_cards_1.manage.MyPlayerSet;
import org.bql.utils.builder_clazz.ann.Protocol;

import java.util.List;

/**
 * @作者： big
 * @创建时间： 2018/5/22
 * @文件描述：禁比卡
 */
@Protocol("1023")
public class GPRoom_ForbidCompare extends OperateCommandAbstract {
    private final String targetAccount;

    public GPRoom_ForbidCompare(String targetAccount) {
        this.targetAccount = targetAccount;
    }

    @Override
    public Object execute() {
        FirstPlayerRoom player = (FirstPlayerRoom) getSession().getAttachment();
        PlayerInfoDto playerInfoDto = player.getPlayer();
        List<ResourceModel> prop = playerInfoDto.getProps();
        int pos = -1;
        for(int i = 0;i<prop.size();i++){
            if(prop.get(i).getId() == 19)
                pos = i;
        }
        if(pos < 0)
            new GenaryAppError(AppErrorCode.NOT_FORBID_CARD);//禁比卡不足
        FirstRooms room = (FirstRooms) player.getRoom();
        MyPlayerSet playerSet = room.getPlayerSet();
        if(!playerSet.isPlayForAccount(targetAccount))
            new GenaryAppError(AppErrorCode.PLAYER_NOT_PAY);
        FirstGamblingParty gamblingParty = room.getGamblingParty();
        gamblingParty.addForbidCompare(player.getPlayer().getAccount(),targetAccount);
        ResourceModel r = prop.get(pos);
        r.reduce(1);
        return null;
    }
}
