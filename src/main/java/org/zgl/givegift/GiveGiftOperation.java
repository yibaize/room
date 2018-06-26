package org.zgl.givegift;

import org.zgl.error.AppErrorCode;
import org.zgl.error.GenaryAppError;
import org.zgl.net.builder_clazz.NotifyCode;
import org.zgl.net.message.ServerResponse;
import org.zgl.net.server.manage.OperateCommandAbstract;
import org.zgl.net.server.session.ISession;
import org.zgl.net.server.session.SessionManager;
import org.zgl.player.PlayerInfoDto;
import org.zgl.player.PlayerRoom;
import org.zgl.remote.HttpProxyOutboundHandler;
import org.zgl.remote.IBackHall;
import org.zgl.rooms.thousands_of.dto.PlayerWeathUpdateDto;
import org.zgl.utils.ProtostuffUtils;
import org.zgl.utils.builder_clazz.ann.Protocol;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者： big
 * @创建时间： 2018/6/23
 * @文件描述：送礼物
 */
@Protocol("1035")
public class GiveGiftOperation extends OperateCommandAbstract {
    private final int giftId;
    private final long targetUid;

    public GiveGiftOperation(int gitfId, long targetUid) {
        this.giftId = gitfId;
        this.targetUid = targetUid;
    }

    @Override
    public Object execute() {
        CommodityDataTable dataTable = CommodityDataTable.get(giftId);
        if (dataTable == null)
            new GenaryAppError(AppErrorCode.NOT_IS_GIF);
        if (dataTable.getIntegral() != -1)//不是礼物不能送
            new GenaryAppError(AppErrorCode.NOT_IS_GIF);
        PlayerRoom selfPlayer = (PlayerRoom) getSession().getAttachment();
        PlayerInfoDto selfPlayerInfo = selfPlayer.getPlayer();
        if (!selfPlayerInfo.reduceGold(dataTable.getSelling())) {
            new GenaryAppError(AppErrorCode.GOLD_NOT);
        }
        ISession targetSession = SessionManager.getSession(targetUid);
        if(targetSession == null)
            new GenaryAppError(AppErrorCode.FRIENT_NOT_ONLIEN);
        PlayerRoom targetPlayer = (PlayerRoom) targetSession.getAttachment();
        if (targetPlayer == null)
            new GenaryAppError(AppErrorCode.FRIENT_NOT_ONLIEN);
        PlayerInfoDto targetInfo = targetPlayer.getPlayer();
        if (targetInfo.getScenesId() == selfPlayerInfo.getScenesId() && targetInfo.getRoomId() == selfPlayerInfo.getRoomId()) {
            //不在同一个场不能送
            if (!dataTable.getDescribe().equals("1"))
                new GenaryAppError(AppErrorCode.NOT_IS_LIKE_SCENES);
        }
        //加礼物数量
        long character = targetInfo.getCharacterNum() + Long.parseLong(dataTable.getEffect());
        targetInfo.setCharacterNum(character);//加人品值
        targetInfo.insertGift(giftId, dataTable.getShopId(), dataTable.getCount());
        List<PlayerInfoDto> list = new ArrayList<>(2);
        list.add(selfPlayerInfo);
        list.add(targetInfo);
        IBackHall backHall = HttpProxyOutboundHandler.getRemoteProxyObj(IBackHall.class);
        backHall.backHall(list);
        targetPlayer.getSession().write(null);
        ServerResponse response = new ServerResponse();
        byte[] buf = ProtostuffUtils.serializer(new GiveGiftDto(selfPlayerInfo.getUsername(), giftId));
        response.setData(buf);
        response.setId(NotifyCode.GET_GIFT);
        targetPlayer.getSession().write(response);
        return new PlayerWeathUpdateDto(selfPlayerInfo.getGold(),0);
    }
}
