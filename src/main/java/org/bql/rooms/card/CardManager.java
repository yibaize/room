package org.bql.rooms.card;

import org.bql.rooms.three_cards.three_cards_1.model.HandCard;
import org.bql.utils.ArrayUtils;
import org.bql.utils.RandomUtils;
import org.bql.utils.builder_clazz.excel_init_data.ExcelUtils;
import org.bql.utils.builder_clazz.excel_init_data.StaticConfigMessage;
import org.bql.utils.logger.LoggerUtils;

import java.util.*;

public class CardManager {
    //线程安全的懒汉式单利模式
    private static class SingletonHolder{
        public final static CardManager instance = new CardManager();
    }
    private static CardManager instance;
    public static CardManager getInstance(){
        return SingletonHolder.instance;
    }
    private final List<CardDataTable> cardList;//所有的牌
    private final List<CardDataTable> cardDataTables;//千王场的牌
    private final Map<Integer,CardDataTable> map;//牌id对应的牌
    private final Map<Integer,List<CardDataTable>> cardTypeMap;//黑红梅方对应的牌
    private final Map<Integer,List<CardDataTable>> cardFaceMap;//牌面对应的牌
    public final int[][] cardTypeFace;
    private CardManager(){
        map = new HashMap<>();
        cardTypeMap = new HashMap<>();
        cardFaceMap = new HashMap<>();
        cardTypeFace = new int[5][15];
        List<Object> cards = new ArrayList<>(StaticConfigMessage.getInstance().getMap(CardDataTable.class).values());
        cardList = new ArrayList<>(cards.size());
        cardDataTables = new ArrayList<>();
        for(Object o : cards){
            CardDataTable ccc = (CardDataTable) o;
            cardList.add(ccc);
            if(ccc.getFace() > 7)
                cardDataTables.add(ccc);
            map.putIfAbsent(((CardDataTable) o).getId(),ccc);

            List<CardDataTable> c = cardTypeMap.getOrDefault(ccc.getType(),null);
            if(c == null) {
                c = new ArrayList<>(13);
                cardTypeMap.put(ccc.getType(),c);
            }
            c.add(ccc);

            List<CardDataTable> cf = cardFaceMap.getOrDefault(ccc.getFace(),null);
            if(cf == null){
                cf = new ArrayList<>(4);
                cardFaceMap.put(ccc.getFace(),cf);
            }
            cf.add(ccc);

            cardTypeFace[ccc.getType()][ccc.getFace()] = ccc.getId();
        }
    }

    public List<CardDataTable> getCardDataTables() {
        return new ArrayList<>(cardDataTables);
    }

    public Map<Integer, CardDataTable> getMap() {
        return map;
    }
    public CardDataTable getCard(int id){
        return map.getOrDefault(id,null);
    }
    /**
     * 洗牌之后要截取多少张牌
     * @param num
     * 这里要区分是千王场还是经典场
     */
    public List<CardDataTable> shuff(int num,int scenesType){
        List<CardDataTable> c = null;
        switch (scenesType){
            case 1:
                c = new ArrayList<>(cardList);
                break;
            case 2:
                c = new ArrayList<>(cardDataTables);
        }
        Collections.shuffle(c);
        return c.subList(0,num);
    }
    public HandCard getCardType(HandCard c){
        if(AAA(c))
            return c;
        else if(leopard(c))
            return c;
        else if(straightFlush(c))
            return c;
        else if(sameColor(c))
            return c;
        else if(straight(c))
            return c;
        else if(pair(c))
            return c;
        else if(highCard(c))
            return c;
        return c;
    }

    /**
     * 比较大小
     * @param
     * @return
     */
    public void compareCard(HandCard self, HandCard other){
        int selfType = self.getCardType();
        int otherType = other.getCardType();
        boolean compareResult = false;
        if(selfType > otherType){
            compareResult = true;
        }else if(selfType == otherType){
            compareHighCart(self,other);
            return;
        }else {
            compareResult = false;
        }
        self.setCompareResult(compareResult);
        other.setCompareResult(!compareResult);
    }
    private void compareHighCart(HandCard self, HandCard other){
        boolean selfResult = false;
        if(self.getMax() == other.getMax()){//最大的相等
            if(self.getCardFaces()[1] == other.getCardFaces()[1]){//第二个的相等
                if(self.getMin() == other.getMin()){//最小的相等
                    selfResult = map.get(self.getMax()).getType() > map.get(other.getMax()).getType();//比黑 红 梅 方
                }else {
                    selfResult = self.getMin() > other.getMin();
                }
            }else {
                selfResult = self.getCardFaces()[1] > other.getCardFaces()[1];
            }
        }else {
            selfResult = self.getMax() > other.getMax();
        }
        self.setCompareResult(selfResult);
        other.setCompareResult(!selfResult);
    }
    /**
     * 散牌
     * @param c
     */
    public boolean highCard(HandCard c){
        Integer[] cardFace = c.getCardFaces();
        Arrays.sort(cardFace);
        c.setCardType(CardType.HIGH_CARD.id());
        c.setMax(cardFace[2]);
        c.setMin(cardFace[0]);
        return true;
    }

