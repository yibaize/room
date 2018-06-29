package org.zgl.rooms.always_happy.model;

import org.zgl.error.AppErrorCode;
import org.zgl.error.GenaryAppError;
import org.zgl.hall_connection.dto.RoomWeathDtos;
import org.zgl.net.builder_clazz.NotifyCode;
import org.zgl.net.http.HttpClient;
import org.zgl.net.message.ServerResponse;
import org.zgl.net.server.session.ISession;
import org.zgl.net.server.session.SessionManager;
import org.zgl.player.PlayerFactory;
import org.zgl.player.PlayerInfoDto;
import org.zgl.player.PlayerRoom;
import org.zgl.remote.HttpProxyOutboundHandler;
import org.zgl.remote.IBackHall;
import org.zgl.remote.ISystemWeathUpdate;
import org.zgl.rooms.always_happy.dto.AHBetDto;
import org.zgl.rooms.always_happy.dto.AHHistoryDto;
import org.zgl.rooms.always_happy.dto.AHResultDto;
import org.zgl.rooms.always_happy.dto.AHWeathDto;
import org.zgl.rooms.always_happy.manager.AHDealManager;
import org.zgl.rooms.card.CardType;
import org.zgl.rooms.thousands_of.dto.PositionDto;
import org.zgl.rooms.type.ProcedureType;
import org.zgl.rooms.type.RoomStateType;
import org.zgl.utils.DateUtils;
import org.zgl.utils.JsonUtils;
import org.zgl.utils.ProtostuffUtils;
import org.zgl.utils.logger.LoggerUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

public class AHGamblingParty {
    private final AHRoom room;
    private AHPlayerSet playerSet;
    private Map<Integer, AHBetModel> betMap;
    private AtomicLong allMoney;
    private AtomicLong todayGetMoney;//当天系统财富收入
    private int todayTimer;//当天系统财富收入计时器
    private long lastTimeGrantAward;//上期发放奖金
    private Queue<AHHistoryDto> historyDtos;
    private long nowNum;//当前场次
    private AHHistoryDto thisGamblingParCard;
    private Set<String> nowBetPlayerNum;
    private Map<Long, Long> beforeBet;

    public AHGamblingParty(AHRoom room) {
        this.room = room;
        this.todayGetMoney = new AtomicLong(0);
        this.playerSet = room.getPlayerSet();
        this.betMap = new ConcurrentHashMap<>();
        this.allMoney = new AtomicLong(0);
        this.historyDtos = new ConcurrentLinkedQueue<>();
        this.nowBetPlayerNum = new HashSet<>();
        this.beforeBet = new HashMap<>();
    }


    public void addBefore(long uid, long money) {
        beforeBet.putIfAbsent(uid, money);
    }

    public int getNowBetPlayerNum() {
        return nowBetPlayerNum.size();
    }

    public List<AHHistoryDto> getHistory() {
        return new ArrayList<>(historyDtos);
    }

    public long getLastTimeGrantAward() {
        return lastTimeGrantAward;
    }

    public long getAllMoney() {
        return allMoney.get();
    }

    public void bet(int betPosition, long num, PlayerRoom player, int betCount) {
        //开奖你让下注
        long m = allMoney.get();
        allMoney.set(m + num);
        if (betPosition < 0)
            new GenaryAppError(AppErrorCode.DATA_ERR);
        AHBetModel bet = betMap.get(betPosition);
        if (bet == null) {
            bet = new AHBetModel(betPosition);
            betMap.put(betPosition, bet);
        }
        bet.bet(player, betCount);
        playerSet.enter(player);
        nowBetPlayerNum.add(player.getPlayer().getAccount());
        //通知有人下注
        room.braodcast(playerSet.getPlayers(), NotifyCode.AH_ROOM_BET, new AHBetDto(getNowBetPlayerNum(), allMoney.get()));
    }

