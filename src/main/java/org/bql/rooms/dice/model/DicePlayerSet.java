package org.bql.rooms.dice.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DicePlayerSet {
    private static final Object LOCK = new Object();
    private DiceRoom room;
    private Map<String,DicePlayer> allPlayer;//所有玩家
    private int betCount = 0;// 投注次的胡

    public DicePlayerSet(DiceRoom room) {
        this.room = room;
        allPlayer = new ConcurrentHashMap<>();

    }

    /**
     * 获取所有玩家
     * @return
     */
    public List<DicePlayer> getAllPlayer(){
        return new ArrayList<>(allPlayer.values());
    }

    /**
     * 获取除指定玩家以外的所有人
     * @param account
     * @return
     */
    public List<DicePlayer> getNotPlayerToAllPlayer(String account){
        List<DicePlayer> list = new ArrayList<>(allPlayer.size() - 1);
        for(DicePlayer d : allPlayer.values()){
            if(!d.getPlayer().getAccount().equals(account)){
                list.add(d);
            }
        }
        return list;
    }
    /**
     * 获取房间人数
     * @return
     */
    public int getPlayerNum(){
        return allPlayer.size();
    }
    public DicePlayer getPlayerForAccount(String account){
        return allPlayer.getOrDefault(account,null);
    }
}
