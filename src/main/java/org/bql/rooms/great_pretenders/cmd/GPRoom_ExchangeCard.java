package org.bql.rooms.great_pretenders.cmd;

import org.bql.error.AppErrorCode;
import org.bql.error.GenaryAppError;
import org.bql.net.builder_clazz.NotifyCode;
import org.bql.net.server.manage.OperateCommandAbstract;
import org.bql.player.PlayerInfoDto;
import org.bql.player.ResourceModel;
import org.bql.rooms.RoomAbs;
import org.bql.rooms.card.CardDataTable;
import org.bql.rooms.great_pretenders.dto.GPRoomExchangeCardDto;
import org.bql.rooms.thousands_of.dto.PositionDto;
import org.bql.rooms.three_cards.three_cards_1.dto.RoomPlayerAccountDto;
import org.bql.rooms.three_cards.three_cards_1.manage.FirstPlayerRoom;
import org.bql.rooms.three_cards.three_cards_1.manage.FirstRooms;
import org.bql.rooms.three_cards.three_cards_1.model.HandCard;
import org.bql.utils.ArrayUtils;
import org.bql.utils.RandomUtils;
import org.bql.utils.builder_clazz.ann.Protocol;

import java.util.List;

/**
 * @作者： big
 * @创建时间： 2018/5/21
 * @文件描述：换牌
 */
@Protocol("1012")
public class GPRoom_ExchangeCard extends OperateCommandAbstract {
    /**要换牌的id*/
    private final int cardId;
    public GPRoom_ExchangeCard(int cardId) {
        this.cardId = cardId;
    }
    private FirstRooms rooms;
    private String account;
    @Override
    public Object execute() {
        FirstPlayerRoom player = (FirstPlayerRoom) getSession().getAttachment();
        HandCard handCard = player.getHandCard();
        if(handCard == null)
            new GenaryAppError(AppErrorCode.SERVER_ERR);
        Integer[] ids = handCard.getCardIds();
        if(!ArrayUtils.contains(ids,new Integer[]{cardId}))
            new GenaryAppError(AppErrorCode.DATA_ERR);
        rooms = (FirstRooms) player.getRoom();
        List<CardDataTable> residueCard = rooms.getGamblingParty().getExchangeCard();
        //检查还有没有剩余的牌
        if(residueCard == null || residueCard.size() <= 0)
            new GenaryAppError(AppErrorCode.NOT_CARD_CAN_EXCHANGE);

        PlayerInfoDto playerInfoDto = player.getPlayer();
        account = playerInfoDto.getAccount();
        List<ResourceModel> prop = playerInfoDto.getProps();
        if(prop == null || prop.size() <= 0)
            new GenaryAppError(AppErrorCode.NOT_EXCHANGE_CARD_PROP);
        //检查换牌卡
        int propId = -1;
        for(int i = 0;i<prop.size();i++){
            if(prop.get(i).getId() == 21){
                propId = i;
                break;
            }
        }
        if(propId == -1)
            new GenaryAppError(AppErrorCode.NOT_EXCHANGE_CARD_PROP);
        int random = RandomUtils.randomIndex(residueCard.size());
        //从余牌中随机获取一个再删除
        CardDataTable c = residueCard.get(random);
        //替换那个牌
        for(int i = 0;i<ids.length;i++){
            if(ids[i] == cardId){
                ids[i] = c.getId();
                break;
            }
        }
        residueCard.remove(random);//从剩余的牌中删除这个牌
        ResourceModel pro = prop.get(propId);
        pro.reduce(1);//减去换牌卡数量
        //返回牌id
        return new PositionDto(c.getId());
    }

    @Override
    public void broadcast() {
        rooms.broadcast(rooms.getPlayerSet().getNotAccountPlayer(account),NotifyCode.ROOM_PLAYER_EXCHANGE_CARD,new GPRoomExchangeCardDto(account,cardId));
    }
}
