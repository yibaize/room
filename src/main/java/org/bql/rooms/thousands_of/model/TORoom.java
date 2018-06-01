package org.bql.rooms.thousands_of.model;

import org.bql.net.handler.TcpHandler;
import org.bql.net.message.ServerResponse;
import org.bql.player.IPlayer;
import org.bql.player.PlayerRoom;
import org.bql.rooms.RoomAbs;
import org.bql.rooms.RoomFactory;
import org.bql.rooms.card.CardManager;
import org.bql.rooms.thousands_of.dto.TORoomInfoDto;
import org.bql.rooms.type.RoomStateType;
import org.bql.rooms.type.ScenesType;
import org.bql.utils.ProtostuffUtils;

import java.util.ArrayList;
import java.util.List;

public class TORoom extends RoomAbs {
    private TOPlayerSet playerSet;
    private TOGamblingParty gamblingParty;
    private CardManager cardManager;
    private RoomStateType roomState;// 1 当前可以下注，2 通知比牌并返回结果 3 通知本场结束 设置房间状态为下注状态
    public TORoom() {
        super(ScenesType.TO_ROOM.id(),-778899, "",TcpHandler.getInstance().pool.nextWorker());
        playerSet = new TOPlayerSet(this);
        cardManager = CardManager.getInstance();
        gamblingParty = new TOGamblingParty(this);
    }

    public TOPlayerSet getPlayerSet() {
        return playerSet;
    }

    public CardManager getCardManager() {
        return cardManager;
    }
    public TOGamblingParty getGamblingParty() {
        return gamblingParty;
    }

    public RoomStateType getRoomState() {
        return roomState;
    }

    public void setRoomState(RoomStateType roomState) {
        this.roomState = roomState;
    }

    /**
     * 当前庄家信息
     * 房间人数
     * 房间状态
     * 房间剩余时间
     * 位置信息
     * 历史记录
     * @return
     */
    @Override
    public Object roomInfo(String account) {
        return new TORoomInfoDto().dto(this,account);
    }

    @Override
    public void enterRoom(IPlayer player) {
        playerSet.enter((TOPlayer) player);
    }

    @Override
    public void exitRoom(IPlayer player) {
        playerSet.exit((TOPlayer) player);
        gamblingParty.exit((TOPlayer) player);
    }

    @Override
    public IPlayer getPlayerForAccount(String account) {
        return playerSet.getPlayerForAccount(account);
    }

    @Override
    public int getPlayerNum() {
        return playerSet.getAllPlayerNum();
    }

    @Override
    public List<PlayerRoom> getNotAccountAllPlayer(String account) {
        List<TOPlayer> playerRooms = playerSet.allPlayerNotId(account);
        List<PlayerRoom> pr = new ArrayList<>(playerRooms.size());
        for(TOPlayer f:playerRooms){
            pr.add(f);
        }
        return pr;
    }

    @Override
    public List<PlayerRoom> getAllPlayer() {
        return new ArrayList<>(playerSet.getAllPlayer());
    }

    @Override
    public void timer() {
        getGamblingParty().timer();
    }

    public void broadcast(List<TOPlayer> playerList, short cmdId, Object msg){
        ServerResponse r = response(cmdId,msg);
        for(TOPlayer tp : playerList){
            tp.getSession().write(r);
        }
    }
    private ServerResponse response(short cmdId,Object o){
        byte[] buf = null;
        if(o != null)
            buf = ProtostuffUtils.serializer(o);
        return new ServerResponse(cmdId,buf);
    }

    @Override
    public void kicking(PlayerRoom player,int position) {
        playerSet.kicking((TOPlayer) player,position);
    }

    public void end(){
    }
}
