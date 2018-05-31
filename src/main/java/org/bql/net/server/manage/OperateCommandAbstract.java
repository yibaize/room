package org.bql.net.server.manage;


import org.bql.net.server.session.ISession;
import org.bql.rooms.RoomAbs;

public abstract class OperateCommandAbstract implements IOperationCommand{
    private short cmdId;
    private ISession session;
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
    }

    @Override
    public void broadcast() {

    }
}