package org.bql.chat;

import org.bql.net.builder_clazz.NotifyCode;
import org.bql.net.message.ServerResponse;
import org.bql.net.server.session.ISession;
import org.bql.net.server.session.SessionManager;
import org.bql.player.PlayerFactory;
import org.bql.utils.ProtostuffUtils;

import java.util.Map;

/**
 * @作者： big
 * @创建时间： 2018/5/22
 * @文件描述：广播消息
 */
public class Broadcast {
    /**
     * 除了account玩家之外广播所有人
     * @param account
     */
    public static void broadcastForNaotAccount(String account,ChatDto chatDto){
        if(account == null || account.equals(""))
            account = PlayerFactory.SYSTEM_PLAYER_ID;
        byte[] buf = ProtostuffUtils.serializer(chatDto);
        ServerResponse serverResponse = new ServerResponse(NotifyCode.BROADCAST,buf);
        for(Map.Entry<String,ISession> e:SessionManager.map().entrySet()){
            if(!e.getKey().equals(account)){
                e.getValue().write(serverResponse);
            }
        }
    }
}
