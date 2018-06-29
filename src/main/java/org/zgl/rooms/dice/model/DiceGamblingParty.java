package org.zgl.rooms.dice.model;

import org.zgl.net.builder_clazz.NotifyCode;
import org.zgl.player.PlayerInfoDto;
import org.zgl.rooms.dice.data.DiceDataTable;
import org.zgl.rooms.dice.dto.DiceCountDto;
import org.zgl.rooms.dice.dto.DiceSettleRanking;
import org.zgl.rooms.dice.manager.DiceManager;
import org.zgl.rooms.thousands_of.dto.BetUpdateDto;
import org.zgl.rooms.type.DiceCountType;
import org.zgl.rooms.type.ProcedureType;
import org.zgl.rooms.type.RoomStateType;
import org.zgl.rooms.type.RoundDiceType;
import org.zgl.utils.logger.LoggerUtils;
import org.zgl.utils.weightRandom.WeightRandom;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DiceGamblingParty {
    private final DiceRoom room;
    private final DicePlayerSet playerSet;
    private Map<Integer, DiceBet> bets;//下注位置
    private Queue<DiceCountDto> historyQueue;//历史记录
    private long battleCount = 0;//场次
    private long allMoney;
    private DiceCountType thisTimeCount;//当局点数
    private RoundDiceType isRound;//是否是围骰
    private Set<String> betPlayerNum;//下注人数
    private long systemWeath;
    private DiceCountDto diceCountDto;
    private Map<Long, Long> beforeBet;//参与下注的所有人下注之前的人uid对应金币,需要排行的时候

    public DiceGamblingParty(DiceRoom room) {
        this.room = room;
        playerSet = room.getPlayerSet();
        historyQueue = new ConcurrentLinkedQueue<>();
        betPlayerNum = new HashSet<>();
        bets = new HashMap<>();
        isRound = RoundDiceType.NONE;
        this.beforeBet = new HashMap<>();
        diceCountDto = new DiceCountDto();
    }

    public void start() {
        //权重随机生成点数
        int id = WeightRandom.awardPosition(DiceManager.getInstance().getDiceDate());
        DiceDataTable diceDataTable = DiceDataTable.get(id);
        DiceCountDto splitCount = diceDataTable.getSplitCount();
        int one = splitCount.getOne();
        int two = splitCount.getTwo();
        //围骰
        if (one == splitCount.getTwo()) {
            if (one == 1 || one == 6) {
                isRound = RoundDiceType.get(16);
            } else {
                isRound = RoundDiceType.get(diceDataTable.getCount());
            }
        }
        diceCountDto = new DiceCountDto(one, two, battleCount++);
        thisTimeCount = DiceCountType.getDiceType(diceDataTable.getCount());
    }
    public void addBeforeBet(long uid,long gold){
        beforeBet.putIfAbsent(uid,gold);
    }
    public void bet(DicePlayer player, long num, int position) {
        String account = player.getPlayer().getAccount();
        betPlayerNum.add(account);
        allMoney += num;
        DiceBet d = bets.getOrDefault(position, null);
        if (d == null) {
            d = new DiceBet();
            d.setPosition(position);
            bets.put(position, d);
        }
        d.bet(player, num);
        BetUpdateDto betUpdateDto = new BetUpdateDto(account, allMoney, num, position);
        betUpdateDto.setBetPlayerNum(betPlayerNum.size());
        player.addBet(num);
        room.broadcast(playerSet.getAllPlayer(), NotifyCode.DICE_ROOM_PLAYER_BET, betUpdateDto);
    }

    public int getBetPlayerNum() {
        return betPlayerNum.size();
    }

    public void settleAccounts() {
        for (DiceBet db : bets.values()) {
            if (db.getPosition() == thisTimeCount.getCount()) {
                //赢
                allMoney -= db.settleAccounts(thisTimeCount.getRate(), ProcedureType.WIN);
            } else if (db.getPosition() == thisTimeCount.getSize() && isRound.id() != db.getPosition()) {
                //赢
                allMoney -= db.settleAccounts(1, ProcedureType.WIN);
            } else if (isRound.id() == db.getPosition()) {
                //赢
                allMoney -= db.settleAccounts(isRound.rate(), ProcedureType.WIN);
            } else {
                db.loseSettle();
            }
        }
        systemWeath += allMoney;
        isRound = RoundDiceType.NONE;
        //排行
        ArrayList<BetUpdateDto> ll = new ArrayList<>();
        List<DicePlayer> allPlayer = playerSet.getAllPlayer();
        for(DicePlayer d:allPlayer){
            PlayerInfoDto dto = d.getPlayer();
            if(beforeBet.containsKey(dto.getUid())){
                LoggerUtils.getLogicLog().info("前："+beforeBet.get(dto.getUid()) + "后："+dto.getGold() + "差值:"+(dto.getGold() - beforeBet.get(dto.getUid())));
                if(dto.getGold() > beforeBet.get(dto.getUid())){
                    ll.add(new BetUpdateDto(dto.getUsername(),dto.getGold(), dto.getGold()-beforeBet.get(dto.getUid()), -1));
                }
            }
        }
        Collections.sort(ll);
        int size = ll.size() >= 3 ? 3 : ll.size();

        //发送本局结束之后赢钱最多的三个人和四个位置的输赢情况
        room.broadcast(playerSet.getAllPlayer(), NotifyCode.DICE_ROOM_END, new DiceSettleRanking(diceCountDto.getOne(), diceCountDto.getTwo(), ll.subList(0, size)));
        //历史记录
        if (historyQueue.size() >= 10) {
            historyQueue.poll();
        }
        historyQueue.offer(diceCountDto);
    }

    public long getAllMoney() {
        return allMoney;
    }

    public void clearPlayerBet(DicePlayer player) {
        long money = 0;
        for (DiceBet db : bets.values()) {
            money += db.clearPlayerBet(player);
        }
        allMoney -= money;
        betPlayerNum.remove(player.getPlayer().getAccount());
        BetUpdateDto betUpdateDto = new BetUpdateDto(player.getPlayer().getAccount(), allMoney, money, 0);
        betUpdateDto.setBetPlayerNum(betPlayerNum.size());
        player.addBet(money);
        room.broadcast(playerSet.getAllPlayer(), NotifyCode.DICE_ROOM_PLAYER_BET, betUpdateDto);
    }

    public List<DiceCountDto> getHistory() {
        return new ArrayList<>(historyQueue);
    }

    private int residueTime;

    public int getResidueTime() {
        return residueTime;
    }

    public void timer() {
        residueTime++;
        switch (residueTime) {
            case 1:
                room.setRoomState(RoomStateType.READY);
                room.broadcast(playerSet.getAllPlayer(), NotifyCode.TO_ROOM_START, null);
                //通知开局
                break;
            case 25:
                room.setRoomState(RoomStateType.START);
                room.broadcast(playerSet.getAllPlayer(), NotifyCode.DICE_ROOM_NET_BET, null);
                //通知停止下注 要骰子
                break;
            case 28:
                start();
                settleAccounts();
                //通知结算
                room.setRoomState(RoomStateType.END);
                break;
            case 35:
                room.setRoomState(RoomStateType.END);
                room.broadcast(playerSet.getAllPlayer(), NotifyCode.TO_ROOM_END, null);
                clear();
                //通知本局结束
                break;
        }
        if (residueTime > 35) {
            residueTime = 0;
        }
    }

    private void clear() {
        allMoney = 0;
        beforeBet.clear();
        betPlayerNum.clear();
        playerSet.end();
    }
}
