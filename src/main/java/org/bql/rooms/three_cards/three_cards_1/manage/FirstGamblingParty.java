package org.bql.rooms.three_cards.three_cards_1.manage;

import org.bql.error.AppErrorCode;
import org.bql.error.GenaryAppError;
import org.bql.hall_connection.dto.RoomWeathDto;
import org.bql.hall_connection.dto.RoomWeathDtos;
import org.bql.net.builder_clazz.NotifyCode;
import org.bql.net.client.GameClient;
import org.bql.net.http.HttpClient;
import org.bql.net.message.ClientRequest;
import org.bql.net.message.ClientResponse;
import org.bql.net.message.Msg;
import org.bql.player.PlayerFactory;
import org.bql.rooms.card.CardDataTable;
import org.bql.rooms.card.CardManager;
import org.bql.rooms.three_cards.three_cards_1.dto.CompareCardResultDto;
import org.bql.rooms.three_cards.three_cards_1.dto.CompareCardResultDtos;
import org.bql.rooms.three_cards.three_cards_1.dto.FirstRoomStartDto;
import org.bql.rooms.three_cards.three_cards_1.dto.RoomPlayerAccountDto;
import org.bql.rooms.three_cards.three_cards_1.model.ForbidCompareModel;
import org.bql.rooms.three_cards.three_cards_1.model.HandCard;
import org.bql.rooms.type.Chip;
import org.bql.rooms.type.RoomStateType;
import org.bql.utils.ArrayUtils;
import org.bql.utils.DateUtils;
import org.bql.utils.JsonUtils;
import org.bql.utils.ProtostuffUtils;
import org.bql.utils.logger.LoggerUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FirstGamblingParty {
    public static final int MULTIPLE = 2;//看牌后翻的倍数
    private static final long OUT_TIME = 5000;//有超过两个人准备之后最多五秒后房间开局
    private static final long END_OUT_TIME = 15000;//每次考虑下注最长的时间，如果超过视为弃牌
    private List<CardDataTable> exchangeCard;//换牌是使用的集合
    private Map<String, List<ForbidCompareModel>> forbidCompareModelMap;//禁比
    private final FirstRooms myRoom;
    private final MyPlayerSet playerSet;
    private final CardManager cardManager;
    private AtomicLong startTime;
    private AtomicInteger winPosition;//上把赢钱的位置
    private AtomicInteger nowBottomPos;//当前下注房间位置
    private AtomicInteger nowBottomChip;//当前筹码位置
    private AtomicInteger oparetionCont;//操作轮数
    private AtomicInteger oparetionPlayerNum;//操作人数
    private AtomicBoolean betState;//压注状态，是否已经开启全压模式
    private FirstPlayerRoom betAllPlayer;//上一个全压的玩家
    public FirstGamblingParty(FirstRooms myRoom) {
        this.myRoom = myRoom;
        this.cardManager = myRoom.getCardManager();
        this.playerSet = myRoom.getPlayerSet();
        this.winPosition = new AtomicInteger(0);
        this.nowBottomPos = new AtomicInteger(0);
        this.startTime = new AtomicLong(0);
        this.nowBottomChip = new AtomicInteger(0);
        this.exchangeCard = new ArrayList<>();
        this.forbidCompareModelMap = new ConcurrentHashMap<>();
        this.oparetionCont = new AtomicInteger(0);
        this.oparetionPlayerNum = new AtomicInteger(0);
        this.betState = new AtomicBoolean(false);
    }

    public void setStartTime(long startTime) {
        this.startTime.set(startTime);
    }

    public AtomicInteger getWinPosition() {
        return winPosition;
    }

    public void setWinPosition(int winPosition) {
        this.winPosition.set(winPosition);
    }

    public AtomicInteger getNowBottomPos() {
        return nowBottomPos;
    }

    public void setNowBottomPos(AtomicInteger nowBottomPos) {
        this.nowBottomPos = nowBottomPos;
    }

    /**
     * 开局一条狗，发牌全靠吹
     *
     * @return
     */
    private boolean startBattle() {
        List<FirstPlayerRoom> players = playerSet.getReadyPlayer(true);
        int chip = Chip.FIRST_BOTTOM_NUM[nowBottomChip.get()];
        for (FirstPlayerRoom p : players) {
            //减底注
            p.getPlayer().reduceGold(chip);
            myRoom.addAllMoney(chip);
        }

        //发牌洗牌
        myRoom.setRoomState(RoomStateType.START);
        myRoom.getPlayerSet().nowPlayPlayer(players);
        //发牌洗牌
        shuffCard(players);
        String nextPositionAccount = playerSet.getNextPositionAccount(winPosition.get());//下个玩家开始计时
        nowBottomPos.set(playerSet.getPlayerPos(nextPositionAccount));
        setStartTime();
        myRoom.broadcast(playerSet.getAllPlayer(), NotifyCode.ROOM_START, new FirstRoomStartDto(nextPositionAccount));
        LoggerUtils.getLogicLog().info("现在下注位置是--->开始" + nowBottomPos.get());
        return false;
    }

    public AtomicInteger getNowBottomChip() {
        return nowBottomChip;
    }

    public void setNowBottomChip(AtomicInteger nowBottomChip) {
        this.nowBottomChip = nowBottomChip;
    }

    /**
     * 发牌洗牌
     */
    private void shuffCard(List<FirstPlayerRoom> players) {
        int playerNum = players.size();
        int scenesType = 1;
        if (myRoom.getScenecId() > 5)
            scenesType = 2;
        exchangeCard = cardManager.getCardDataTables();
        List<CardDataTable> cardDataTables = cardManager.shuff(playerNum * 3, scenesType);
        //移除已经发了的的牌
        shuffExchange(cardDataTables);
        for (int i = 0; i < playerNum; i++) {
            Integer[] cardIds = new Integer[3];
            Integer[] cardFaces = new Integer[3];
            HandCard handCard = new HandCard();
            int temp = 0;
            for (int j = 0; j < 3; j++) {
                cardIds[j] = cardDataTables.get(temp).getId();
                cardFaces[j] = cardDataTables.get(temp).getFace();
                temp++;
            }
            handCard.setCardIds(cardIds);
            handCard.setCardFaces(cardFaces);
            //整理牌型
            cardManager.getCardType(handCard);
            players.get(i).setHandCard(handCard);
        }
    }

    public List<CardDataTable> getExchangeCard() {
        return exchangeCard;
    }

    /**
     * 发牌的时候要预留剩余的牌提供换牌是使用
     */
    private void shuffExchange(List<CardDataTable> c) {
        for (int i = 0; i < c.size(); i++) {
            if (exchangeCard.contains(c)) {
                exchangeCard.remove(i);
            }
        }
    }

    /**
     * 超时下注视为弃牌
     *
     * @return
     */
    public boolean isBottomTimeOut() {
        return timeDifference() > END_OUT_TIME;
    }

    /**
     * 获取时间差
     *
     * @return
     */
    public long timeDifference() {
        return System.currentTimeMillis() - startTime.get();
    }

    public long getStartTime() {
        return startTime.get();
    }

    public void setStartTime() {
        this.startTime.set(System.currentTimeMillis());
    }

    public void addForbidCompare(String selfAccount, String targetAccount) {
        List<ForbidCompareModel> list = forbidCompareModelMap.get(selfAccount);
        if (list == null) {
            list = new ArrayList<>();
            forbidCompareModelMap.put(selfAccount, list);
        } else {
            for (ForbidCompareModel ff : list) {
                if (ff.getAccount().equals(targetAccount)) {
                    new GenaryAppError(AppErrorCode.PLAYER_IS_FORBID);
                }
            }
        }
        ForbidCompareModel f = new ForbidCompareModel(targetAccount, 0);
        list.add(f);
    }

    public boolean isForbidCompare(String selfAccount, String targetAccount) {
        List<ForbidCompareModel> list = forbidCompareModelMap.get(selfAccount);
        if (list == null) {
            return false;
        } else {
            for (ForbidCompareModel ff : list) {
                if (ff.getAccount().equals(targetAccount)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 增加操作轮数,禁比超过5轮的移除
     */
    public void addOparetionCount() {
        int i = oparetionPlayerNum.getAndIncrement();
        if (i == playerSet.getNowPlay().size()) {
            oparetionCont.incrementAndGet();
            for(Map.Entry<String,List<ForbidCompareModel>> e:forbidCompareModelMap.entrySet()){
                List<Integer> removeAccount = new ArrayList<>();
                List<ForbidCompareModel> eValue = e.getValue();
                for(int j = 0,s = eValue.size();j<s;j++){
                    eValue.get(i).add();
                    if(eValue.get(i).getCount() >= 5)
                        removeAccount.add(i);
                }
                for(int g:removeAccount){
                    eValue.remove(g);
                }
            }
        }
    }

    public void end() {
        setStartTime();
        exchangeCard.clear();
        forbidCompareModelMap.clear();
        oparetionCont.set(0);
        oparetionPlayerNum.set(0);
        betState.set(false);
        betAllPlayer = null;
        myRoom.setRoomState(RoomStateType.READY);
        //设置下一局下注位置
        String account = playerSet.getNextPositionAccount(nowBottomPos.get());
        winPosition.set(playerSet.getPlayerPos(account));
        nowBottomChip.set(0);

        List<FirstPlayerRoom> pay = playerSet.nowAllPayPlayer();
        List<RoomWeathDto> weathDtos = new ArrayList<>(pay.size());
        for (FirstPlayerRoom f : pay) {
            HandCard handCard = f.getHandCard();
            weathDtos.add(f.getPlayer().weathDto(handCard.getCardType(), handCard.isCompareResult()));
        }

        RoomWeathDtos weath = new RoomWeathDtos(weathDtos);
        //同步大厅数据
        HttpClient.getInstance().asyncPost(NotifyCode.REQUEST_HALL_UPDATE_WEATH, JsonUtils.jsonSerialize(weath) + "," + PlayerFactory.SYSTEM_PLAYER_ID);
        //请求大厅数据
    }

    public void timer() {
        if (myRoom.getRoomState() != RoomStateType.START) {
            if (playerSet.getChoiceNum(true) >= 2 && System.currentTimeMillis() - startTime.get() >= OUT_TIME) {
                startBattle();
                LoggerUtils.getLogicLog().info("这一直执行吗？");
            }
            return;
        }
        //通知所有位置玩家超时 弃牌
        if (isBottomTimeOut()) {
            FirstPlayerRoom losePlayer = playerSet.getPlayerForPosition(nowBottomPos.get());
            String loseAccount = losePlayer.getPlayer().getAccount();
            playerSet.losePlayer(loseAccount);
            losePlayer.getHandCard().setCompareResult(false);

            if (playerSet.loseNum() >= playerSet.playNum() - 1) {
                myRoom.end();
                return;
            }
            myRoom.broadcast(playerSet.getAllPlayer(), NotifyCode.ROOM_BET_TIME_OUT, new RoomPlayerAccountDto(loseAccount));

            startTime.set(System.currentTimeMillis());
        }
    }

    /**
     * 某个玩家弃牌导致牌局结束
     */
    public void timeOutAndEnd() {
        List<FirstPlayerRoom> allPlayer = playerSet.getAllPlayer();
        List<CompareCardResultDto> resultDtos = new ArrayList<>();
        for (FirstPlayerRoom f : allPlayer) {
            HandCard handCard = f.getHandCard();
            if (handCard == null)
                continue;
            if (playerSet.getNowPlay().containsKey(f.getPlayer().getAccount())) {
                handCard.setCompareResult(true);
            }
            CompareCardResultDto compareCardResultDto = new CompareCardResultDto(f.getPlayer().getAccount(), "",
                    handCard.getCardType(),
                    ArrayUtils.arrayToList(handCard.getCardIds()));
            resultDtos.add(compareCardResultDto);
        }
        myRoom.broadcast(allPlayer, NotifyCode.ROOM_BATTLE_END, new CompareCardResultDtos(resultDtos));
    }

    public boolean getBetState() {
        return betState.get();
    }

    public void setBetState() {
        this.betState.set(true);
    }

    public FirstPlayerRoom getBetAllPlayer() {
        return betAllPlayer;
    }

    public void setBetAllPlayer(FirstPlayerRoom betAllPlayer) {
        this.betAllPlayer = betAllPlayer;
    }
}
