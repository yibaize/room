package org.bql.rooms.thousands_of.model;

import org.bql.error.AppErrorCode;
import org.bql.error.GenaryAppError;
import org.bql.net.builder_clazz.NotifyCode;
import org.bql.net.message.ServerResponse;
import org.bql.player.PlayerInfoDto;
import org.bql.player.PlayerRoomBaseInfoDto;
import org.bql.rooms.thousands_of.dto.PositionDto;
import org.bql.rooms.three_cards.three_cards_1.dto.RoomPlayerAccountDto;
import org.bql.utils.ArrayUtils;
import org.bql.utils.ProtostuffUtils;

import java.util.*;

public class TOPlayerSet {
    private static final Object LOCK = new Object();
    public static final int MAX_POSITION = 6;
    private static final int MAX_BANKER_COUNT = 6;//最大连庄次数
    private TORoom room;
    private TOPlayer[] positionList;
    private TOPlayer banker;//庄家
    private int bankerCount;//当前玩家上庄次数
    private LinkedList<TOPlayer> bankerUpList;//上庄列表
    private Map<String, TOPlayer> allPlayer;//所有玩家

    private int nowPositionNum = 0;

    public TOPlayerSet() {
    }

    public TOPlayerSet(TORoom room) {
        this.room = room;
        positionList = new TOPlayer[MAX_POSITION];
        bankerUpList = new LinkedList<>();
        allPlayer = new HashMap<>();
    }

    public void enter(TOPlayer player) {
        synchronized (LOCK) {
            String account = player.getPlayer().getAccount();
            allPlayer.putIfAbsent(account, player);
            //通知所有玩家有人上线
            room.broadcast(allPlayerNotId(account), NotifyCode.ENTER_ROOM, null); //有人进入房间
            if (nowPositionNum < MAX_POSITION) {
                for (int i = 0; i < positionList.length; i++) {
                    if (positionList[i] == null) {
                        upPosition(i, player);
                        break;
                    }
                }
            }
        }
    }

    public void exit(TOPlayer p) {
        synchronized (LOCK) {
            String account = p.getPlayer().getAccount();
            allPlayer.remove(account);
            if (banker != null && banker.equals(p)) {
                //下庄
                bankerExit(account);
            } else if (bankerUpList.contains(p)) {
                bankerUpList.remove(p);
            }
            RoomPlayerAccountDto dto = null;
            if (p.getPosition() != TOPlayer.DEFAULT_POS) {
                positionList[p.getPosition()] = null;
                nowPositionNum--;
                dto = new RoomPlayerAccountDto(account);
            }
            room.broadcast(allPlayerNotId(account), NotifyCode.EXIT_ROOM, dto); //有人离开房间
        }
    }

    public int getAllPlayerNum() {
        return allPlayer.size();
    }

    public TOPlayer getBanker() {
        return banker;
    }

    /**
     * 申请上庄
     *
     * @param p
     */
    public boolean bankerUp(TOPlayer p) {
//        String account = p.getPlayer().getAccount();
        if(bankerUpList.contains(p) || p.equals(banker))
            return false;
        bankerUpList.push(p);
        bankerUp();
        return true;
    }

    /**
     * 下庄
     *
     * @param account
     * @return
     */
    public boolean bankerExit(String account) {
        if (banker.getPlayer().getAccount().equals(account)) {
            //通知上衣庄家下庄了
            banker = null;
            bankerUp();
            return true;
        }
        return false;
    }

    public LinkedList<TOPlayer> getBankerUpList() {
        return bankerUpList;
    }

    private void bankerUp() {
        if(banker != null)
            return;
        if (bankerUpList.size() > 0) {
            banker = bankerUpList.pop();
            bankerCount = 0;
            //通知所有人换庄
            PlayerRoomBaseInfoDto prbif = notifyDto(0, banker.getPlayer(), 0, true);
            //通知所有玩家
            room.broadcast(getAllPlayer(), NotifyCode.EXCHANGE_BANKER, prbif); //通知所有人有人上了位置
        } else {
            room.broadcast(getAllPlayer(), NotifyCode.IS_SYSTEM_BANKER, null); //通知所有人现在是系统庄家
        }
    }

    private final static Object POT_LOCK = new Object();

