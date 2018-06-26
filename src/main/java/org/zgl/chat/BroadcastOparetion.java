package org.zgl.chat;

import org.zgl.error.AppErrorCode;
import org.zgl.error.GenaryAppError;
import org.zgl.net.server.manage.OperateCommandAbstract;
import org.zgl.player.PlayerInfoDto;
import org.zgl.player.PlayerRoom;
import org.zgl.player.ResourceModel;
import org.zgl.utils.builder_clazz.ann.Protocol;

import java.util.List;

/**
 * @作者： big
 * @创建时间： 2018/5/22
 * @文件描述：
 */
@Protocol("5560")
public class BroadcastOparetion extends OperateCommandAbstract {
    /**系统消息或是玩家消息*/
    private final int sysOrUser;
    private final int type;
    private final String msg;

    public BroadcastOparetion(int sysOrUser, int type, String msg) {
        this.sysOrUser = sysOrUser;
        this.type = type;
        this.msg = msg;
    }

    private PlayerInfoDto infoDto;
    @Override
    public Object execute() {
        if(sysOrUser == 1)
            return null;
        PlayerRoom player = (PlayerRoom) getSession().getAttachment();
        //27
        infoDto = player.getPlayer();
        List<ResourceModel> prop = infoDto.getProps();
        if(prop == null || prop.size() <= 0)
            new GenaryAppError(AppErrorCode.NOT_TRUMPET);
        ResourceModel trumpt = null;
        for(int i = 0;i<prop.size();i++){
            if(prop.get(i).getId() == 24){
                trumpt = prop.get(i);
                break;
            }
        }
        if(trumpt == null)
            new GenaryAppError(AppErrorCode.NOT_TRUMPET);
        if(!trumpt.reduce(1)){
            new GenaryAppError(AppErrorCode.NOT_TRUMPET);
        }
        return null;
    }
    /**
     * 广播消息
     */
    @Override
    public void broadcast() {
        ChatDto chatDto = new ChatDto();
        chatDto.setBroadcatType(sysOrUser);
        chatDto.setMsg(msg);
        chatDto.setMsgType(type);
        if(sysOrUser != 1){
            chatDto.setUsername(infoDto.getUsername());
            chatDto.setAccount(infoDto.getAccount());
            chatDto.setVipLv(infoDto.getVipLv());
        }
        Broadcast.broadcastForNaotAccount(infoDto.getUid(),chatDto);
    }
}
