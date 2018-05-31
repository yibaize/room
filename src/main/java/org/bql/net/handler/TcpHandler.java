package org.bql.net.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.bql.error.*;
import org.bql.net.builder_clazz.OperateCommandRecive;
import org.bql.net.message.Msg;
import org.bql.net.message.ServerRequest;
import org.bql.net.message.ServerResponse;
import org.bql.net.server.NettySerializable;
import org.bql.net.server.manage.OperateCommandAbstract;
import org.bql.net.server.session.ISession;
import org.bql.net.server.session.SessionImpl;
import org.bql.player.PlayerInfoDto;
import org.bql.player.PlayerRoom;
import org.bql.utils.logger.LoggerUtils;
import org.bql.utils.ProtostuffUtils;
import org.bql.utils.StringUtils;
public class TcpHandler {
    public static void massegeRansiter(ISession session, ServerRequest request) {
        try {
            short id = request.getId();
            String[] s = StringUtils.split(request.getData().getMsg(),",");
            OperateCommandAbstract msg = OperateCommandRecive.getInstance().recieve(id,s);
            msg.setCmdId(id);
            msg.setSession(session);
            Object o = msg.execute();
            response(id,session,o);
            msg.broadcast();
        }catch (Exception e){
            errRecive(e,session);
        }
    }
    private static void response(short cmdId,ISession session,Object o){
        byte[] buf = null;
        if (o != null)
            buf = ProtostuffUtils.serializer(o);
        ServerResponse response =  new ServerResponse(cmdId, buf);
        session.write(response);
    }

    /**
     * 异常处理
     * @param e
     * @param session
     */
    private static void errRecive(Exception e,ISession session){
        PlayerRoom playerRoom = (PlayerRoom) session.getAttachment();
        PlayerInfoDto player = null;
        if(playerRoom == null){
                  player = new PlayerInfoDto();
                    player.setId(-999999999);
                  player.setAccount("-999999999");
                  player.setUsername("-系统链接");
        }else {
            player = playerRoom.getPlayer();
        }
//

        if(e instanceof ErrorAbs) {
            ErrorAbs errorAbs = (ErrorAbs) e;
            if (e instanceof GenaryAppError) {
                GenaryAppError error = (GenaryAppError) e;
                error((short) error.getErrorCode(), session);
            } else if (e instanceof LogAppError) {
                LogAppError error = (LogAppError) e;
                LoggerUtils.getLogicLog().error(error.getMessage());
                error((short) AppErrorCode.DATA_ERR, session);
            } else if (e instanceof CloseConnectionError) {
                CloseConnectionError error = (CloseConnectionError) e;
                error((short) error.getErrorCode(), session);
                session.close();
            }
            LoggerUtils.getLogicLog().info(format("用户id:<<%s>>:用户名:<<%s>>操作异常<---||--->；异常码：<<%s>>(^_^)异常信息：<<%s>>",player.getId(),player.getUsername(),errorAbs.getErrorCode(),errorAbs.getMsg()), e);
        }else {
            error((short) AppErrorCode.SERVER_ERR,session);
            LoggerUtils.getLogicLog().info(format("用户id:<<%s>>:用户名:<<%s>>",player.getId(),player.getUsername()), e);
        }
    }
    private static String format(String str,Object...args){
        return String.format(str,args);
    }
    private static void error(short errorCode,ISession session){
        ByteBuf buf = NettySerializable.getBuffer();
        buf.writeShort(errorCode);
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        ServerResponse response =  new ServerResponse((short) 404, bytes);
        session.write(response);
    }
    public static void exceptionCaught(Channel ctx,Throwable cause){
        String str = String.format("%s ：<<---用户异常下线--->> %s",cause.getMessage(),ctx.remoteAddress(),cause);
        massegeRansiter(new SessionImpl(ctx), new ServerRequest((short) 10003, new Msg("")));
        LoggerUtils.getLogicLog().error(str,cause);
    }
    public static void channelInactive(Channel ctx){
        massegeRansiter(new SessionImpl(ctx), new ServerRequest((short) 10003, new Msg("")));
        LoggerUtils.getLogicLog().info("用户下线"+ctx.remoteAddress());
    }
    public static void channelActive(Channel ctx){
        LoggerUtils.getLogicLog().info("用户上线"+ctx.remoteAddress());
    }

    public static void main(String[] args) {
        System.out.println(String.format("a:%s,b:%s","a",1));
        LoggerUtils.getLogicLog().info("das");
    }
}