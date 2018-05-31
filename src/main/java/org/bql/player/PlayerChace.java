package org.bql.player;

import org.bql.net.server.session.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerChace {
    private static Map<String,PlayerRoom> playerMap = new ConcurrentHashMap<>();
    public static void into(PlayerRoom p){
        String account = p.getPlayer().getAccount();
        SessionManager.putSession(account,p.getSession());
        playerMap.putIfAbsent(p.getPlayer().getAccount(),p);
    }
    public static PlayerRoom leave(PlayerRoom p){
        String account = p.getPlayer().getAccount();
        SessionManager.removeSession(account);
        if(playerMap.containsKey(account))
            return playerMap.remove(account);
        return null;
    }
    public List<PlayerRoom> allPlayer(){
        return new ArrayList<>(playerMap.values());
    }
}