    /**
     * 上位
     *
     * @param pos
     * @param p
     */
    public boolean upPosition(int pos, TOPlayer p) {
        synchronized (POT_LOCK) {
            if (p.getPosition() != TOPlayer.DEFAULT_POS)
                new GenaryAppError(AppErrorCode.POSITION_HASH_PLAYER);//当前已经有位置
            if (positionList[pos] == null) {
                positionList[pos] = p;
                p.setPosition(pos);
                nowPositionNum++;
                PlayerRoomBaseInfoDto prbif = notifyDto(pos, p.getPlayer(), 0, true);
                //通知所有玩家
                room.broadcast(allPlayerNotId(p.getPlayer().getAccount()), NotifyCode.UP_POSTION, prbif); //通知所有人有人上了位置
                return true;
            }
            return false;
        }
    }

    private PlayerRoomBaseInfoDto notifyDto(int pos, PlayerInfoDto pd, long bottomNum, boolean hasReady) {
        PlayerRoomBaseInfoDto prbif = new PlayerRoomBaseInfoDto();
        prbif.setAccount(pd.getAccount());
        prbif.setPostion(pos);
        prbif.setBottomNum(bottomNum);
        prbif.setGold(pd.getGold());
        prbif.setUserName(pd.getUsername());
        prbif.setHeadIcon(pd.getHeadIcon());
        prbif.setVipLv(pd.getVipLv());
        prbif.setAutoId(pd.getNowUserAutos());
        return prbif;
    }

    /**
     * 下位置
     *
     * @param p
     * @return
     */
    public boolean downPos(TOPlayer p) {
        if (p.getPosition() != TOPlayer.DEFAULT_POS) {
            synchronized (POT_LOCK) {
                positionList[p.getPosition()] = null;
                p.setPosition(TOPlayer.DEFAULT_POS);
                nowPositionNum--;
                return true;
            }
        }
        return false;
    }

    public int getNowPositionNum() {
        return nowPositionNum;
    }

    /**
     * 踢人出位置
     *
     * @param p
     * @return
     */
    public boolean kicking(TOPlayer p, int pos) {
        synchronized (POT_LOCK) {
            TOPlayer target = positionList[pos];
            if (target != null && target.getPlayer().getAccount() != p.getPlayer().getAccount()) {
                PlayerInfoDto self = p.getPlayer();
                PlayerInfoDto other = target.getPlayer();
                if (self.getVipLv() < other.getVipLv()) {
                    new GenaryAppError(AppErrorCode.VIP_LV_NOT_ERR);//vip等级没别人高
                }
                target.setPosition(TOPlayer.DEFAULT_POS);
                byte[] buf = ProtostuffUtils.serializer(new RoomPlayerAccountDto(self.getUsername()));
                target.getSession().write(new ServerResponse(NotifyCode.POISITION_KICKING, buf));// 通知被踢玩家
                downPos(target);
                //通知有人下位置
                room.broadcast(allPlayerNotId(target.getPlayer().getAccount()), NotifyCode.HASK_PLAYER_KICKING, new RoomPlayerAccountDto(target.getPlayer().getUsername())); //有人离开房间
            }
            return false;
        }
    }

    /**
     * 结束的时候调用
     */
    public void addBankerCount() {
        bankerCount++;
        if (bankerCount >= MAX_BANKER_COUNT) {
            if (banker == null) {
                bankerCount = 0;
                return;
            }
            bankerExit(banker.getPlayer().getAccount());
        }
    }

    //获取所有人
    public List<TOPlayer> getAllPlayer() {
        return new ArrayList<>(allPlayer.values());
    }

    /**
     * 除了某个玩家的所有玩家
     *
     * @param account
     * @return
     */
    public List<TOPlayer> allPlayerNotId(String account) {
        ArrayList<TOPlayer> list = new ArrayList<>();
        for (TOPlayer p : allPlayer.values()) {
            if (!p.getPlayer().getAccount().equals(account)) {
                list.add(p);
            }
        }
        return list;
    }

    public int allPlayerNum() {
        return allPlayer.size();
    }

    public List<TOPlayer> positionPlayer() {
        List<TOPlayer> to = new ArrayList<>(6);
        for (int i = 0; i < positionList.length; i++) {
            TOPlayer t = positionList[i];
            if (t != null) {
                to.add(t);
            }
        }
        return to;
    }

    public TOPlayer getPlayerForAccount(String account) {
        return allPlayer.getOrDefault(account, null);
    }

    public void end() {
        addBankerCount();
        //positionList = ArrayUtils.clear(positionList);
    }
}
