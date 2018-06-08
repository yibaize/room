package org.bql.rooms.thousands_of.model;

import org.bql.net.builder_clazz.NotifyCode;
import org.bql.net.message.ServerResponse;
import org.bql.player.PlayerInfoDto;
import org.bql.rooms.card.CardType;
import org.bql.rooms.thousands_of.dto.BetUpdateDto;
import org.bql.rooms.thousands_of.dto.PlayerWeathUpdateDto;
import org.bql.rooms.type.ProcedureType;
import org.bql.utils.ProtostuffUtils;
import org.bql.utils.logger.LoggerUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class TOBet implements Comparable<TOBet> {
    private Map<TOPlayer, Long> players;
    private AtomicLong allMoney;
    private int position;

    public TOBet() {
        players = new ConcurrentHashMap<>();
        allMoney = new AtomicLong(0);
    }

    /**
     * 下注
     *
     * @param toPlayer
     * @param num
     */
    public void bet(TOPlayer toPlayer, long num) {
        allMoney.addAndGet(num);
        if (players.containsKey(toPlayer)) {
            long number = players.get(toPlayer);
            num += number;
        }
        players.put(toPlayer, num);
    }

    public long allMoney() {
        return allMoney.get();
    }

    /**
     * 结算
     *
     * @param cardType      牌型
     * @param procedureType 手续费
     */
    public long settleAccounts(CardType cardType, ProcedureType procedureType) {
        ServerResponse response = new ServerResponse();
        response.setId(NotifyCode.ROOM_WEATH_UPDATE);
        long allMoneytTemp = 0;
        switch (procedureType) {
            case LOSE:
                allMoneytTemp = allMoney.get() * cardType.rate();
                break;
            case WIN:
                allMoneytTemp = (-allMoney.get() * cardType.rate());
                break;
        }
        for (Map.Entry<TOPlayer, Long> e : players.entrySet()) {
            TOPlayer p = e.getKey();
            p.clearBet();
            PlayerInfoDto playerInfoDto = p.getPlayer();
            long money = e.getValue() * cardType.rate();//这里的5是牌的倍率
            //扣除百分之5的平台费
            LoggerUtils.getLogicLog().info(playerInfoDto.getGold() + "：前");
            switch (procedureType) {
                case WIN://赢的
                    money = (long) (money - money * procedureType.id());
                    playerInfoDto.insertGold(money + e.getValue());
                    LoggerUtils.getLogicLog().info("赢" + money + ":" + e.getValue());
                    break;
                case LOSE://输的
                    long temp = Math.abs(money - e.getValue());
                    playerInfoDto.reduceGold(temp);
                    LoggerUtils.getLogicLog().info("输" + money + ":" + e.getValue() + " : " + temp);
                    break;
            }
            LoggerUtils.getLogicLog().info(playerInfoDto.getGold() + "：后:");
            //通知自己赢得金币
            byte[] buf = ProtostuffUtils.serializer(new PlayerWeathUpdateDto(playerInfoDto.getGold()));
            response.setData(buf);
            //通知自己财富变更
            e.getKey().getSession().write(response);
            //有位置
            if (p.getPosition() != TOPlayer.DEFAULT_POS) {
                //如果是位置上的人就要通知所有人财富变更
                TORoom room = (TORoom) p.getRoom();
                //这里通知的账号现在是用来存储这个玩家输或者赢的标记
                room.broadcast(room.getPlayerSet().getAllPlayer(), NotifyCode.NOTIFY_POSITION_PLAYER_WEATH_UPDATE,
                        new BetUpdateDto(procedureType.getResult() + "", playerInfoDto.getGold(),money, p.getPosition()));
            }
        }
        clear();
        return allMoneytTemp;
    }

    public List<TOPlayer> getAllPlayer() {
        return new ArrayList<>(players.keySet());
    }

    public void exit(TOPlayer player) {
        if (players.containsKey(player)) {
            players.remove(player);
        }
    }

    private void clear() {
        allMoney.set(0);
        players.clear();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<BetUpdateDto> getRanking(CardType cardType) {
        List<BetUpdateDto> list = new ArrayList<>(players.size());
        for (Map.Entry<TOPlayer, Long> e : players.entrySet()) {
            long gold = e.getValue() * cardType.rate();
            gold -= gold * ProcedureType.WIN.id();
            PlayerInfoDto infoDto = e.getKey().getPlayer();
            list.add(new BetUpdateDto(infoDto.getAccount(),infoDto.getGold(), gold, position));
        }
        Collections.sort(list);
        int size = list.size() >= 5 ? 5 : list.size();
        List<BetUpdateDto> newList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            newList.add(list.get(i));
        }
        return newList;
    }

    @Override
    public int compareTo(TOBet o) {
        return (int) (o.allMoney() - this.allMoney());
    }
}