    /**
     * 上次更新的当条财富和血池
     * 如果没有变化不需要同步到数据库
     */
    private boolean isUpdateSystemWeath = false;
    public void settleAccount() {
        if (todayTimer != DateUtils.currentDay()) {
            todayTimer = DateUtils.currentDay();
            todayGetMoney.set(0);
        }
        isUpdateSystemWeath = false;
        //通知所有人开牌了
        List<PlayerRoom> ahPlayers = new ArrayList<>();
        lastTimeGrantAward = 0;
        List<AwardModel> awardModels = new ArrayList<>();
        for (AHBetModel b : betMap.values()) {
            ahPlayers.addAll(b.getAllPlayer());
            if (b.getPosition() == thisGamblingParCard.getResult()) {
                awardModels.addAll(b.awardModels((int) CardType.getType(thisGamblingParCard.getResult()).ahRate()));
                lastTimeGrantAward += b.settleAccount(CardType.getType(thisGamblingParCard.getResult()).ahRate(), ProcedureType.WIN);//赢
            } else if (b.getPosition() == thisGamblingParCard.getOddEnven()) {
                awardModels.addAll(b.awardModels(38000));
                lastTimeGrantAward += b.settleAccount(38000F, ProcedureType.WIN);//赢
            } else {
                //玩家输了就把所有的钱放到血池中去
                lastTimeGrantAward -= b.getAllMoney();
            }
        }
        todayGetMoney.addAndGet(lastTimeGrantAward);
        notifyResult();
        Map<Long, AwardModel> m = new HashMap<>();
        for (AwardModel a : awardModels) {
            if (m.containsKey(a.getUid())) {
                AwardModel awdml = m.get(a.getUid());
                long gold = awdml.getWinGold() + a.getWinGold();
                awdml.setWinGold(gold);
                m.put(a.getUid(), awdml);
            } else {
                m.put(a.getUid(), a);
            }
        }
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setId(NotifyCode.AH_ROOM_SETTLE_ACCOUNT);
        for (Map.Entry<Long, AwardModel> e : m.entrySet()) {
            //结算
            byte[] buf = ProtostuffUtils.serializer(new AHWeathDto(0, 0, e.getValue().getWinGold()));
            serverResponse.setData(buf);
            ISession session = SessionManager.getSession(e.getKey());
            if (session != null)
                session.write(serverResponse);
        }
        //本局有财富变更才更新
        //通知大厅 系统赢了多少或者输了多少 今日输赢财富 奖池 血池
        if (lastTimeGrantAward != 0) {
            ISystemWeathUpdate weathUpdate = HttpProxyOutboundHandler.getRemoteProxyObj(ISystemWeathUpdate.class);
            weathUpdate.update(room.getScenecId(), todayGetMoney.get(), 0, 0);
        }
        List<PlayerInfoDto> weathDtos = new ArrayList<>(ahPlayers.size());
        for (PlayerRoom ah : ahPlayers) {
            if (m.containsKey(ah.getPlayer().getUid())) {
                weathDtos.add(ah.getPlayer());
            }
        }
        if (weathDtos.size() > 0) {
            IBackHall backHall = HttpProxyOutboundHandler.getRemoteProxyObj(IBackHall.class);
            backHall.backHall(weathDtos);
        }
        historyDtos.offer(thisGamblingParCard);
        if (historyDtos.size() >= 14) {
            historyDtos.poll();
        }
    }
    private void shuffle() {
        thisGamblingParCard = AHDealManager.getInstance().shuffleAndDeal();
        thisGamblingParCard.setNum(nowNum);
    }

    private void notifyResult() {
        AHResultDto dto = new AHResultDto();
        dto.setCardIds(thisGamblingParCard.getCardIds());
        if (lastTimeGrantAward > 0)
            dto.setLastTimeGrantAward(lastTimeGrantAward);
        dto.setNum(nowNum);
        dto.setOddEnven(thisGamblingParCard.getOddEnven());
        dto.setResult(thisGamblingParCard.getResult());
        room.braodcast(playerSet.getPlayers(), NotifyCode.AH_ROOM_CARD_RESULT, dto);
    }

    private int timer;

    public int getTimer() {
        return timer;
    }

    public void timer() {
        timer--;
        switch (timer) {
            //通知开始
            case 30:
                SessionManager.notifyAllx(NotifyCode.AH_ROOM_START, new PositionDto(30));
                room.setRoomState(RoomStateType.START);
                break;
            case 23:
                shuffle();
                break;
            case 10:
                //不能下注了
                room.setRoomState(RoomStateType.STOP_BOTTOM);
                room.braodcast(playerSet.getPlayers(), NotifyCode.AH_ROOM_CAN_NOT_BET,  new PositionDto(10));
                break;
            case 5:
                settleAccount();
                room.setRoomState(RoomStateType.SETTLE);
                break;
            case 1:
                SessionManager.notifyAllx(NotifyCode.AH_ROOM_END,  new PositionDto(1));
                room.setRoomState(RoomStateType.END);
                //通知结束
                end();
                timer = 31;
                nowNum++;
                break;
        }
        if (timer <= 0) {
            timer = 30;
        }
    }

    public void end() {
        nowBetPlayerNum.clear();
        betMap.clear();
        allMoney.set(0);
        playerSet.end();
        beforeBet.clear();
    }
}
