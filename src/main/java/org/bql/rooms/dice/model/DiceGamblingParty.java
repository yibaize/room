package org.bql.rooms.dice.model;

import org.bql.rooms.card.CardManager;
import org.bql.rooms.dice.dto.DiceCountDto;
import org.bql.rooms.dice.manager.DiceManager;
import org.bql.rooms.type.DiceCountType;
import org.bql.rooms.type.ProcedureType;
import org.bql.rooms.type.RoomStateType;
import org.bql.utils.DateUtils;
import org.bql.utils.RandomUtils;
import org.bql.utils.weightRandom.WeightRandom;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DiceGamblingParty {
    private final DiceRoom room;
    private final DicePlayerSet playerSet;
    private final CardManager cardManager;
    private Map<Integer,DiceBet> bets;//下注位置
    private Queue<DiceCountDto> historyQueue;//历史记录
    private long battleCount = 0;//场次
    private long startTime;
    private long endTime;
    private long allMoney;
    private DiceCountType thisTimeCount;//当局点数
    private Set<String> betPlayerNum;//下注人数
    public DiceGamblingParty(DiceRoom room) {
        this.room = room;
        playerSet = room.getPlayerSet();
        cardManager = room.getCardManager();
        startTime = DateUtils.currentTime();
        endTime = DateUtils.getFutureTimeMillis(30);
        historyQueue = new ConcurrentLinkedQueue<>();
    }
    public void start(){
        //权重随机生成点数
        int count = WeightRandom.awardPosition(DiceManager.getInstance().getDiceDate());
        thisTimeCount = DiceCountType.getDiceType(count);
        int random = RandomUtils.getRandom(count-1,2);
        DiceCountDto diceDto = new DiceCountDto(count - random,random,battleCount++);
        if(historyQueue.size() >= 15){
            historyQueue.poll();
        }
        historyQueue.offer(diceDto);
    }
    private static final Object BET_LOCK = new Object();
    public void bet(DicePlayer player,long num,int position){
        synchronized (BET_LOCK) {
            betPlayerNum.add(player.getPlayer().getAccount());
            allMoney += num;
            DiceBet d = bets.getOrDefault(position, null);
            if (d == null) {
                d = new DiceBet();
                d.setPosition(position);
                bets.put(position,d);
            }
            d.bet(player, num);
            //TODO 通知有人下注 下了多少
        }
    }

    public int getBetPlayerNum() {
        return betPlayerNum.size();
    }

    public void settleAccounts(){
        for(DiceBet db : bets.values()){
            if(db.getPosition() == thisTimeCount.getCount() || db.getPosition() == thisTimeCount.getSize()){
                //赢
                 db.settleAccounts(thisTimeCount,ProcedureType.WIN);
            }else {
                //输
                db.settleAccounts(thisTimeCount,ProcedureType.LOSE);
            }
        }
    }
    public long getAllMoney(){
        return allMoney;
    }
    public void clearPlayerBet(DicePlayer player){
        for (DiceBet db:bets.values()){
            db.clearPlayerBet(player);
        }
    }
    public List<DiceCountDto> getHistory(){
        return new ArrayList<>(historyQueue);
    }
    private int residueTime;
    public int getResidueTime(){
        return residueTime;
    }
    public void timer(){
        residueTime++;
        switch (residueTime){
            case 1:
                room.setRoomState(RoomStateType.READY);
                //通知开局
                break;
            case 10:
//                start();
                room.setRoomState(RoomStateType.START);
                //通知停止下注 要骰子
                break;
            case 15:
                //通知结算
                room.setRoomState(RoomStateType.END);
                break;
            case 17:
                room.setRoomState(RoomStateType.END);
                //通知本局结束
                break;
        }
        if(residueTime > 17){
            residueTime = 0;
        }
    }
    private void clear(){
        betPlayerNum.clear();
    }
}
