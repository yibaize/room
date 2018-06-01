package org.bql.rooms.always_happy.cmd;

import org.bql.error.AppErrorCode;
import org.bql.error.GenaryAppError;
import org.bql.net.server.manage.OperateCommandAbstract;
import org.bql.player.PlayerInfoDto;
import org.bql.player.PlayerRoom;
import org.bql.rooms.RoomFactory;
import org.bql.rooms.always_happy.model.AHRoom;
import org.bql.rooms.three_cards.three_cards_1.dto.SettleModelDto;
import org.bql.rooms.type.RoomStateType;
import org.bql.utils.builder_clazz.ann.Protocol;

/**
 * @作者： big
 * @创建时间： 2018/5/24
 * @文件描述：下注
 */
@Protocol("1017")
public class AHBet extends OperateCommandAbstract {
    /**下注位置*/
    private final int betPosition;
    /**几注*/
    private final int betCount;

    public AHBet(int betPosition, int betCount) {
        this.betPosition = betPosition;
        this.betCount = betCount;
    }

    @Override
    public Object execute() {
        PlayerRoom player = (PlayerRoom) getSession().getAttachment();
        PlayerInfoDto infoDto = player.getPlayer();
        long allNum = betCount * 20000;
        if(player.canBet() < allNum){
            new GenaryAppError(AppErrorCode.IN_TO_ROOM_BET_NIMIETY);
        }
        if(!infoDto.reduceGold(allNum)){
            new GenaryAppError(AppErrorCode.GOLD_NOT);
        }
        player.addBet(allNum);
        AHRoom room = RoomFactory.getInstance().getAhRoom();
        if(room.getRoomState() != RoomStateType.START){
            new GenaryAppError(AppErrorCode.BET_TIME_OUT);
        }
        room.getGamblingParty().bet(betPosition,allNum, player);
        return new SettleModelDto(infoDto.getAccount(),infoDto.getGold(),room.getGamblingParty().getAllMoney());
    }
}
