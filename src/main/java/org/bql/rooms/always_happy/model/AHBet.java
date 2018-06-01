package org.bql.rooms.always_happy.model;

import org.bql.net.builder_clazz.NotifyCode;
import org.bql.net.message.ServerResponse;
import org.bql.net.server.session.ISession;
import org.bql.net.server.session.SessionManager;
import org.bql.player.PlayerInfoDto;
import org.bql.player.PlayerRoom;
import org.bql.rooms.always_happy.dto.AHWeathDto;
import org.bql.rooms.card.CardType;
import org.bql.rooms.type.ProcedureType;
import org.bql.utils.ProtostuffUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class AHBet {
    private Map<PlayerRoom, Long> players;
    private AtomicLong allMoney;
    private int position;

    public AHBet() {
    }

    public AHBet(int position) {
        allMoney = new AtomicLong(0);
        players = new ConcurrentHashMap<>();
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
    public List<PlayerRoom> getAllPlayer(){
        return new ArrayList<>(players.keySet());
    }
    public void bet(PlayerRoom player, long count) {
        allMoney.addAndGet(count);
        long num = count;
        if (players.containsKey(player)) {
            num += players.get(player);
        }
        players.put(player, num);
    }

    public long settleAccount(CardType cardType, ProcedureType type) {
        long allMo = allMoney.get();
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setId(NotifyCode.AH_ROOM_SETTLE_ACCOUNT);
        for (Map.Entry<PlayerRoom, Long> e : players.entrySet()) {
            PlayerRoom ap = e.getKey();
            ap.clearBet();
            PlayerInfoDto infoDto = ap.getPlayer();
            long resultMoney = (long) (e.getValue() * cardType.ahRate());
            switch (type) {
                case WIN:
                    resultMoney = (long) (resultMoney - resultMoney * type.id());
                    infoDto.insertGold(resultMoney + e.getValue());
                    infoDto.reduceGold(resultMoney);
                    break;
                case LOSE:
                    long temp = Math.abs(resultMoney - e.getValue());
                    infoDto.reduceGold(temp);
                    break;
            }
            //结算
            byte[] buf = ProtostuffUtils.serializer(new AHWeathDto(type.getResult(),infoDto.getGold(),resultMoney));
            serverResponse.setData(buf);
            ISession session = SessionManager.getSession(infoDto.getAccount());
            if(session != null)
                session.write(serverResponse);
        }
        allMoney.set(0);
        players.clear();
        System.err.println("这里清空");
        return allMo;
    }
}
