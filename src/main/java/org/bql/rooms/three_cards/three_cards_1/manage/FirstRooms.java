package org.bql.rooms.three_cards.three_cards_1.manage;

import org.bql.error.AppErrorCode;
import org.bql.error.GenaryAppError;
import org.bql.hall_connection.BackHall;
import org.bql.net.builder_clazz.CommandCode;
import org.bql.net.builder_clazz.NotifyCode;
import org.bql.net.handler.TcpHandler;
import org.bql.net.message.ServerResponse;
import org.bql.player.*;
import org.bql.rooms.RoomAbs;
import org.bql.rooms.RoomFactory;
import org.bql.rooms.card.CardManager;
import org.bql.rooms.three_cards.three_cards_1.dto.FirstRoomStartDto;
import org.bql.rooms.three_cards.three_cards_1.dto.PlayerRoomDto;
import org.bql.rooms.three_cards.three_cards_1.dto.RoomBetDto;
import org.bql.rooms.three_cards.three_cards_1.dto.RoomPlayerAccountDto;
import org.bql.rooms.type.RoomStateType;
import org.bql.utils.ProtostuffUtils;
import org.bql.utils.logger.LoggerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

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
        //获取换牌卡数量
        int exchangeCardCount = 0;
        PlayerInfoDto infoDto = playerSet.getPlayerForPosition(account).getPlayer();
        List<ResourceModel> prop =infoDto.getProps();
        int size = prop == null ? 0 : prop.size();
        for(int i = 0;i<size;i++){
            if(prop.get(i).getId() == 22){
                exchangeCardCount = prop.get(i).getCount();
                break;
            }
        }
        return new PlayerRoomDto(getRoomId(), roomState.id(), playerSet.getPlayerPos(account),exchangeCardCount, baseInfoDtos);
    }

    @Override
    public void enterRoom(IPlayer player) {
        FirstPlayerRoom firstPlayerRoom = (FirstPlayerRoom) player;
        PlayerInfoDto p = firstPlayerRoom.getPlayer();
        p.setRoomId(getRoomId());
        playerSet.resetpos(firstPlayerRoom);//位置
        boolean isReady = false;
        if (roomState == RoomStateType.READY) {
            playerSet.getNowPlay().putIfAbsent(p.getAccount(), firstPlayerRoom);
            playerSet.addPlayerChoice(p.getAccount(), true);//准备
            isReady = true;
        }
        //通知房间玩家有人进来
        PlayerRoomBaseInfoDto prbifd = firstPlayerRoom.roomPlayerDto();
        prbifd.setHasReady(isReady);
        prbifd.setPostion(p.getRoomPosition());
        gamblingParty.setStartTime();
        broadcast(playerSet.getNotAccountPlayer(p.getAccount()), NotifyCode.ROOM_PLAYER_ENTER, prbifd);
    }

    @Override
    public void exitRoom(IPlayer player) {
        FirstPlayerRoom firstPlayerRoom = (FirstPlayerRoom) player;
        String account = firstPlayerRoom.getPlayer().getAccount();
        boolean isPlay = playerSet.isPlayForAccount(account);
        //获取下一玩家账号放这时因为如果放下面那么在playerSet.exit(account);中已经把该玩家一处所以获取到的位置会有问题
        String nextAccount = playerSet.getNextPositionAccount(firstPlayerRoom.getPlayer().getRoomPosition());
        playerSet.exit(account);
        FirstRoomStartDto dto = new FirstRoomStartDto(account);
        broadcast(playerSet.getNotAccountPlayer(account), NotifyCode.ROOM_PLAYER_EXIT, dto);
        if(roomState == RoomStateType.READY || !isPlay)
            return;
        int nowPlayPlayer = playerSet.playNum();
        if(nowPlayPlayer <= 1){
            if(nowPlayPlayer <= 0)
                new GenaryAppError(AppErrorCode.SERVER_ERR);
            FirstPlayerRoom winPlayer = playerSet.nowAllPayPlayer().get(0);
            gamblingParty.setWinPlayer(winPlayer);
            gamblingParty.setWinPosition(winPlayer.getPlayer().getRoomPosition());//设置这把赢家的位置
            gamblingParty.setRoomEnd();
            gamblingParty.setEndTime();
            end();
        }else {
            //下一个位置的玩家
            if(gamblingParty.getNowBottomPos().get() != firstPlayerRoom.getPlayer().getRoomPosition()){
                return;
            }
            if(nextAccount == null)
                new GenaryAppError(AppErrorCode.SERVER_ERR);
            FirstPlayerRoom nextPlayer = playerSet.getPlayerForPosition(nextAccount);
            gamblingParty.setNowBottomPos(new AtomicInteger(nextPlayer.getPlayer().getRoomPosition()));
            nextPlayer.getSession().write(new ServerResponse(NotifyCode.ROOM_PLAYER_BOTTOM,null));
            //通知下一个位置玩家做动作
            //通知下一个下注玩家
            FirstRoomStartDto nextDto = new FirstRoomStartDto(nextAccount);
            //通知所有玩家到这玩家下注
            broadcast(playerSet.getNotAccountPlayer(account),NotifyCode.HASH_PLAYER_EXIT,nextDto);
        }
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
        FirstPlayerRoom target = playerSet.getPlayerForPosition(position);
        if(target == null)
            new GenaryAppError(AppErrorCode.POSITION_HASH_PLAYER);
        if(target.getPlayer().getAccount().equals(player.getPlayer().getAccount()))
            new  GenaryAppError(AppErrorCode.DO_NOT_KICKING_SELF);
        if(target.getPlayer().getVipLv() > player.getPlayer().getVipLv())
            new GenaryAppError(AppErrorCode.VIP_LV_NOT_ERR);
        exitRoom(target);
        target.setRoom(null);
        byte[] buf = ProtostuffUtils.serializer(new RoomPlayerAccountDto(player.getPlayer().getUsername()));
        target.getSession().write(new ServerResponse(NotifyCode.KICKING_ROOM,buf));
        BackHall backHall = new BackHall();
        backHall.setCmdId((short) CommandCode.BackHall);
        backHall.setSession(target.getSession());
        backHall.subim();
    }
}
