package org.bql.rooms.dice.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class DiceBet {
    private Map<DicePlayer,Long> players;
    private AtomicLong allMoney;

    public DiceBet() {
        players = new ConcurrentHashMap<>();
        allMoney = new AtomicLong(0);
    }

    public long getAllMoney() {
        return allMoney.get();
    }
    public List<DicePlayer> getBetAllPlayer(){
        return new ArrayList<>(players.keySet());
    }
    public void bet(DicePlayer dicePlayer,long num){
        if(players.containsKey(dicePlayer)){
            long number = players.get(dicePlayer);
            num += number;
        }
        players.put(dicePlayer,num);
        allMoney.addAndGet(num);
    }
}
