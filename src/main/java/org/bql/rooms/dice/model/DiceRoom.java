package org.bql.rooms.dice.model;

import org.bql.net.handler.TcpHandler;
import org.bql.net.message.ServerResponse;
import org.bql.player.IPlayer;
import org.bql.player.PlayerRoom;
import org.bql.rooms.RoomAbs;
import org.bql.rooms.RoomFactory;
import org.bql.rooms.card.CardManager;
import org.bql.rooms.dice.dto.DiceRoomInfiDto;
import org.bql.rooms.type.RoomStateType;
import org.bql.utils.ProtostuffUtils;

import java.util.ArrayList;
import java.util.List;

public class DiceRoom extends RoomAbs {
    private DicePlayerSet playerSet;
    private DiceGamblingParty gamblingParty;
    private CardManager cardManager;
    private RoomStateType roomState;
    public DiceRoom() {
        super(4,-753294, "",TcpHandler.getInstance().pool.nextWorker());
        playerSet = new DicePlayerSet(this);
        gamblingParty = new DiceGamblingParty(this);
    }

    public DicePlayerSet getPlayerSet() {
        return playerSet;
    }

    public void setPlayerSet(DicePlayerSet playerSet) {
        this.playerSet = playerSet;
    }

    public DiceGamblingParty getGamblingParty() {
        return gamblingParty;
    }

    public void setGamblingParty(DiceGamblingParty gamblingParty) {
        this.gamblingParty = gamblingParty;
    }

    public CardManager getCardManager() {
        return cardManager;
    }

    public void setCardManager(CardManager cardManager) {
        this.cardManager = cardManager;
    }

    public RoomStateType getRoomState() {
        return roomState;
    }

    public void setRoomState(RoomStateType roomState) {
        this.roomState = roomState;
    }

    @Override
    public Object roomInfo(String account) {
        return new DiceRoomInfiDto().dto(this,account);
    }

    @Override
    public void enterRoom(IPlayer player) {
    }

    @Override
    public void exitRoom(IPlayer player) {

    }

    @Override
    public IPlayer getPlayerForAccount(String account) {
        return playerSet.getPlayerForAccount(account);
    }

    @Override
    public int getPlayerNum() {
        return playerSet.getPlayerNum();
    }

    @Override
    public List<PlayerRoom> getNotAccountAllPlayer(String account) {
        List<DicePlayer> playerRooms = playerSet.getNotPlayerToAllPlayer(account);
        List<PlayerRoom> pr = new ArrayList<>(playerRooms.size());
        for(DicePlayer f:playerRooms){
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
        gamblingParty.timer();
    }

    @Override
    public void kicking(PlayerRoom player, int position) {

    }
    public void broadcast(List<DicePlayer> playerList, short cmdId, Object msg){
        ServerResponse r = response(cmdId,msg);
        for(DicePlayer tp : playerList){
            tp.getSession().write(r);
        }
    }
    private ServerResponse response(short cmdId,Object o){
        byte[] buf = null;
        if(o != null)
            buf = ProtostuffUtils.serializer(o);
        return new ServerResponse(cmdId,buf);
    }
}
