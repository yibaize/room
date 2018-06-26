package org.zgl.chat;

import org.zgl.net.builder_clazz.NotifyCode;
import org.zgl.net.message.ServerResponse;
import org.zgl.net.server.session.ISession;
import org.zgl.net.server.session.SessionManager;
import org.zgl.player.PlayerFactory;
import org.zgl.utils.ProtostuffUtils;

import java.util.Map;

/**
 * @作者： big
 * @创建时间： 2018/5/22
 * @文件描述：广播消息
 */
public class Broadcast {
    /**
     * 除了account玩家之外广播所有人
     * @param
     */
    public static void broadcastForNaotAccount(long uid,ChatDto chatDto){
        if(uid == 0)
            uid = PlayerFactory.SYSTEM_PLAYER_ID;
        byte[] buf = ProtostuffUtils.serializer(chatDto);
        ServerResponse serverResponse = new ServerResponse(NotifyCode.BROADCAST,buf);
        for(Map.Entry<Long,ISession> e:SessionManager.map().entrySet()){
            if(!e.getKey().equals(uid)){
                e.getValue().write(serverResponse);
            }
        }
    }
}
