package org.zgl.net.server.manage;


import org.zgl.net.handler.TcpHandler;
import org.zgl.net.server.session.ISession;
import org.zgl.player.PlayerRoom;
import org.zgl.rooms.RoomAbs;

public abstract class OperateCommandAbstract implements IOperationCommand{
    private short cmdId;
    private ISession session;
    private RoomAbs room;
    private TcpHandler tcpHandler = TcpHandler.getInstance();
    public short getCmdId() {
        return cmdId;
    }

    public void setCmdId(short cmdId) {
        this.cmdId = cmdId;
    }

    public ISession getSession() {
        return session;
    }

    public void setSession(ISession session) {
        this.session = session;
    }

    public void run() {
        try {
            Object o = execute();
            tcpHandler.response(cmdId,session,o);
            broadcast();
        }catch (Exception e){
            tcpHandler.errRecive(e,session);
        }
    }
    public void subim(){
        PlayerRoom player = (PlayerRoom) getSession().getAttachment();
        if(player != null && player.getRoom() != null){
            room = player.getRoom();
            room.submin(this);
        }else {
            run();
        }
    }
    @Override
    public void broadcast() {

    }
}