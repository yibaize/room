package org.zgl.rooms.three_cards.three_cards_1.manage;

import org.zgl.error.AppErrorCode;
import org.zgl.error.GenaryAppError;
import org.zgl.utils.ArrayUtils;
import org.zgl.utils.logger.LoggerUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MyPlayerSet {
    public static final int MAX_SIZE = 5;//最大人数
    private final Integer[] pos = new Integer[]{0, 1, 2, 3, 4};
    //玩家位置信息
    private FirstRooms room;
    private ConcurrentHashMap<String, FirstPlayerRoom> playerForAccount;//玩家集合

    private final ConcurrentHashMap<String, Boolean> playerChoiceStateMap = new ConcurrentHashMap<>();//玩家的选择状态 key:玩家ID  value:是否同意
    private int compareNum;//本场比牌人数
    private Map<String, FirstPlayerRoom> nowPlay;//当前玩牌的人

    private ConcurrentHashMap<String, Integer> playerPosForAccount;//玩家位置
    private ConcurrentHashMap<Integer, String> playerAccountForPos;//玩家位置
    private List<Integer> position;//本局所有位置
    private List<Integer> losePosition;//本局输掉的玩家位置

    public MyPlayerSet() {
    }
    /**
     * 房间本局结束
     */
    public void end() {
        playerChoiceStateMap.clear();//玩家的选择状态 key:玩家ID  value:是否同意
        nowPlay.clear();//当前玩牌的人
        compareNum = 0;//本场比牌人数
        losePosition.clear();//本局输掉的玩家位置
        //删除缓存
        for(Map.Entry<String,FirstPlayerRoom> e:playerForAccount.entrySet()){
            if(!e.getValue().getSession().isConnected()){
                playerForAccount.remove(e.getKey());
                if(playerPosForAccount.contains(e.getKey())){
                    if(playerAccountForPos.contains(playerPosForAccount.get(e.getKey()))){
                        playerAccountForPos.remove(playerPosForAccount.get(e.getKey()));
                    }
                    playerPosForAccount.remove(e.getKey());
                }
            }
        }
    }
    public MyPlayerSet(FirstRooms room) {
        this.playerForAccount = new ConcurrentHashMap<>();
        this.playerPosForAccount = new ConcurrentHashMap<>();
        this.playerAccountForPos = new ConcurrentHashMap<>();
        this.losePosition = new ArrayList<>(MAX_SIZE);
        this.nowPlay = new ConcurrentHashMap<>();
        this.position = ArrayUtils.arrayToList(pos);
        this.room = room;
    }

    /**
     * 离开房间
     *
     * @param account
     */
    public void exit(String account) {
        if (playerForAccount.containsKey(account)) {
            playerForAccount.remove(account);
        }
        if (playerChoiceStateMap.containsKey(account)) {
            playerChoiceStateMap.remove(account);
        }
        if (nowPlay.containsKey(account)) {
            nowPlay.remove(account);
        }
        if (losePosition.contains(account)) {
            losePosition.remove(account);
        }
        if (playerPosForAccount.containsKey(account)) {
            int tempPos = playerPosForAccount.remove(account);
            if (!position.contains(tempPos))
                position.add(tempPos);//将位置放回
            playerAccountForPos.remove(tempPos);//移除对应位置
        }
    }

    public FirstRooms getRoom() {
        return room;
    }


    //设置玩家位置
    public void resetpos(FirstPlayerRoom p) {
        synchronized (lock) {
            if (playerForAccount.containsKey(p.getPlayer().getAccount()))
                new GenaryAppError(AppErrorCode.SERVER_ERR);
            int pos = getNullPosition();
            if (pos == -1)
                new GenaryAppError(AppErrorCode.SERVER_ERR);
            p.getPlayer().setRoomPosition(pos);
            String account = p.getPlayer().getAccount();
            playerForAccount.putIfAbsent(account, p);
            //设置位置
            playerPosForAccount.put(account, pos);
            playerAccountForPos.put(pos, account);
        }

    }
    private final Object lock = new Object();
    private int getNullPosition() {
        synchronized (lock) {
            if (position.size() <= 0)
                return -1;
            return position.remove(0);
        }
    }

    public Map<String, FirstPlayerRoom> getNowPlay() {
        return nowPlay;
    }

    public int getCompareNum() {
        return compareNum;
    }

    public void addCompareNum() {
        compareNum++;
    }

    public ConcurrentHashMap<String, FirstPlayerRoom> getPlayerForAccount() {
        return playerForAccount;
    }

    /**
     * 获取指定玩家的位置
     *
     * @param account
     * @return
     */
    public int getPlayerPos(String account) {
        return playerPosForAccount.getOrDefault(account, -1);
    }

    public List<FirstPlayerRoom> getAllPlayer() {
        return new ArrayList<>(playerForAccount.values());
    }

    /**
     * 获取除指定玩家除外的房间人
     *
     * @return
     */
    public List<FirstPlayerRoom> getNotAccountPlayer(String account) {
        ArrayList<FirstPlayerRoom> arr = new ArrayList<>(playerForAccount.size());
        for (Map.Entry<String, FirstPlayerRoom> e : playerForAccount.entrySet()) {
            if (!e.getKey().equals(account)) {
                arr.add(e.getValue());
            }
        }
        return arr;
    }

    /**
     * 获取房间人数
     *
     * @return
     */
    public int getRoomPlayerNum() {
        return playerForAccount.size();
    }

    /**
     * 输掉的玩家的位置
     *
     * @param account
     */
    public void losePlayer(String account) {
        nowPlay.remove(account);//从当前玩牌玩家集合中一处
        //添加到输牌的玩家集合中
        losePosition.add(playerPosForAccount.getOrDefault(account, -1));
    }

    public boolean isLose(int pos) {
        return losePosition.contains(pos);
    }

    public int loseNum() {
        return losePosition.size();
    }

    /**
     * 获取下一个有人而且在玩 还没输掉的人的账号
     *
     * @param nowPosition
     * @return
     */
    public String getNextPositionAccount(int nowPosition) {
        if(playerAccountForPos.size() <= 1)
            return null;
        List<Integer> temPos = new ArrayList<>(playerAccountForPos.keySet());
        Collections.sort(temPos);
        int index = 0;
        for (int i = 0; i < temPos.size(); i++) {
            if (nowPosition == temPos.get(i)) {
                index = i;
                break;
            }
        }
        int nextPos = getNextPos(index + 1, temPos.size(), temPos);
        if (nextPos == -1) {
            nextPos = getNextPos(0, index, temPos);
        }
        if (nextPos == -1) {
            return null;
        }
        return playerAccountForPos.get(nextPos);
    }

    /**
     * 获取下一个有人而且在玩 还没输掉的人的位置
     *
     * @param startIndex
     * @param endIndex
     * @param posList
     * @return
     */
    private int getNextPos(int startIndex, int endIndex, List<Integer> posList) {
        for (int i = startIndex; i < endIndex; i++) {
            int tempPos = posList.get(i);
            String acconut = playerAccountForPos.get(tempPos);
            if (playerChoiceStateMap.getOrDefault(acconut, false) && !losePosition.contains(tempPos)) {//这个人是已经准备的,平且没有输牌
                return tempPos;
            }
        }
        return -1;
    }

    /**
     * 根据账号找到玩家
     *
     * @param account
     * @return
     */
    public FirstPlayerRoom getPlayerForPosition(String account) {
        return playerForAccount.get(account);
    }

    /**
     * 获取指定位置上的玩家
     *
     * @param position
     * @return
     */
    public FirstPlayerRoom getPlayerForPosition(int position) {
        String account = playerAccountForPos.get(position);
        if (account == null)
            return null;
        return playerForAccount.get(account);
    }

    /**
     * 添加一个玩家的选择
     *
     * @param playerId
     * @param bor
     */
    public void addPlayerChoice(String playerId, boolean bor) {
        this.playerChoiceStateMap.put(playerId, bor);
    }

    /**
     * 获取指定选择的人数
     *
     * @param choice
     * @return
     */
    public int getChoiceNum(boolean choice) {
        int num = 0;
        for (Map.Entry<String, Boolean> entry : this.playerChoiceStateMap.entrySet()) {
            if (choice == entry.getValue()) {
                num++;
            }
        }
        return num;
    }

    /**
     * 当前在玩的人数
     *
     * @return
     */
    public int playNum() {
        return nowPlay.size();
    }

    /**
     * 获取指定玩家的选择
     *
     * @param playerId
     * @return
     */
    public boolean getPlayerChoice(String playerId) {
        if (playerChoiceStateMap.containsKey(playerId))
            return this.playerChoiceStateMap.get(playerId);
        return false;
    }

    /**
     * 获取房间指定玩家
     *
     * @param account
     * @return
     */
    public FirstPlayerRoom getPlayerForAccount(String account) {
        return playerForAccount.getOrDefault(account, null);
    }

    /**
     * 获取房间所有已经准备的玩家
     *
     * @return
     */
    public List<FirstPlayerRoom> getReadyPlayer(boolean state) {
        List<FirstPlayerRoom> list = new ArrayList<>(5);
        for (Map.Entry<String, Boolean> e : playerChoiceStateMap.entrySet()) {
            if (e.getValue() == state)
                list.add(playerForAccount.get(e.getKey()));
        }
        return list;
    }

    /**
     * 当前在玩的玩家
     *
     * @param list
     */
    public void nowPlayPlayer(List<FirstPlayerRoom> list) {
        Map<String, FirstPlayerRoom> m = new HashMap<>(list.size());
        for (FirstPlayerRoom f : list) {
            m.put(f.getPlayer().getAccount(), f);
        }
        nowPlay = m;
    }

    /**
     * 当前所有在玩的玩家
     */
    public List<FirstPlayerRoom> nowAllPayPlayer() {
        return new ArrayList<>(nowPlay.values());
    }

    /**
     * 查看某个玩家是否在玩
     *
     * @param account
     * @return
     */
    public boolean isPlayForAccount(String account) {
        return nowPlay.containsKey(account);
    }


}
