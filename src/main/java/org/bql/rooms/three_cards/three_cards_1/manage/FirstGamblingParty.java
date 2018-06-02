package org.bql.rooms.three_cards.three_cards_1.manage;

import org.bql.error.AppErrorCode;
import org.bql.error.GenaryAppError;
import org.bql.hall_connection.dto.RoomWeathDto;
import org.bql.hall_connection.dto.RoomWeathDtos;
import org.bql.net.builder_clazz.NotifyCode;
import org.bql.net.http.HttpClient;
import org.bql.player.PlayerFactory;
import org.bql.player.PlayerInfoDto;
import org.bql.rooms.card.CardDataTable;
import org.bql.rooms.card.CardManager;
import org.bql.rooms.three_cards.three_cards_1.dto.*;
import org.bql.rooms.three_cards.three_cards_1.model.ForbidCompareModel;
import org.bql.rooms.three_cards.three_cards_1.model.HandCard;
import org.bql.rooms.type.Chip;
import org.bql.rooms.type.RoomStateType;
import org.bql.utils.ArrayUtils;
import org.bql.utils.JsonUtils;
import org.bql.utils.logger.LoggerUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FirstGamblingParty {
    public static final int MULTIPLE = 2;//看牌后翻的倍数
    private static final long OUT_TIME = 5000;//有超过两个人准备之后最多五秒后房间开局
    private static final int END_TIME = 3000;//结束之后延迟3秒才结束牌局
    private static final long END_OUT_TIME = 15000;//每次考虑下注最长的时间，如果超过视为弃牌
    private List<CardDataTable> exchangeCard;//换牌是使用的集合
    private Map<String, List<ForbidCompareModel>> forbidCompareModelMap;//禁比
    private final FirstRooms myRoom;
    private final MyPlayerSet playerSet;
    private final CardManager cardManager;
    private AtomicLong startTime;
    private AtomicLong endTime;
    private AtomicInteger winPosition;//上把赢钱的位置
    private AtomicInteger nowBottomPos;//当前下注房间位置
    private AtomicInteger nowBottomChip;//当前筹码位置
    private AtomicInteger operationCont;//操作轮数
    private AtomicInteger operationPlayerNum;//操作人数
    private AtomicBoolean betState;//压注状态，是否已经开启全压模式
    private FirstPlayerRoom betAllPlayer;//上一个全压的玩家
    private long betAllGold;//全压的金币数量是多少
    private AtomicBoolean roomEnd;//房间结束标记
    private FirstPlayerRoom winPlayer;
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
        this.operationCont = new AtomicInteger(0);
        this.operationPlayerNum = new AtomicInteger(0);
        this.betState = new AtomicBoolean(false);
        this.roomEnd = new AtomicBoolean(false);
        this.endTime = new AtomicLong(0);
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
    public void setEndTime(){
        endTime.set(System.currentTimeMillis());
    }
    public void setRoomEnd(){
        roomEnd.set(true);
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
            myRoom.bottom(p.getPlayer().getAccount(),chip);
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
        int temp = 0;
        for (int i = 0; i < playerNum; i++) {
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

    public FirstPlayerRoom getWinPlayer() {
        return winPlayer;
    }

    public void setWinPlayer(FirstPlayerRoom winPlayer) {
        this.winPlayer = winPlayer;
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
        int i = operationPlayerNum.getAndIncrement();
        if (i == playerSet.getNowPlay().size()) {
            operationCont.incrementAndGet();
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
        operationCont.set(0);
        operationPlayerNum.set(0);
        betState.set(false);
        roomEnd.set(false);
        endTime.set(0);
        betAllPlayer = null;
        betAllGold = 0;
        myRoom.setRoomState(RoomStateType.READY);
        //设置下一局下注位置
        String account = playerSet.getNextPositionAccount(nowBottomPos.get());
        winPosition.set(playerSet.getPlayerPos(account));
        nowBottomChip.set(0);

        List<FirstPlayerRoom> pay = playerSet.getAllPlayer();
        List<RoomWeathDto> weathDtos = new ArrayList<>(pay.size());
        for (FirstPlayerRoom f : pay) {
            HandCard handCard = f.getHandCard();
            if(handCard == null)
                continue;
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
                List<FirstPlayerRoom> list = new ArrayList<>(playerSet.getNowPlay().values());
                if(list.size() <= 0)
                    new GenaryAppError(AppErrorCode.SERVER_ERR);
                winPlayer = list.get(0);
                myRoom.end();
                return;
            }
            myRoom.broadcast(playerSet.getAllPlayer(), NotifyCode.ROOM_BET_TIME_OUT, new RoomPlayerAccountDto(loseAccount));

            startTime.set(System.currentTimeMillis());
        }
        //延迟3秒结束
        if(roomEnd.get() && System.currentTimeMillis() - endTime.get() > END_TIME){
            myRoom.end();
        }
    }

    /**
     * 某个玩家弃牌导致牌局结束
     */
    public void timeOutAndEnd() {
        FirstRoomSettleDto settleDto = new FirstRoomSettleDto();
        settleDto.setAccount(winPlayer.getPlayer().getAccount());
        settleDto.setCardType(winPlayer.getHandCard().getCardType());
        settleDto.setCardIds(ArrayUtils.arrToList(winPlayer.getHandCard().getCardIds()));
        settleDto.setWinPlayerGetNum(myRoom.getAllMoneyNum());
        Map<String,Long> moneys = myRoom.getBottomMoney();
        List<SettleLoseModelDto> modelDtos = new ArrayList<>(moneys.size());
        //输的玩家
        List<FirstPlayerRoom> allPlayer = playerSet.getAllPlayer();
        String winAccount = winPlayer.getPlayer().getAccount();
        for(FirstPlayerRoom p:allPlayer){
            if(p.getHandCard() == null)
                continue;
            String pAccount = p.getPlayer().getAccount();
            if(!pAccount.equals(winAccount)) {
                modelDtos.add(new SettleLoseModelDto(pAccount, moneys.get(pAccount),ArrayUtils.arrToList(p.getHandCard().getCardIds()),p.getHandCard().getCardType()));
            }
        }
        settleDto.setSettleModelDtos(modelDtos);
        winPlayer.getPlayer().insertGold(myRoom.getAllMoneyNum());//赢家财富
        myRoom.broadcast(playerSet.getAllPlayer(),NotifyCode.ROOM_SETTLE_ACCOUNT,settleDto);//通知所有玩家这家伙赢了
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

    public void setBetAllGold(long betAllGold) {
        this.betAllGold = betAllGold;
    }

    public long getBetAllGold() {
        return betAllGold;
    }

    public void setBetAllPlayer(FirstPlayerRoom betAllPlayer) {
        this.betAllPlayer = betAllPlayer;
    }
}
