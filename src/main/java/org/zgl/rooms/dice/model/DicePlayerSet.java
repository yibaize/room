package org.zgl.rooms.dice.model;
import org.zgl.net.builder_clazz.NotifyCode;
import org.zgl.player.PlayerInfoDto;
import org.zgl.player.PlayerRoomBaseInfoDto;
import org.zgl.rooms.three_cards.three_cards_1.dto.RoomPlayerAccountDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DicePlayerSet {
    private final int maxPos = 4;
    private static final Object LOCK = new Object();
    private DiceRoom room;
    private Map<String, DicePlayer> allPlayer;//所有玩家
    private DicePlayer[] positions;
    private int nowPositionCount;//当前位置上的数量

    public DicePlayerSet(DiceRoom room) {
        this.room = room;
        this.allPlayer = new ConcurrentHashMap<>();
        this.positions = new DicePlayer[maxPos];
    }

    public void enter(DicePlayer player) {
        String account = player.getPlayer().getAccount();
        if (!allPlayer.containsKey(account)) {
            allPlayer.put(account, player);
//            room.broadcast(getNotPlayerToAllPlayer(account), NotifyCode.ENTER_ROOM, null); //有人进入房间
            if (nowPositionCount < positions.length) {
                upPosition(player);
            }
        }
    }

    /**
     * 进入房间
     *
     * @param player
     */
    public void exit(DicePlayer player) {
        String account = player.getPlayer().getAccount();
        if (allPlayer.containsKey(account)) {
            allPlayer.remove(account);
            if (player.getRoomPosition() != DicePlayer.DEFAULT_POSITION) {
                downPosition(player);
            }
        }
    }

    public void upPosition(DicePlayer player) {
        for (int i = 0; i < positions.length; i++) {
            if (positions[i] == null) {
                upPosition(player, i);
                break;
            }
        }
    }

    public boolean upPosition(DicePlayer player, int position) {
        if (player.getRoomPosition() == DicePlayer.DEFAULT_POSITION) {
            if (positions[position] == null) {
                positions[position] = player;
                nowPositionCount++;
                player.setRoomPosition(position);
                PlayerRoomBaseInfoDto prbif = notifyDto(position, player.getPlayer(), 0);
                //通知所有玩家
                room.broadcast(getNotPlayerToAllPlayer(player.getPlayer().getAccount()), NotifyCode.UP_POSTION, prbif); //通知所有人有人上了位置
                return true;
            }
        }
        return false;
    }
    private PlayerRoomBaseInfoDto notifyDto(int pos, PlayerInfoDto pd, long bottomNum) {
        PlayerRoomBaseInfoDto prbif = new PlayerRoomBaseInfoDto();
        prbif.setUid(pd.getUid());
        prbif.setAccount(pd.getAccount());
        prbif.setPostion(pos);
        prbif.setBottomNum(bottomNum);
        prbif.setGold(pd.getGold());
        prbif.setUserName(pd.getUsername());
        prbif.setHeadIcon(pd.getHeadIcon());
        prbif.setVipLv(pd.getVipLv());
        prbif.setAutoId(pd.getNowUserAutos());
        prbif.setGender(pd.getGender());
        return prbif;
    }
    public void downPosition(DicePlayer player) {
        if (player.getRoomPosition() == DicePlayer.DEFAULT_POSITION)
            return;
        int playerRoomPosition = player.getRoomPosition();
        if (playerRoomPosition > maxPos || playerRoomPosition < 0)
            return;
        DicePlayer tempPlayer = positions[player.getRoomPosition()];
        if(tempPlayer == null)
            return;
        if (tempPlayer.getPlayer().getAccount().equals(player.getPlayer().getAccount())) {
            positions[playerRoomPosition] = null;
            nowPositionCount--;
            tempPlayer.setRoomPosition(DicePlayer.DEFAULT_POSITION);
            room.broadcast(getNotPlayerToAllPlayer(player.getPlayer().getAccount()), NotifyCode.DOWN_POSITION, new RoomPlayerAccountDto(player.getPlayer().getAccount())); //有人离开房间
        }
    }

    /**
     * 获取所有玩家
     *
     * @return
     */
    public List<DicePlayer> getAllPlayer() {
        return new ArrayList<>(allPlayer.values());
    }

    /**
     * 获取除指定玩家以外的所有人
     *
     * @param account
     * @return
     */
    public List<DicePlayer> getNotPlayerToAllPlayer(String account) {
        List<DicePlayer> list = new ArrayList<>();
        for (DicePlayer d : allPlayer.values()) {
            if (!d.getPlayer().getAccount().equals(account)) {
                list.add(d);
            }
        }
        return list;
    }

    /**
     * 获取房间人数
     *
     * @return
     */
    public int getPlayerNum() {
        return allPlayer.size();
    }

    public DicePlayer getPlayerForAccount(String account) {
        return allPlayer.getOrDefault(account, null);
    }
    public List<DicePlayer> postionAllPlayer(){
        List<DicePlayer> list = new ArrayList<>();
        for(int i = 0;i<positions.length;i++){
            if(positions[i] != null){
                list.add(positions[i]);
            }
        }
        return list;
    }
    public void end(){
        for(Map.Entry<String,DicePlayer> e: allPlayer.entrySet()){
            if(!e.getValue().getSession().isConnected()){
                allPlayer.remove(e.getKey());
            }
        }
        for(int i = 0;i < positions.length;i++){
            if(positions[i] != null){
                if(!positions[i].getSession().isConnected()){
                    positions[i] = null;
                }
            }
        }
    }
}
