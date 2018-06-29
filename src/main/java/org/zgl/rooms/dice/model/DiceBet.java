package org.zgl.rooms.dice.model;

import org.zgl.net.builder_clazz.NotifyCode;
import org.zgl.net.message.ServerResponse;
import org.zgl.player.PlayerInfoDto;
import org.zgl.rooms.thousands_of.dto.BetUpdateDto;
import org.zgl.rooms.thousands_of.dto.PlayerWeathUpdateDto;
import org.zgl.rooms.type.ProcedureType;
import org.zgl.utils.ProtostuffUtils;
import org.zgl.utils.logger.LoggerUtils;

import java.util.ArrayList;
import java.util.Collections;
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
    public long clearPlayerBet(DicePlayer player){
        if(players.containsKey(player)){
            long money = players.remove(player);
            player.getPlayer().insertGold(money);
            return money;
        }
        return 0;
    }
    public void loseSettle(){
        ServerResponse response = new ServerResponse();
        response.setId(NotifyCode.ROOM_WEATH_UPDATE);
        for (Map.Entry<DicePlayer, Long> e : players.entrySet()) {
            DicePlayer p = e.getKey();
            PlayerInfoDto playerInfoDto = p.getPlayer();

            BetUpdateDto betUpdateDto = new BetUpdateDto();
            betUpdateDto.setAccount(e.getValue().toString());
            betUpdateDto.setGold(playerInfoDto.getGold());
            betUpdateDto.setPosition(position);
            betUpdateDto.setBetPlayerNum(2);
            //通知自己赢得金币
            byte[] buf = ProtostuffUtils.serializer(betUpdateDto);
            response.setData(buf);
            e.getKey().getSession().write(response);
            p.clearBet();
        }
        clear();
    }
    public long settleAccounts(float rate, ProcedureType procedureType){
        long allMoneytTemp = 0;
        ServerResponse response = new ServerResponse();
        response.setId(NotifyCode.ROOM_WEATH_UPDATE);

        allMoneytTemp = (long) (-allMoney.get() * rate);

        for (Map.Entry<DicePlayer, Long> e : players.entrySet()) {
            DicePlayer p = e.getKey();
            p.clearBet();
            PlayerInfoDto playerInfoDto = p.getPlayer();
            long money = (long) (e.getValue() * rate);//这里的5是牌的倍率
            playerInfoDto.insertGold(money + e.getValue());
//            //扣除百分之5的平台费
//            money = (long) (money - money * procedureType.id());
            BetUpdateDto betUpdateDto = new BetUpdateDto();
            betUpdateDto.setAccount(e.getValue().toString());
            betUpdateDto.setGold(playerInfoDto.getGold());
            betUpdateDto.setPosition(position);
            betUpdateDto.setBetPlayerNum(1);
            //这个点的倍数
            betUpdateDto.setWinGld((long) rate);
            //通知自己赢得金币
            byte[] buf = ProtostuffUtils.serializer(betUpdateDto);
            response.setData(buf);
            //通知自己财富变更
            p.getSession().write(response);
//            有位置
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
