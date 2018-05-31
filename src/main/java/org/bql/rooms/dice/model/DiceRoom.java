package org.bql.rooms.dice.model;

import org.bql.player.IPlayer;
import org.bql.player.PlayerRoom;
import org.bql.rooms.RoomAbs;
import org.bql.rooms.card.CardManager;

import java.util.ArrayList;
import java.util.List;

public class DiceRoom extends RoomAbs {
    private DicePlayerSet playerSet;
    private DiceGamblingParty gamblingParty;
    private CardManager cardManager;
    private int roomState;
    public DiceRoom() {
        super(4,-753294, "");
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

    public int getRoomState() {
        return roomState;
    }

    public void setRoomState(int roomState) {
        this.roomState = roomState;
    }

    @Override
    public Object roomInfo(String account) {
        return null;
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

    }

    @Override
    public void kicking(PlayerRoom player, int position) {

    }
}
