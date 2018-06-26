package org.zgl.chat;

import org.zgl.net.builder_clazz.NotifyCode;
import org.zgl.net.message.ServerResponse;
import org.zgl.net.server.manage.OperateCommandAbstract;
import org.zgl.player.PlayerInfoDto;
import org.zgl.player.PlayerRoom;
import org.zgl.rooms.RoomAbs;
import org.zgl.utils.ProtostuffUtils;
import org.zgl.utils.builder_clazz.ann.Protocol;

import java.util.List;
@Protocol("5599")
public class ChatRoomOperation extends OperateCommandAbstract {
    /**消息类型 表情（普通，vip） 文字*/
    private final int msgType;
    /**聊天内容*/
    private final String msg;

    public ChatRoomOperation( int msgType, String msg) {
        this.msgType = msgType;
        this.msg = msg;
    }

    @Override
    public Object execute() {

        return null;
    }

    @Override
    public void broadcast() {

        //这里只处理了房间内的聊天
        PlayerRoom playerRoom = (PlayerRoom) getSession().getAttachment();
        PlayerInfoDto infoDto = playerRoom.getPlayer();
        RoomAbs room = playerRoom.getRoom();
        //除了自己不通知
        List<PlayerRoom> players = room.getNotAccountAllPlayer(playerRoom.getPlayer().getAccount());

        ChatDto chatDto = new ChatDto();
        chatDto.setBroadcatType(2);
        chatDto.setMsg(msg);
        chatDto.setMsgType(msgType);
        chatDto.setUsername(infoDto.getUsername());
        chatDto.setAccount(infoDto.getAccount());
        chatDto.setVipLv(infoDto.getVipLv());

        byte[] buf = ProtostuffUtils.serializer(chatDto);
        for(PlayerRoom pr : players){
            pr.getSession().write(new ServerResponse(NotifyCode.CHAT,buf));
        }
    }
}
