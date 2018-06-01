package org.bql.rooms.always_happy.model;

import org.bql.net.handler.TcpHandler;
import org.bql.net.message.ServerResponse;
import org.bql.player.IPlayer;
import org.bql.player.PlayerFactory;
import org.bql.player.PlayerRoom;
import org.bql.rooms.RoomAbs;
import org.bql.rooms.RoomFactory;
import org.bql.rooms.always_happy.dto.AHHistorysDtos;
import org.bql.rooms.type.ScenesType;
import org.bql.utils.ProtostuffUtils;
import org.bql.utils.executer.NioServerWorker;

import java.util.List;

/**
 * @作者： big
 * @创建时间： 2018/5/22
 * @文件描述：
 */
public class AHRoom extends RoomAbs {
    /**
     * 创建房间
     *
     * @param
     * @param
     * @param
     */
    private AHGamblingParty gamblingParty;
    private AHPlayerSet playerSet;

    public AHRoom() {
        super(ScenesType.AH_ROOM.id(), -1111, PlayerFactory.SYSTEM_PLAYER_ID,TcpHandler.getInstance().pool.nextWorker());
        this.playerSet = new AHPlayerSet(this);
        this.gamblingParty = new AHGamblingParty(this);
    }

    public AHGamblingParty getGamblingParty() {
        return gamblingParty;
    }

    public AHPlayerSet getPlayerSet() {
        return playerSet;
    }

    public void braodcast(List<PlayerRoom> players, short id, Object msg) {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setId(id);
        byte[] buf = null;
        if (msg != null) {
            buf = ProtostuffUtils.serializer(msg);
        }
        serverResponse.setData(buf);
        for (PlayerRoom ap : players) {
            if (ap.getSession().isConnected())
                ap.getSession().write(serverResponse);
        }
    }

    @Override
    public Object roomInfo(String account) {
        AHHistorysDtos dtos = new AHHistorysDtos();
        dtos.setPlayerNum(gamblingParty.getNowBetPlayerNum());
        dtos.setLastTimeGrantAward(gamblingParty.getLastTimeGrantAward());
        dtos.setNowBetMoney(gamblingParty.getAllMoney());
        dtos.setHistoryDtos(gamblingParty.getHistory());
        dtos.setRoomTime(gamblingParty.getTimer());
        return dtos;
    }

    @Override
    public void enterRoom(IPlayer player) {
        playerSet.enter((PlayerRoom) player);
    }

    @Override
    public void exitRoom(IPlayer player) {
        playerSet.exit((PlayerRoom) player);
    }

    @Override
    public IPlayer getPlayerForAccount(String account) {
        return null;
    }

    @Override
    public int getPlayerNum() {
        return 0;
    }

    @Override
    public List<PlayerRoom> getNotAccountAllPlayer(String account) {
        return null;
    }

    @Override
    public List<PlayerRoom> getAllPlayer() {
        return null;
    }

    @Override
    public void timer() {
        getGamblingParty().timer();
    }

    @Override
    public void kicking(PlayerRoom player, int position) {

    }
}
