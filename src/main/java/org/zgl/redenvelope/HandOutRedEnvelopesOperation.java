package org.zgl.redenvelope;

import org.zgl.error.AppErrorCode;
import org.zgl.error.GenaryAppError;
import org.zgl.net.builder_clazz.NotifyCode;
import org.zgl.net.message.ServerResponse;
import org.zgl.net.server.manage.OperateCommandAbstract;
import org.zgl.net.server.session.ISession;
import org.zgl.net.server.session.SessionManager;
import org.zgl.player.PlayerInfoDto;
import org.zgl.player.PlayerRoom;
import org.zgl.player.ResourceModel;
import org.zgl.remote.HttpProxyOutboundHandler;
import org.zgl.remote.IBackHall;
import org.zgl.remote.IRedEvenlopes;
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
    //红红包数量
    private final int redEvenlopesType;
    private final long targetUid;
    private final String explain;
    //红包个数
    private final int redEnvelopesCount;
    public HandOutRedEnvelopesOperation(long targetUid,int redEnvelopesCount,String explain,int redEvenlopesType) {
        this.redEvenlopesType = redEvenlopesType;
        this.targetUid = targetUid;
        this.explain = explain;
        this.redEnvelopesCount = redEnvelopesCount;
    }

    @Override
    public Object execute() {
        PlayerRoom player = (PlayerRoom) getSession().getAttachment();
        PlayerInfoDto playerInfoDto = player.getPlayer();
        if(!playerInfoDto.reduceGold(redEvenlopesType)){
            new GenaryAppError(AppErrorCode.GOLD_NOT);
        }
        DBRedEvenlopes redEvenlopes = new DBRedEvenlopes();
        redEvenlopes.setUid(playerInfoDto.getUid());
        redEvenlopes.setCreateTime(new Date(new java.util.Date().getTime()));
        redEvenlopes.setHasGet(redEnvelopesCount);
        redEvenlopes.setHeadIcon(playerInfoDto.getHeadIcon());
        redEvenlopes.setNum(redEnvelopesCount);
        redEvenlopes.setRedEvenlopesType(redEvenlopesType);
        redEvenlopes.setUserName(playerInfoDto.getUsername());
        redEvenlopes.setVipLv(playerInfoDto.getVipLv());
        redEvenlopes.setTargetUid(targetUid);
        redEvenlopes.setNumed(0);
        redEvenlopes.setExplain(explain);
        redEvenlopes.setResidueGold(GOLD.get(redEnvelopesCount));

        IRedEvenlopes iRedEvenlopes = HttpProxyOutboundHandler.getRemoteProxyObj(IRedEvenlopes.class);
        iRedEvenlopes.insertRedEvenlopes(redEvenlopes);

        IBackHall backHall = HttpProxyOutboundHandler.getRemoteProxyObj(IBackHall.class);
        List<PlayerInfoDto> list = new ArrayList<>(1);
        list.add(playerInfoDto);
        if(backHall.backHall(list) != -200){
            new GenaryAppError(AppErrorCode.DATA_ERR);
        }
        HandOutRedEvenlopesDto dto = new HandOutRedEvenlopesDto();
        dto.setUid(playerInfoDto.getUid());
        dto.setUserName(playerInfoDto.getUsername());
        dto.setHeadIcon(playerInfoDto.getHeadIcon());
        dto.setVipLv(playerInfoDto.getVipLv());
        dto.setExplain(explain);
        byte[] buf = ProtostuffUtils.serializer(dto);
        ServerResponse serverResponse = new ServerResponse(NotifyCode.BROADCAST,buf);
        for(Map.Entry<Long,ISession> e:SessionManager.map().entrySet()){
            if(!e.getKey().equals(playerInfoDto.getUid())){
                e.getValue().write(serverResponse);
            }
        }
        return null;
    }
}
