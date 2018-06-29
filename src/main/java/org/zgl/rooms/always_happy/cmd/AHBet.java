package org.zgl.rooms.always_happy.cmd;

import org.zgl.error.AppErrorCode;
import org.zgl.error.GenaryAppError;
import org.zgl.net.server.manage.OperateCommandAbstract;
import org.zgl.player.PlayerInfoDto;
import org.zgl.player.PlayerRoom;
import org.zgl.rooms.RoomFactory;
import org.zgl.rooms.always_happy.model.AHRoom;
import org.zgl.rooms.three_cards.three_cards_1.dto.SettleModelDto;
import org.zgl.rooms.type.RoomStateType;
import org.zgl.utils.builder_clazz.ann.Protocol;

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
//        if(player.canBet() < allNum){
//            new GenaryAppError(AppErrorCode.IN_TO_ROOM_BET_NIMIETY);
//        }
        long gold = infoDto.getGold();
        if(!infoDto.reduceGold(allNum)){
            new GenaryAppError(AppErrorCode.GOLD_NOT);
        }
        player.addBet(allNum);
        AHRoom room = RoomFactory.getInstance().getAhRoom();
        if(room.getRoomState() != RoomStateType.START){
            new GenaryAppError(AppErrorCode.BET_TIME_OUT);
        }
        room.getGamblingParty().addBefore(infoDto.getUid(),gold);
        room.getGamblingParty().bet(betPosition,allNum, player,betCount);
        infoDto.addAhRoomBetNum(allNum);
        return new SettleModelDto(infoDto.getAccount(),infoDto.getGold(),room.getGamblingParty().getAllMoney());
    }
}
