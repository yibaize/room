package org.bql.rooms.thousands_of.model;

import org.bql.hall_connection.dto.RoomWeathDto;
import org.bql.hall_connection.dto.RoomWeathDtos;
import org.bql.net.builder_clazz.NotifyCode;
import org.bql.net.http.HttpClient;
import org.bql.player.PlayerFactory;
import org.bql.player.PlayerInfoDto;
import org.bql.rooms.card.CardDataTable;
import org.bql.rooms.card.CardManager;
import org.bql.rooms.card.CardType;
import org.bql.rooms.thousands_of.dto.*;
import org.bql.rooms.three_cards.three_cards_1.model.HandCard;
import org.bql.rooms.type.ProcedureType;
import org.bql.rooms.type.RoomStateType;
import org.bql.utils.DateUtils;
import org.bql.utils.JsonUtils;
import org.bql.utils.logger.LoggerUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

//10,20,30,40
//10
//9,18,27,36
//90
public class TOGamblingParty {
    private TORoom room;
    private TOPlayerSet playerSet;
    private CardManager cardManager;
    private Map<Integer, HandCard> handCardMap;//手牌
    private Map<Integer, TOBet> bets;//下注位置
    private Queue<HistoryDto> historyQueue;//历史记录
    private long battleCount;//场次
    private AtomicLong allMoney;
    private long jackpot;
    private long bloodGroove;
    private AtomicLong todayMoney;//万人场今天出金币情况
    private int todayTime;//万人场金币记时器，如果不是今天就清空并同步到大厅数据

    public TOGamblingParty() {
    }

    public TOGamblingParty(TORoom room) {
        this.room = room;
        this.playerSet = room.getPlayerSet();
        this.cardManager = room.getCardManager();
        this.handCardMap = new HashMap<>(5);
        this.bets = new ConcurrentHashMap<>(4);
        this.allMoney = new AtomicLong(0);
        this.historyQueue = new ConcurrentLinkedQueue<>();
        this.todayMoney = new AtomicLong(0);
    }

    /**
     * 开局一条狗，发牌全靠吹
     *
     * @return
     */

    public boolean startBattle() {
        room.setRoomState(RoomStateType.START);
        //发牌洗牌比牌
        shuffCard();
        //通知所有人发牌了；本局开始了
        room.broadcast(playerSet.getAllPlayer(), NotifyCode.ROOM_START, null);
        return false;
    }

    /**
     * 随机发牌洗牌比牌
     */
    private void shuffCard() {
        List<CardDataTable> cardDataTables = cardManager.shuff(15, 1);
        HandCard bankerHandCard = null;
        int temp = 0;
        for (int i = 0; i < 5; i++) {
            Integer[] cardIds = new Integer[3];
            Integer[] cardFaces = new Integer[3];
            HandCard handCard = new HandCard();
            for (int j = 0; j < 3; j++) {
                cardIds[j] = cardDataTables.get(temp).getId();
                cardFaces[j] = cardDataTables.get(temp).getFace();
                temp++;
            }
            handCard.setCardIds(cardIds);
            handCard.setCardFaces(cardFaces);
            if (bankerHandCard == null) {
                bankerHandCard = handCard;//庄家手牌
            }
            //整理牌型
            cardManager.getCardType(handCard);
            if (i != 0) {
                //和庄家比输赢了
                cardManager.compareCard(handCard, bankerHandCard);
            }
            handCard.setPosition(i);
            handCardMap.putIfAbsent(i, handCard);
        }
    }

    /**
     * 下注
     *
     * @param betPosition 下注的位置 : 注 位置从1开始 1-4
     * @param num 下注的数量
     * @param player 下注的玩家
     */
    private static final Object BET_LOCK = new Object();

    public void bet(int betPosition, long num, TOPlayer player) {
        synchronized (BET_LOCK) {
            TOBet b = bets.getOrDefault(betPosition, null);
            if (b == null) {
                b = new TOBet();
            }
            b.setPosition(betPosition);
            b.bet(player, num);
            bets.put(betPosition, b);
            player.addBet(num);
            PlayerInfoDto infoDto = player.getPlayer();
            room.broadcast(playerSet.getAllPlayer(), NotifyCode.NOTIFY_BET_WEATH_UPDATE, new BetUpdateDto(infoDto.getAccount(), infoDto.getGold(), num, betPosition));
        }
    }

    /**
     * 当前奖池数量
     *
     * @return
     */
    public long getJackpot() {
        return jackpot;
    }

