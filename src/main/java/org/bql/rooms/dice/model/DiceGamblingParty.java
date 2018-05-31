package org.bql.rooms.dice.model;

import org.bql.rooms.card.CardManager;
import org.bql.rooms.dice.manager.DiceManager;
import org.bql.utils.DateUtils;
import org.bql.utils.RandomUtils;
import org.bql.utils.weightRandom.WeightRandom;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DiceGamblingParty {
    private final DiceRoom room;
    private final DicePlayerSet playerSet;
    private final CardManager cardManager;
    private Map<Integer,DiceBet> bets;//下注位置
    private Queue<DiceDto> historyQueue;//历史记录
    private long battleCount = 0;//场次
    private long startTime;
    private long endTime;
    private long allMoney;
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
        int random = RandomUtils.randomSection(count-1,2);
        DiceDto diceDto = new DiceDto(count - random,random,battleCount++);
        if(historyQueue.size() >= 10){
            historyQueue.poll();
        }
        historyQueue.offer(diceDto);
    }
    private static final Object BET_LOCK = new Object();
    public void bet(DicePlayer player,long num,int position){
        synchronized (BET_LOCK) {
            DiceBet d = bets.getOrDefault(position, null);
            if (d == null) {
                d = new DiceBet();
            }
            d.bet(player, num);
            allMoney += num;
        }
    }
    public long getAllMoney(){
        return allMoney;
    }
}