    /**
     * 对子
     * @return
     */
    public boolean pair(HandCard c){
        Integer[] cardFace = c.getCardFaces();
        Arrays.sort(cardFace);
        for(int i = 0;i<cardFace.length - 1;i++){
            if(cardFace[i] == cardFace[i+1]) {
                c.setCardType(CardType.PAIR.id());
                if(cardFace[0] == cardFace[1]){
                    c.setMin(cardFace[2]);
                }else {
                    c.setMin(cardFace[0]);
                }
                c.setMax(cardFace[1]);
                return true;
            }
        }
        return false;
    }
    public static final Integer[] A12 = new Integer[]{14,2,3};
    //顺子
    public boolean straight(HandCard c){
        Integer[] cardFace = c.getCardFaces();
        Arrays.sort(cardFace);
        if(ArrayUtils.contains(cardFace,A12)){
            return true;
        }
        for(int i = 0;i<cardFace.length - 1;i++){
            if(cardFace[i] - cardFace[i+1] != -1) {
                return false;
            }
        }
        c.setCardType(CardType.STRAIGHT.id());
        c.setMax(cardFace[2]);
        c.setMin(cardFace[0]);
        return true;
    }
    //同花
    public boolean sameColor(HandCard c){
        Integer ids[] = c.getCardIds();
        for(int i=0;i<ids.length-1;i++){
            if(map.get(ids[i]).getType() != map.get(ids[i+1]).getType())
                return false;
        }
        Integer[] cardFace = c.getCardFaces();
        Arrays.sort(cardFace);
        c.setCardType(CardType.SAME_COLOR.id());
        c.setMax(cardFace[2]);
        c.setMin(cardFace[0]);
        return true;
    }
    /**
     * 同花顺
     * @return
     */
    public boolean straightFlush(HandCard c){
        if(!sameColor(c) || !straight(c))
            return false;
        Integer[] cardFace = c.getCardFaces();
        Arrays.sort(cardFace);
        c.setCardType(CardType.STRAIGHT_FLUSH.id());
        c.setMax(cardFace[2]);
        c.setMin(cardFace[0]);
        return true;
    }

    public static void main(String[] args) {
        ExcelUtils.init("excel");
        HandCard self = new HandCard();
        HandCard other = new HandCard();
        self.setCardIds(new Integer[]{23, 35, 1});
        other.setCardIds(new Integer[]{30, 38, 48});

        self.setCardFaces(new Integer[]{9, 10, 14});
        other.setCardFaces(new Integer[]{4, 14, 9});
        getInstance().getCardType(self);
        getInstance().getCardType(other);
        getInstance().compareCard(self,other);
        System.out.println(self);
        System.out.println(other);
//        ExcelUtils.init("excel");
//        getInstance();
//        int[] i = new int[]{1,1,1,1};
//        System.out.println(CardManager.getInstance().pair(i));
//        System.out.println(CardManager.getInstance().straight(i));
//        System.out.println(CardManager.getInstance().leopard(i));
    }
    /**
     * 豹子
     * @return
     */
    public boolean leopard(HandCard c){
        Integer[] cardFace = c.getCardFaces();
        Arrays.sort(cardFace);
        if(cardFace[0] == cardFace[cardFace.length-1]) {
            c.setCardType(CardType.LEOPARD.id());
            c.setMax(cardFace[2]);
            c.setMin(cardFace[0]);
            return true;
        }
        return false;
    }
    /**
     * 三张A
     * @return
     */
    public boolean AAA(HandCard c){
        Integer[] cardFace = c.getCardFaces();
        if(!leopard(c))
            return false;
        if(cardFace[0] == 1) {
            c.setCardType(CardType.AAA.id());
            c.setMax(cardFace[2]);
            c.setMin(cardFace[0]);
            return true;
        }
        return false;
    }

}
