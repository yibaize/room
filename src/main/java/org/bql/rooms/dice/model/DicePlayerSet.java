package org.bql.rooms.dice.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DicePlayerSet {
    private final int maxPos = 4;
    private static final Object LOCK = new Object();
    private DiceRoom room;
    private Map<String, DicePlayer> allPlayer;//所有玩家
    private DicePlayer[] positions;
    private AtomicInteger nowPositionCount;//当前位置上的数量

    public DicePlayerSet(DiceRoom room) {
        this.room = room;
        this.allPlayer = new ConcurrentHashMap<>();
        this.positions = new DicePlayer[maxPos];
    }

    public void enter(DicePlayer player) {
        String account = player.getPlayer().getAccount();
        if (!allPlayer.containsKey(account)) {
            allPlayer.put(account, player);
            //TODO 通知所有人有人上线
            if (nowPositionCount.get() < positions.length) {
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
            //TODO 通知所有人有人下线
            if (player.getRoomPosition() != DicePlayer.DEFAULT_POSITION) {
                downPosition(player);
            }
        }
    }

    public void upPosition(DicePlayer player) {
        for (int i = 0; i < positions.length; i++) {
            if (positions[i] == null) {
                upPosition(player, i);
            }
        }
    }

    public boolean upPosition(DicePlayer player, int position) {
        if (player.getRoomPosition() != DicePlayer.DEFAULT_POSITION) {
            if (positions[position] == null) {
                positions[position] = player;
                nowPositionCount.incrementAndGet();
                player.setRoomPosition(position);
                //TODO 通知所有人有人上位置
                return true;
            }
        }
        return false;
    }

    public void downPosition(DicePlayer player) {
        if (player.getRoomPosition() == DicePlayer.DEFAULT_POSITION)
            return;
        int playerRoomPosition = player.getRoomPosition();
        if (playerRoomPosition > maxPos || playerRoomPosition < 0)
            return;
        DicePlayer tempPlayer = positions[player.getRoomPosition()];
        if (tempPlayer.getPlayer().getAccount().equals(player.getPlayer().getAccount())) {
            positions[playerRoomPosition] = null;
            nowPositionCount.decrementAndGet();
            tempPlayer.setRoomPosition(DicePlayer.DEFAULT_POSITION);
            //TODO 通知所有人有人下位置
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
        List<DicePlayer> list = new ArrayList<>(allPlayer.size() - 1);
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
}
