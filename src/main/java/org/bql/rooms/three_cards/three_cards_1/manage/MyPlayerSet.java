package org.bql.rooms.three_cards.three_cards_1.manage;

import org.bql.error.AppErrorCode;
import org.bql.error.GenaryAppError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyPlayerSet {
    public static final int MAX_SIZE = 5;//最大人数
    //玩家位置信息
    private FirstPlayerRoom[] playerPos;
    private FirstRooms room;
    private ConcurrentHashMap<String,FirstPlayerRoom> playerForAccount;//玩家集合
    private ConcurrentHashMap<String,Integer> playerPosForAccount;//玩家位置
    private final ConcurrentHashMap<String,Boolean> playerChoiceStateMap = new ConcurrentHashMap<>();//玩家的选择状态 key:玩家ID  value:是否同意
    private Map<String,FirstPlayerRoom> nowPlay;//当前玩牌的人
    private int compareNum;//本场比牌人数
    private List<Integer> losePosition;//本局输掉的玩家位置
    public MyPlayerSet() {
    }
    public MyPlayerSet(FirstRooms room) {
        this.playerPos = new FirstPlayerRoom[MAX_SIZE];
        playerForAccount = new ConcurrentHashMap<>();
        playerPosForAccount = new ConcurrentHashMap<>();
        losePosition = new ArrayList<>(MAX_SIZE);
        nowPlay = new ConcurrentHashMap<>();
        this.room = room;
    }
    /**
     * 离开房间
     * @param account
     */
    public void exit(String account){
        if(playerForAccount.containsKey(account)){
            playerForAccount.remove(account);
        }
        if(playerPosForAccount.containsKey(account)){
            playerPosForAccount.remove(account);
        }
        if(playerChoiceStateMap.containsKey(account)){
            playerChoiceStateMap.remove(account);
        }
        if(nowPlay.containsKey(account)){
            nowPlay.remove(account);
        }
        if(losePosition.contains(account)){
            losePosition.remove(account);
        }
        for(int i = 0;i<playerPos.length;i++){
            if(playerPos[i] != null && playerPos[i].getPlayer().getAccount().equals(account)) {
                playerPos[i] = null;
                break;
            }
        }
    }

    public FirstRooms getRoom() {
        return room;
    }


    //设置玩家位置
    public void resetpos(FirstPlayerRoom p){
        if(playerForAccount.containsKey(p.getPlayer().getAccount()))
            new GenaryAppError(AppErrorCode.SERVER_ERR);
        int pos = getNullPosition();
        if(pos == -1)
            new GenaryAppError(AppErrorCode.SERVER_ERR);
        playerPos[pos] = p;
        p.getPlayer().setRoomPosition(pos);
        String account = p.getPlayer().getAccount();
        playerPosForAccount.putIfAbsent(account,pos);
        playerForAccount.putIfAbsent(account,p);
    }
    private int getNullPosition(){
        for(int i = 0;i<playerPos.length;i++){
            if(playerPos[i] == null)
                return i;
        }
        return -1;
    }

    public Map<String, FirstPlayerRoom> getNowPlay() {
        return nowPlay;
    }

    public void setNowPlay(Map<String, FirstPlayerRoom> nowPlay) {
        this.nowPlay = nowPlay;
    }

    public int getCompareNum() {
        return compareNum;
    }

    public void setCompareNum(int compareNum) {
        this.compareNum = compareNum;
    }
    public void addCompareNum(){
        compareNum++;
    }
    public ConcurrentHashMap<String, FirstPlayerRoom> getPlayerForAccount() {
        return playerForAccount;
    }

    public ConcurrentHashMap<String, Integer> getPlayerPosForAccount() {
        return playerPosForAccount;
    }

    /**
     * 获取指定玩家的位置
     * @param account
     * @return
     */
    public int getPlayerPos(String account){
        return playerPosForAccount.getOrDefault(account,-1);
    }
    public List<FirstPlayerRoom> getAllPlayer(){
        return new ArrayList<>(playerForAccount.values());
    }

    /**
     * 获取除指定玩家除外的房间人
     * @return
     */
    public List<FirstPlayerRoom> getNotAccountPlayer(String account){
        ArrayList<FirstPlayerRoom> arr = new ArrayList<>(playerForAccount.size());
        for(Map.Entry<String,FirstPlayerRoom> e:playerForAccount.entrySet()){
            if(!e.getKey().equals(account)){
                arr.add(e.getValue());
            }
        }
        return arr;
    }

    /**
     * 获取房间人数
     * @return
     */
    public int getRoomPlayerNum(){
        return playerForAccount.size();
    }

    /**
     * 输掉的玩家的位置
     * @param account
     */
    public void losePlayer(String account){
        nowPlay.remove(account);//从当前玩牌玩家集合中一处
        //添加到输牌的玩家集合中
        losePosition.add(playerPosForAccount.getOrDefault(account,-1));
    }
    public boolean isLose(int pos){
        return losePosition.contains(pos);
    }
    public int loseNum(){
        return losePosition.size();
    }
    /**
     * 获取指定位置下一有人的位置
     * @param i
     * @return
     */
    public String getNextPositionAccount(int i){
        int tem = i;
        ++i;
        String acount = null;
        if(i >= playerPos.length)
            i = 0;
        int j = 0;
        while (j<MAX_SIZE){
            if(i >= playerPos.length)
                i = 0;
            if(playerPos[i] != null && i != tem) {
                acount = playerPos[i].getPlayer().getAccount();
                if(playerChoiceStateMap.getOrDefault(acount,false) && !losePosition.contains(i)) {//这个人是已经准备的,平且没有输牌
                    break;
                }
            }
            i++;
            j++;
        }
        return acount;
    }

    /**
     * 根据位置找到玩家
     * @param account
     * @return
     */
    public FirstPlayerRoom getPlayerForPosition(String account){
        for(int j = 0;j<playerPos.length;j++){
            if(playerPos[j] != null && playerPos[j].getPlayer().getAccount().equals(account)) {
                return playerPos[j];
            }
        }
        return null;
    }
    public FirstPlayerRoom getPlayerForPosition(int position){
        return playerPos[position];
    }
    /**
     * 添加一个玩家的选择
     * @param playerId
     * @param bor
     */
    public void addPlayerChoice(String playerId,boolean bor){
        this.playerChoiceStateMap.put(playerId, bor);
    }
    /**
     * 获取指定选择的人数
     * @param choice
     * @return
     */
    public int getChoiceNum(boolean choice){
        int num = 0;
        for (Map.Entry<String, Boolean> entry : this.playerChoiceStateMap.entrySet()) {
            if(choice == entry.getValue()){
                num ++;
            }
        }
        return num;
    }

    /**
     * 当前在玩的人数
     * @return
     */
    public int playNum(){
        return nowPlay.size();
    }
    /**
     * 获取指定玩家的选择
     * @param playerId
     * @return
     */
    public boolean getPlayerChoice(String playerId){
        if(playerChoiceStateMap.containsKey(playerId))
            return this.playerChoiceStateMap.get(playerId);
        return false;
    }

    /**
     * 获取当前玩家的选择
     * @return
     */
    public Map<String,Boolean> getPlayerChoices(){
        return new HashMap<>(this.playerChoiceStateMap);
    }
    public FirstPlayerRoom getPlayerForAccount(String account){
        return playerForAccount.getOrDefault(account,null);
    }
    /**
     * 获取房间所有已经准备的玩家
     * @return
     */
    public List<FirstPlayerRoom> getReadyPlayer(boolean state){
        List<FirstPlayerRoom> list = new ArrayList<>(5);
        for(Map.Entry<String,Boolean> e: playerChoiceStateMap.entrySet()){
            if(e.getValue() == state)
                list.add(playerForAccount.get(e.getKey()));
        }
        return list;
    }

    /**
     * 当前在玩的玩家
     * @param list
     */
    public void nowPlayPlayer(List<FirstPlayerRoom> list){
        Map<String,FirstPlayerRoom> m = new HashMap<>(list.size());
        for(FirstPlayerRoom f:list){
            m.put(f.getPlayer().getAccount(),f);
        }
        nowPlay = m;
    }

    /**
     * 当前所有在玩的玩家
     */
    public List<FirstPlayerRoom> nowAllPayPlayer(){
        return new ArrayList<>(nowPlay.values());
    }
    /**
     * 查看某个玩家是否在玩
     * @param account
     * @return
     */
    public boolean isPlayForAccount(String account){
        return nowPlay.containsKey(account);
    }
    /**
     * 房间本局结束
     */
    public void end(){
        playerChoiceStateMap.clear();//玩家的选择状态 key:玩家ID  value:是否同意
        nowPlay.clear();//当前玩牌的人
        compareNum = 0;//本场比牌人数
        losePosition.clear();//本局输掉的玩家位置
    }
}
