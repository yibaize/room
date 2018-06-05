package org.bql.rooms.dice.model;

import org.bql.net.builder_clazz.NotifyCode;
import org.bql.net.message.ServerResponse;
import org.bql.player.PlayerInfoDto;
import org.bql.rooms.card.CardType;
import org.bql.rooms.thousands_of.dto.BetUpdateDto;
import org.bql.rooms.thousands_of.dto.PlayerWeathUpdateDto;
import org.bql.rooms.type.DiceCountType;
import org.bql.rooms.type.ProcedureType;
import org.bql.utils.ProtostuffUtils;
import org.bql.utils.logger.LoggerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 骰子位置记录
 */
public class DiceBet {
    private Map<DicePlayer,Long> players;
    private AtomicLong allMoney;
    private int position;
    public DiceBet() {
        players = new ConcurrentHashMap<>();
        allMoney = new AtomicLong(0);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public long getAllMoney() {
        return allMoney.get();
    }
    public List<DicePlayer> getBetAllPlayer(){
        return new ArrayList<>(players.keySet());
    }
    public void bet(DicePlayer dicePlayer,long num){
        allMoney.addAndGet(num);
        if(players.containsKey(dicePlayer)){
            long number = players.get(dicePlayer);
            num += number;
        }
        players.put(dicePlayer,num);
    }
    /**
     * 根据玩家清除玩家下的注并将钱返还
     * @param player
     */
    public void clearPlayerBet(DicePlayer player){
        if(players.containsKey(player)){
            long money = players.remove(player);
            player.getPlayer().insertGold(money);
        }
    }
    public long settleAccounts(DiceCountType countType, ProcedureType procedureType){
        long allMoneytTemp = 0;
        ServerResponse response = new ServerResponse();
        response.setId(NotifyCode.ROOM_WEATH_UPDATE);
        switch (procedureType) {
            case LOSE:
                allMoneytTemp = (long) (allMoney.get() * countType.getRate());
                break;
            case WIN:
                allMoneytTemp = (long) (-allMoney.get() * countType.getRate());
                break;
        }
        for (Map.Entry<DicePlayer, Long> e : players.entrySet()) {
            DicePlayer p = e.getKey();
            p.clearBet();
            PlayerInfoDto playerInfoDto = p.getPlayer();
            long money = (long) (e.getValue() * countType.getRate());//这里的5是牌的倍率
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
            if (p.getRoomPosition() != DicePlayer.DEFAULT_POSITION) {
                //如果是位置上的人就要通知所有人财富变更
                DiceRoom room = (DiceRoom) p.getRoom();
                //这里通知的账号现在是用来存储这个玩家输或者赢的标记
                room.broadcast(room.getPlayerSet().getNotPlayerToAllPlayer(playerInfoDto.getAccount()), NotifyCode.NOTIFY_POSITION_PLAYER_WEATH_UPDATE,
                        new BetUpdateDto(procedureType.getResult() + "", playerInfoDto.getGold(),money, p.getRoomPosition()));
            }
        }
        clear();
        return allMoneytTemp;
    }
    private void clear() {
        allMoney.set(0);
        players.clear();
    }

}
