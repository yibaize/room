package org.zgl.rooms.always_happy.model;

import org.zgl.player.PlayerInfoDto;
import org.zgl.player.PlayerRoom;
import org.zgl.rooms.type.ProcedureType;
import org.zgl.utils.logger.LoggerUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
public class AHBetModel {
    private Map<PlayerRoom, Integer> players;//玩家和下注注码
    private AtomicInteger allMoney;
    private int position;

    public AHBetModel() {
    }

    public AHBetModel(int position) {
        allMoney = new AtomicInteger(0);
        players = new ConcurrentHashMap<>();
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public List<PlayerRoom> getAllPlayer() {
        return new ArrayList<>(players.keySet());
    }

    public void bet(PlayerRoom player, int count/**下注的注数*/) {
        allMoney.addAndGet(count);
        int num = count;
        if (players.containsKey(player)) {
            num += players.get(player);
        }
        players.put(player, num);
    }
    public List<AwardModel> awardModels(int rate){
        List<AwardModel> awardModels = new ArrayList<>();
        for(Map.Entry<PlayerRoom,Integer> e: players.entrySet()){
            awardModels.add(new AwardModel(e.getKey().getPlayer().getUid(),e.getKey().getPlayer().getGold(),e.getValue()*rate));
        }
        return awardModels;
    }
    public long getAllMoney() {
        long all = allMoney.get() * 20000;
        allMoney.set(0);
        players.clear();
        return all;
    }

    public long settleAccount(float rate, ProcedureType type) {
        long allMo = (long) (allMoney.get() * rate);
        allMo -= allMoney.get()*20000;
        for (Map.Entry<PlayerRoom, Integer> e : players.entrySet()) {
            PlayerRoom ap = e.getKey();
            ap.clearBet();
            PlayerInfoDto infoDto = ap.getPlayer();
            long resultMoney = (long) (e.getValue() * rate);
            infoDto.insertGold(resultMoney);
        }
        allMoney.set(0);
        players.clear();
        return allMo;
    }
}
