package org.bql.rooms.always_happy.model;

import org.bql.error.AppErrorCode;
import org.bql.error.GenaryAppError;
import org.bql.hall_connection.dto.RoomWeathDto;
import org.bql.hall_connection.dto.RoomWeathDtos;
import org.bql.net.builder_clazz.NotifyCode;
import org.bql.net.http.HttpClient;
import org.bql.net.server.session.SessionManager;
import org.bql.player.PlayerFactory;
import org.bql.player.PlayerRoom;
import org.bql.rooms.always_happy.dto.AHBetDto;
import org.bql.rooms.always_happy.dto.AHHistoryDto;
import org.bql.rooms.always_happy.dto.AHResultDto;
import org.bql.rooms.always_happy.manager.AHDealManager;
import org.bql.rooms.card.CardType;
import org.bql.rooms.type.ProcedureType;
import org.bql.rooms.type.RoomStateType;
import org.bql.utils.DateUtils;
import org.bql.utils.JsonUtils;
import org.bql.utils.logger.LoggerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
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
    private long bloodGroove;//血池
    private Queue<AHHistoryDto> historyDtos;
    private long nowNum;//当前场次
    private AHHistoryDto thisGamblingParCard;
    private int nowBetPlayerNum;

    public AHGamblingParty(AHRoom room) {
        this.room = room;
        this.todayGetMoney = new AtomicLong(0);
        this.playerSet = room.getPlayerSet();
        this.betMap = new ConcurrentHashMap<>();
        this.allMoney = new AtomicLong(0);
        this.historyDtos = new ConcurrentLinkedQueue<>();
    }

    public void shuffle() {
        thisGamblingParCard = AHDealManager.getInstance().shuffleAndDeal();
        thisGamblingParCard.setNum(nowNum);
        historyDtos.offer(thisGamblingParCard);
        if (historyDtos.size() >= 14) {
            historyDtos.poll();
        }
    }

    public int getNowBetPlayerNum() {
        return nowBetPlayerNum;
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

    public void bet(int betPosition, long num, PlayerRoom player) {
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
        bet.bet(player, num);
        playerSet.enter(player);
        nowBetPlayerNum++;
        //通知有人下注
        room.braodcast(playerSet.getPlayers(), NotifyCode.AH_ROOM_BET, new AHBetDto(nowBetPlayerNum, allMoney.get()));
    }

    /**
     * 上次更新的当条财富和血池
     * 如果没有变化不需要同步到数据库
     */
    private long updatebloodGroove;
    public void settleAccount() {
        if (todayTimer != DateUtils.currentDay()) {
            todayTimer = DateUtils.currentDay();
            todayGetMoney.set(0);
            bloodGroove = 0;
        }
        updatebloodGroove = 0;
        lastTimeGrantAward = 0;
        //通知所有人开牌了
        List<PlayerRoom> ahPlayers = new ArrayList<>();
        for (AHBetModel b : betMap.values()) {
            ahPlayers.addAll(b.getAllPlayer());
            if (b.getPosition() == thisGamblingParCard.getResult() || b.getPosition() == thisGamblingParCard.getOddEnven()) {
                lastTimeGrantAward += b.settleAccount(CardType.getType(thisGamblingParCard.getResult()), ProcedureType.WIN);//赢
                //玩家赢了就扣取百分之五手续费
                updatebloodGroove += lastTimeGrantAward * ProcedureType.WIN.id();//扣取平台费百分之5
            } else {
                //玩家输了就把所有的钱放到血池中去
                lastTimeGrantAward += b.settleAccount(CardType.getType(thisGamblingParCard.getResult()), ProcedureType.LOSE);//输
                todayGetMoney.addAndGet(lastTimeGrantAward);
            }
        }
        bloodGroove += updatebloodGroove;
        notifyResult();
        //本局有财富变更才更新
        //通知大厅 系统赢了多少或者输了多少 今日输赢财富 奖池 血池
        if (lastTimeGrantAward > 0 || updatebloodGroove > 0)
            HttpClient.getInstance().asyncPost(NotifyCode.SYSTEM_WEATH_UPDATE, PlayerFactory.SYSTEM_PLAYER_ID + "," + room.getScenecId() + "," + todayGetMoney.get() + "," + 0 + "," + bloodGroove);
        List<RoomWeathDto> weathDtos = new ArrayList<>(ahPlayers.size());
        for (PlayerRoom ah : ahPlayers) {
            weathDtos.add(ah.getPlayer().weathDto(thisGamblingParCard.getResult(), false));
        }
        if (weathDtos.size() > 0) {
            RoomWeathDtos notifyMessage = new RoomWeathDtos(weathDtos);
            HttpClient.getInstance().asyncPost(NotifyCode.REQUEST_HALL_UPDATE_WEATH, JsonUtils.jsonSerialize(notifyMessage) + "," + PlayerFactory.SYSTEM_PLAYER_ID);
        }

    }

    private void notifyResult() {
        AHResultDto dto = new AHResultDto();
        dto.setCardIds(thisGamblingParCard.getCardIds());
        dto.setLastTimeGrantAward(lastTimeGrantAward);
        dto.setNum(nowNum);
        dto.setOddEnven(thisGamblingParCard.getOddEnven());
        dto.setResult(thisGamblingParCard.getResult());
        LoggerUtils.getLogicLog().info(Thread.currentThread().getName()+" : "+playerSet);
        room.braodcast(playerSet.getPlayers(), NotifyCode.AH_ROOM_CARD_RESULT, dto);
    }

    private int timer;

    public int getTimer() {
        return timer;
    }

    public void timer() {
        timer++;
        switch (timer) {
            //通知开始
            case 1:
                SessionManager.notifyAllx(NotifyCode.AH_ROOM_START, null);
                room.setRoomState(RoomStateType.START);
                break;
            case 7:
                shuffle();
                break;
            case 22:
                //不能下注了
                room.setRoomState(RoomStateType.STOP_BOTTOM);
                room.braodcast(playerSet.getPlayers(), NotifyCode.AH_ROOM_CAN_NOT_BET, null);
                break;
            case 25:
                settleAccount();
                room.setRoomState(RoomStateType.SETTLE);
                break;
            case 30:
                SessionManager.notifyAllx(NotifyCode.AH_ROOM_END, null);
                room.setRoomState(RoomStateType.END);
                //通知结束
                end();
                timer = 0;
                nowNum++;
                break;
        }
        if (timer > 40) {
            timer = 0;
        }
    }

    public void end() {
        nowBetPlayerNum = 0;
        betMap.clear();
        allMoney.set(0);
        playerSet.end();
    }
}
