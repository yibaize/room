package org.bql.rooms.three_cards.three_cards_1.manage;

import org.bql.net.builder_clazz.NotifyCode;
import org.bql.net.handler.TcpHandler;
import org.bql.net.message.ServerResponse;
import org.bql.player.IPlayer;
import org.bql.player.PlayerInfoDto;
import org.bql.player.PlayerRoom;
import org.bql.player.PlayerRoomBaseInfoDto;
import org.bql.rooms.RoomAbs;
import org.bql.rooms.RoomFactory;
import org.bql.rooms.card.CardManager;
import org.bql.rooms.three_cards.three_cards_1.dto.FirstRoomStartDto;
import org.bql.rooms.three_cards.three_cards_1.dto.PlayerRoomDto;
import org.bql.rooms.type.RoomStateType;
import org.bql.utils.ProtostuffUtils;
import org.bql.utils.logger.LoggerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FirstRooms extends RoomAbs {
    private RoomStateType roomState;//房间状态码1：等待准备状态 2：房间开启了等待准备状态  3：打牌状态
    private MyPlayerSet playerSet;
    private CardManager cardManager;
    private FirstGamblingParty gamblingParty;//牌局
    private long allMoneyNum;//本局房间总金额
    private Map<String, Long> bottomMoney;//下注的金额数量对应的玩家账号
    /**
     * 创建房间
     *
     * @param roomId
     * @param account 创建人
     */
    public FirstRooms(int scenesId, int roomId, String account) {
        super(scenesId, roomId, account,TcpHandler.getInstance().pool.nextWorker());
        roomState = RoomStateType.READY;
        playerSet = new MyPlayerSet(this);
        cardManager = CardManager.getInstance();
        bottomMoney = new ConcurrentHashMap<>(5);
        gamblingParty = new FirstGamblingParty(this);
    }

    public long getAllMoneyNum() {
        return allMoneyNum;
    }
    public void clearAllMoneyNum(){
        allMoneyNum = 0;
    }
    public void bottom(String account, long num) {
        allMoneyNum += num;
        long num1 = num;
        if (bottomMoney.containsKey(account)) {
            num1 += bottomMoney.get(account);
        }
        bottomMoney.put(account, num1);
    }
    public Map<String, Long> getBottomMoney() {
        return bottomMoney;
    }

    public RoomStateType getRoomState() {
        return roomState;
    }

    public void setRoomState(RoomStateType roomState) {
        this.roomState = roomState;
    }

    public MyPlayerSet getPlayerSet() {
        return playerSet;
    }

    public void setPlayerSet(MyPlayerSet playerSet) {
        this.playerSet = playerSet;
    }

    public CardManager getCardManager() {
        return cardManager;
    }

    public void setCardManager(CardManager cardManager) {
        this.cardManager = cardManager;
    }

    public FirstGamblingParty getGamblingParty() {
        return gamblingParty;
    }

    public void setGamblingParty(FirstGamblingParty gamblingParty) {
        this.gamblingParty = gamblingParty;
    }

    @Override
    public PlayerRoomDto roomInfo(String account) {
        List<FirstPlayerRoom> players = new ArrayList<>(playerSet.getPlayerForAccount().values());
        List<PlayerRoomBaseInfoDto> baseInfoDtos = new ArrayList<>();
        for (FirstPlayerRoom fpr : players) {
            PlayerRoomBaseInfoDto prbifd = fpr.roomPlayerDto();
            prbifd.setHasReady(playerSet.getPlayerChoice(prbifd.getAccount()));
            prbifd.setPostion(playerSet.getPlayerPos(prbifd.getAccount()));
            baseInfoDtos.add(prbifd);
        }
        return new PlayerRoomDto(getRoomId(), roomState.id(), playerSet.getPlayerPos(account), baseInfoDtos);
    }

    @Override
    public void enterRoom(IPlayer player) {
        FirstPlayerRoom firstPlayerRoom = (FirstPlayerRoom) player;
        PlayerInfoDto p = firstPlayerRoom.getPlayer();
        playerSet.resetpos(firstPlayerRoom);//位置
        if (roomState == RoomStateType.READY) {
            playerSet.getNowPlay().putIfAbsent(p.getAccount(), firstPlayerRoom);
            playerSet.addPlayerChoice(p.getAccount(), true);//准备
        }
        //通知房间玩家有人进来
        PlayerRoomBaseInfoDto prbifd = firstPlayerRoom.roomPlayerDto();
        prbifd.setPostion(p.getRoomPosition());
        gamblingParty.setStartTime();
        broadcast(playerSet.getNotAccountPlayer(p.getAccount()), NotifyCode.ROOM_PLAYER_ENTER, prbifd);
    }

    @Override
    public void exitRoom(IPlayer player) {
        FirstPlayerRoom firstPlayerRoom = (FirstPlayerRoom) player;
        playerSet.exit(firstPlayerRoom.getPlayer().getAccount());
        String account = firstPlayerRoom.getPlayer().getAccount();
        FirstRoomStartDto dto = new FirstRoomStartDto(account);
        broadcast(playerSet.getNotAccountPlayer(account), NotifyCode.ROOM_PLAYER_EXIT, dto);
    }

    @Override
    public IPlayer getPlayerForAccount(String account) {
        return playerSet.getPlayerForAccount(account);
    }

    @Override
    public int getPlayerNum() {
        return playerSet.getRoomPlayerNum();
    }

    @Override
    public List<PlayerRoom> getNotAccountAllPlayer(String account) {
        List<FirstPlayerRoom> playerRooms = playerSet.getNotAccountPlayer(account);
        List<PlayerRoom> pr = new ArrayList<>(playerRooms.size());
        for (FirstPlayerRoom f : playerRooms) {
            pr.add(f);
        }
        return pr;
    }

    @Override
    public List<PlayerRoom> getAllPlayer() {
        return new ArrayList<>(playerSet.getAllPlayer());
    }

    public void broadcast(List<FirstPlayerRoom> playerList, short cmdId, Object msg) {
        ServerResponse r = response(cmdId, msg);
        for (FirstPlayerRoom fpr : playerList) {
            fpr.getSession().write(r);
        }
    }

    private ServerResponse response(short cmdId, Object o) {
        byte[] buf = null;
        if (o != null)
            buf = ProtostuffUtils.serializer(o);
        return new ServerResponse(cmdId, buf);
    }

    public void end() {
        roomState = RoomStateType.READY;//房间状态码1：等待准备状态 2：房间开启了等待准备状态  3：打牌状态
        gamblingParty.timeOutAndEnd();
        playerSet.end();
        allMoneyNum = 0;
        bottomMoney.clear();
        gamblingParty.end();//牌局
    }

    public void timer() {
        gamblingParty.timer();
    }

    @Override
    public void kicking(PlayerRoom player, int position) {

    }
}
