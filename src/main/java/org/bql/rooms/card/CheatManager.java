package org.bql.rooms.card;

import org.bql.rooms.thousands_of.model.TOBet;
import org.bql.rooms.three_cards.three_cards_1.model.HandCard;
import org.bql.utils.ArrayUtils;

import java.util.*;

public class CheatManager {
    private final static List<Integer> l;
    static {
        l = new ArrayList<>();
        for(int i = 1;i<5;i++){
            l.add(i);
        }
    }
    public List<Integer> getL(){
        return new ArrayList<>(l);
    }
    private static HandCard[] cheat(Map<Integer,HandCard> handCardMap){
        HandCard[] handCards = ArrayUtils.arrForList(new ArrayList<>(handCardMap.values()));
        //对所有手牌进行排序
        int length = handCards.length;
        //冒泡大到小排序
//        for(int i = 0;i<length;i++){
//            for(int j = 1;j<length - i;j++){
//                HandCard h1 = handCards[j-1];
//                HandCard h2 = handCards[j];
//                CardManager.getInstance().compareCard(h1,h2);
//                if(h1.isCompareResult()){
//                    HandCard temp = h1;
//                    handCards[j-1] = h2;
//                    handCards[j] = temp;
//                }
//            }
//        }
        //优化后的冒泡大到小排序
        int j,k;
        int flag = length;
        while (flag > 0){
            k = flag;
            flag = 0;
            for(j = 1;j<k;j++){
                HandCard h1 = handCards[j-1];
                HandCard h2 = handCards[j];
                CardManager.getInstance().compareCard(h1,h2);
                if(h1.isCompareResult()){
                    HandCard temp = h1;
                    handCards[j-1] = h2;
                    handCards[j] = temp;

                    flag = j;
                }
            }
        }
        return handCards;
    }

    /**
     * index之前全设置为赢的，之后为输的
     * @param handCards
     * @param index
     */
    public void getHandCard(HandCard[] handCards,int index){
        for(int i = 0;i<index;i++){
            handCards[i].setCompareResult(true);
        }
        for(int i = index;i<handCards.length;i++){
            handCards[i].setCompareResult(false);
        }
    }

    /**
     * 确定手牌
     */
    public Map<Integer,HandCard> changeHandCard(Map<Integer,HandCard> handCardMap,Map<Integer,TOBet> bets){
        HandCard[] h = cheat(handCardMap);
        //拿到所有的钱堆
        //哪个钱堆最多
        //如果有两2副牌的情况
        //如果有两3副牌的情况
        //如果有两4副牌的情况
        switch (bets.size()){
            case 2:
                two(bets,h);
                break;
            case 3:
                break;
            case 4:
                break;
        }
        return null;
    }
    private Map<Integer,HandCard> two(Map<Integer,TOBet> bets,HandCard[] h){
        Map<Integer,HandCard> handCardMap = new HashMap<>(5);
        List<Integer> pos = getL();
        List<TOBet> l = new ArrayList<>(bets.values());
        Collections.sort(l);//排序之后
        TOBet t1 = l.get(0);
        TOBet t2 = l.get(1);
        getHandCard(h,3);
        handCardMap.put(pos.remove(t1.getPosition()),h[4]);//t1要输
        handCardMap.put(pos.remove(t2.getPosition()),h[2]);//t2要赢
        handCardMap.put(pos.get(0),h[0]);
        handCardMap.put(pos.get(1),h[1]);
        handCardMap.put(0,h[3]);//庄家
        return handCardMap;
    }
    private void three(Map<Integer,TOBet> bets,HandCard[] h){
        Map<Integer,HandCard> handCardMap = new HashMap<>(5);
        List<Integer> pos = getL();
        List<TOBet> l = new ArrayList<>(bets.values());
        Collections.sort(l);//排序之后
        TOBet t1 = l.get(0);
        TOBet t2 = l.get(1);
        TOBet t3 = l.get(2);
    }
    private void four(Map<Integer,TOBet> bets,HandCard[] h){
        Map<Integer,HandCard> handCardMap = new HashMap<>(5);
        List<Integer> pos = getL();
        List<TOBet> l = new ArrayList<>(bets.values());
        Collections.sort(l);//排序之后
        TOBet t1 = l.get(0);
        TOBet t2 = l.get(1);
        TOBet t3 = l.get(2);
        TOBet t4 = l.get(3);
    }

}
