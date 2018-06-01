package org.bql.player;

import org.bql.net.server.session.ISession;
import org.bql.rooms.RoomAbs;

import java.util.concurrent.atomic.AtomicLong;

public class PlayerRoom implements IPlayer{
    private RoomAbs room;
    private ISession session;
    private PlayerInfoDto player;
    /**万人厂场或者时时乐下注是如果现在金币低于能下注的钱就不能再下注了*/
    private AtomicLong betNum = new AtomicLong(0);
    public ISession getSession() {
        return session;
    }

    public void setSession(ISession session) {
        this.session = session;
    }

    public void addBet(long num){
        betNum.addAndGet(num);
    }
    public void clearBet(){
        betNum.set(0);
    }
    public long canBet(){
        return getPlayer().getGold() - betNum.get() * 5;
    }
    public PlayerInfoDto getPlayer() {
        return player;
    }

    public RoomAbs getRoom() {
        return room;
    }

    public void setRoom(RoomAbs room) {
        this.room = room;
    }


    public void setPlayer(PlayerInfoDto player) {
        this.player = player;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