    /**
     * 结算
     */
    private boolean isSyncSystemWeath = false;
    public void settleAccounts() {
        if (todayTime != DateUtils.currentDay()) {
            todayTime = DateUtils.currentDay();
            todayMoney.set(0);
            jackpot = 0;
            bloodGroove = 0;
        }
        List<Boolean> historyResult = new ArrayList<>(4);
        List<TOPlayer> toPlayers = new ArrayList<>();
        //结算金币
        Set<BetUpdateDto> ranking = new HashSet<>();

        for (Map.Entry<Integer, HandCard> e : handCardMap.entrySet()) {
            if (e.getKey() == 0)
                continue;
            int position = e.getKey();
            HandCard card = e.getValue();
            historyResult.add(card.isCompareResult());
            TOBet b = bets.getOrDefault(position, null);
            if (b == null)
                continue;
            toPlayers.addAll(b.getAllPlayer());
            long m = 0;
            if (card.isCompareResult()) {
                m = b.settleAccounts(CardType.getType(card.getCardType()), ProcedureType.WIN);//位置结算
                ranking.addAll(b.getRanking(CardType.getType(card.getCardType())));
            } else if (!card.isCompareResult()) {
                m = b.settleAccounts(CardType.getType(card.getCardType()), ProcedureType.LOSE);//位置结算
                jackpot += Math.abs(m) * ProcedureType.LOSE.id();
                bloodGroove += Math.abs(m) * 0.04;
                m -= m * ProcedureType.WIN.id();
            }
            isSyncSystemWeath = true;
            allMoney.addAndGet(m);
        }
        //结算庄家
        TOPlayer banker = playerSet.getBanker();
        long alm = allMoney.get();
        if(alm != 0)
            isSyncSystemWeath = true;
        LoggerUtils.getLogicLog().info(Thread.currentThread().getName()+" : "+alm);
        if (banker != null) {
            if (alm < 0) {
                jackpot += Math.abs(alm) * ProcedureType.LOSE.id();
                bloodGroove += Math.abs(alm) * 0.04;
            }
            banker.getPlayer().insertGold(alm);
            alm = banker.getPlayer().getGold();
            //有庄家 通知所有人庄家财富变了
        } else {
            todayMoney.addAndGet(alm);

        }
        room.broadcast(playerSet.getAllPlayer(), NotifyCode.NOTIFY_POSITION_BANKER_WEATH_UPDATE, new PlayerWeathUpdateDto(alm));
        room.setRoomState(RoomStateType.SETTLE);//设置房间状态
        battleCount++;
        //历史记录
        LoggerUtils.getLogicLog().info(Thread.currentThread().getName()+" : "+historyResult);
        HistoryDto history = new HistoryDto(battleCount, historyResult.get(0), historyResult.get(1), historyResult.get(2), historyResult.get(3));
        if (historyResult.size() > 12) {
            historyQueue.poll();
        }
        historyQueue.offer(history);

        //添加与大厅通讯数据
        List<RoomWeathDto> weathDtos = new ArrayList<>(toPlayers.size());
        for (TOPlayer t : toPlayers) {
            weathDtos.add(t.getPlayer().weathDto(1, false));
        }
        //同步大厅系统财富存储记录 今日输赢财富 奖池 血池
        if (isSyncSystemWeath)
            HttpClient.getInstance().asyncPost(NotifyCode.SYSTEM_WEATH_UPDATE, PlayerFactory.SYSTEM_PLAYER_ID + "," + room.getScenecId() + "," + todayMoney.get() + "," + jackpot + "," + bloodGroove);
        if (weathDtos.size() > 0) {//同步大厅玩家财富变更
            RoomWeathDtos notifyMessage = new RoomWeathDtos(weathDtos);
            HttpClient.getInstance().asyncPost(NotifyCode.REQUEST_HALL_UPDATE_WEATH, JsonUtils.jsonSerialize(notifyMessage) + "," + PlayerFactory.SYSTEM_PLAYER_ID);
        }
        ArrayList<BetUpdateDto> ll = new ArrayList<>(ranking);
        Collections.sort(ll);
        int size = ranking.size() >= 3 ? 3 : ranking.size();
        //发送本局结束之后赢钱最多的三个人和四个位置的输赢情况
        room.broadcast(playerSet.getAllPlayer(), NotifyCode.NOTIFY_END_RANKING, new TOSettleRanking(ll.subList(0, size)));
    }

    /**
     * 获取历史记录
     *
     * @return
     */
    public List<HistoryDto> historyDtos() {
        return new ArrayList<>(historyQueue);
    }

    /**
     * 通知玩家手牌
     */
    public void notifyHandCard() {
        //通知手牌来了嗨
        List<TOCardsDto> cardsDtos = new ArrayList<>(5);
        for (Map.Entry<Integer, HandCard> e : handCardMap.entrySet()) {
            cardsDtos.add(new TOCardsDto().dto(e.getKey(), e.getValue()));
        }
        for (TOCardsDto t : cardsDtos) {
            LoggerUtils.getLogicLog().info(Thread.currentThread().getName()+" : "+t);
        }
        room.setRoomState(RoomStateType.DEAL);
        room.broadcast(playerSet.getAllPlayer(), NotifyCode.NOTIFY_HAND_CARD, new TOCardsDtos(cardsDtos));
    }

    public int getResidueTime() {
        return roomTimer.get();
    }

    /**
     * 有玩家下线
     *
     * @param player
     */
    public void exit(TOPlayer player) {
        for (TOBet tb : bets.values()) {
            tb.exit(player);
        }
    }

    /**
     * 结束牌局
     */
    private void end() {
        handCardMap.clear();
        bets.clear();
        allMoney.set(0);
        playerSet.end();
    }

    private AtomicInteger roomTimer = new AtomicInteger(0);

    /**
     * 房间定时器
     */
    public void timer() {
        try {
            //开始，设置房间状态为不能下注，通知发牌
            switch (roomTimer.get()) {
                case 1:
                    room.setRoomState(RoomStateType.READY);
                    room.broadcast(playerSet.getAllPlayer(), NotifyCode.TO_ROOM_START, null);
                    break;
                case 5:
                    startBattle();
                    break;
                case 10:
                    notifyHandCard();
                    break;
                case 11:
                    settleAccounts();
                    break;
                case 12:
                    room.end();
                    end();
                    roomTimer.set(0);
                    break;
            }
            if (roomTimer.get() >= 12) {
                roomTimer.set(0);
            }
            roomTimer.getAndIncrement();
        } catch (Exception e) {
            LoggerUtils.getLogicLog().info(roomTimer, e);
        }
        //通知比牌并返回结果
        //通知本场结束 设置房间状态为下注状态
    }
}
