package org.zgl.redenvelope.cmd;

import org.zgl.error.AppErrorCode;
import org.zgl.error.GenaryAppError;
import org.zgl.net.builder_clazz.NotifyCode;
import org.zgl.net.message.ServerResponse;
import org.zgl.net.server.manage.OperateCommandAbstract;
import org.zgl.net.server.session.ISession;
import org.zgl.net.server.session.SessionManager;
import org.zgl.player.PlayerInfoDto;
import org.zgl.player.PlayerRoom;
import org.zgl.redenvelope.dto.DBRedEvenlopes;
import org.zgl.redenvelope.dto.HandOutRedEvenlopesDto;
import org.zgl.remote.HttpProxyOutboundHandler;
import org.zgl.remote.IBackHall;
import org.zgl.remote.IRedEvenlopes;
import org.zgl.rooms.thousands_of.dto.PlayerWeathUpdateDto;
import org.zgl.rooms.thousands_of.dto.PositionDto;
import org.zgl.utils.DateUtils;
import org.zgl.utils.ProtostuffUtils;
import org.zgl.utils.builder_clazz.ann.Protocol;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @作者： big
 * @创建时间： 2018/6/22
 * @文件描述：发红包
 */
@Protocol("1033")
public class HandOutRedEnvelopesOperation extends OperateCommandAbstract {
    private static final Map<Integer,Integer> GOLD = new HashMap<>();
    static {
        GOLD.put(5,50000);
        GOLD.put(10,100000);
        GOLD.put(20,200000);
    }
    private final int redEvenlopesType;
    private final String explain;
    //红包个数
    private final int redEnvelopesCount;
    public HandOutRedEnvelopesOperation(int redEnvelopesCount,String explain,int redEvenlopesType) {
        this.redEvenlopesType = redEvenlopesType;
        this.explain = explain;
        this.redEnvelopesCount = redEnvelopesCount;
    }

    @Override
    public Object execute() {
        PlayerRoom player = (PlayerRoom) getSession().getAttachment();
        PlayerInfoDto playerInfoDto = player.getPlayer();
        int reduceGold = GOLD.get(redEnvelopesCount);
        if(!playerInfoDto.reduceGold(reduceGold)){
            new GenaryAppError(AppErrorCode.GOLD_NOT);
        }
        DBRedEvenlopes redEnvelopse = new DBRedEvenlopes();
        redEnvelopse.setUid(playerInfoDto.getUid());
        redEnvelopse.setCreateTime(DateUtils.currentTime());
        redEnvelopse.setHasGet(reduceGold);
        redEnvelopse.setHeadIcon(playerInfoDto.getHeadIcon());
        redEnvelopse.setNum(redEnvelopesCount);
        redEnvelopse.setRedEvenlopesType(redEvenlopesType);
        redEnvelopse.setUserName(playerInfoDto.getUsername());
        redEnvelopse.setVipLv(playerInfoDto.getVipLv());
        redEnvelopse.setNumed(0);
        redEnvelopse.setExplain(explain);
        redEnvelopse.setResidueGold(reduceGold);

        IRedEvenlopes iRedEvenlopes = HttpProxyOutboundHandler.getRemoteProxyObj(IRedEvenlopes.class);
        int redEnvelopesId = iRedEvenlopes.insertRedEvenlopes(redEnvelopse);

        IBackHall backHall = HttpProxyOutboundHandler.getRemoteProxyObj(IBackHall.class);
        List<PlayerInfoDto> list = new ArrayList<>(1);
        list.add(playerInfoDto);
        if(backHall.backHall(list) != 200){
            new GenaryAppError(AppErrorCode.DATA_ERR);
        }
        PositionDto dto = new PositionDto(redEnvelopesId);
        byte[] buf = ProtostuffUtils.serializer(dto);
        ServerResponse serverResponse = new ServerResponse(NotifyCode.HAND_OUT_RED_ENVELOPES,buf);
        for(Map.Entry<Long,ISession> e:SessionManager.map().entrySet()){
            if(!e.getValue().isConnected())
                continue;
            if(!e.getKey().equals(playerInfoDto.getUid())){
                e.getValue().write(serverResponse);
            }
        }
        return new PlayerWeathUpdateDto(playerInfoDto.getGold(),GOLD.get(redEnvelopesCount));
    }
}
