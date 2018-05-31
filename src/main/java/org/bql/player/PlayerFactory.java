package org.bql.player;

import org.bql.rooms.dice.model.DicePlayer;
import org.bql.rooms.thousands_of.model.TOPlayer;
import org.bql.rooms.three_cards.three_cards_1.manage.FirstPlayerRoom;

public class PlayerFactory {
    public static final String SYSTEM_PLAYER_ID = "-99999999";
    private static PlayerFactory instance;
    public static PlayerFactory getInstance() {
        if(instance == null)
            instance = new PlayerFactory();
        return instance;
    }
    public FirstPlayerRoom fPlayer(){
        return new FirstPlayerRoom();
    }
    public PlayerRoom playerRoom(){
        return new PlayerRoom();
    }
    public TOPlayer toPlayer(){
        return new TOPlayer();
    }
    public DicePlayer dicePlayer(){
        return new DicePlayer();
    }
    public PlayerRoom ahPlayer(){
        return new PlayerRoom();
    }
}
