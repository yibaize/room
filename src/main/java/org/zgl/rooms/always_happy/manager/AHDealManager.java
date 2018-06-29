package org.zgl.rooms.always_happy.manager;

import org.zgl.error.AppErrorCode;
import org.zgl.error.GenaryAppError;
import org.zgl.rooms.always_happy.data.AHDataTable;
import org.zgl.rooms.always_happy.dto.AHHistoryDto;
import org.zgl.rooms.card.CardDataTable;
import org.zgl.rooms.card.CardManager;
import org.zgl.utils.ArrayUtils;
import org.zgl.utils.RandomUtils;
import org.zgl.utils.builder_clazz.excel_init_data.StaticConfigMessage;
import org.zgl.utils.weightRandom.WeightRandom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.zgl.rooms.card.CardManager.A12;

/**
 * @作者： big
 * @创建时间： 2018/5/24
 * @文件描述：
 */
public class AHDealManager {
    private static AHDealManager instance;
    private static final List<AHDataTable> AH_DATA;

    static {
        Map<Serializable, Object> o = StaticConfigMessage.getInstance().getMap(AHDataTable.class);
        AH_DATA = new ArrayList<>(o.size());
        for (Object ox : o.values()) {
            AH_DATA.add((AHDataTable) ox);
        }
    }

    public static AHDealManager getInstance() {
        if (instance == null)
            instance = new AHDealManager();
        return instance;
    }

    /**
     * 发牌洗牌
     */
    public AHHistoryDto shuffleAndDeal() {
        int result = WeightRandom.awardPosition(AH_DATA);
        Integer[] ids = null;
        switch (result) {
            case 1:
                ids = a();
                break;
            case 2:
                ids = b();
                break;
            case 3:
                ids = c();
                break;
            case 4:
                ids = d();
                break;
            case 5:
                ids = e();
                break;
            case 6:
                ids = f();
                break;
            case 7:
                ids = g();
                break;
            default:
                ids = a();
        }
        int oddEven = 0;
        for (int i = 0; i < ids.length; i++) {
            CardDataTable c = CardManager.getInstance().getCard(ids[i]);
            if (c == null)
                new GenaryAppError(AppErrorCode.SERVER_ERR);
            int faceExchange = c.getFace() == 14 ? 1 : c.getFace();
            oddEven += faceExchange;
        }
        oddEven = oddEven % 2 == 0 ? 8 : 9;
        return new AHHistoryDto(0, result, oddEven, ArrayUtils.arrayToList(ids));
    }

    /**
     * 散牌
     *
     * @return
     */
    private Integer[] a() {
        Integer[] face = RandomUtils.randomNotRepeat(3, 14, 3);
        Arrays.sort(face);
        //顺子
        boolean isStraight = true;
        for (int i = 0; i < face.length - 1; i++) {
            if (face[i] - face[i + 1] != -1) {
                isStraight = false;
            }
        }
        //如果是顺着就把一个牌顺便替换
        if (ArrayUtils.contains(face, A12) || isStraight) {
            int temp = face[0] - 1;
            face[0] = temp;
        }
        /**随机拿同花*/
        Integer[] flower = RandomUtils.randomNotRepeat(1, 4, 3);
        return getIds(flower, face);
    }

    /**
     * 对子
     *
     * @return
     */
    public Integer[] b() {
        Integer[] faces = RandomUtils.randomNotRepeat(2, 14, 2);
        faces = ArrayUtils.add(faces, faces[0]);
        /**随机拿同花*/
        Integer[] flower = RandomUtils.randomNotRepeat(1, 4, 3);
        return getIds(flower, faces);
    }

    /**
     * 顺子
     *
     * @return
     */
    public Integer[] c() {
        int number = RandomUtils.getRandom(3, 13);
        Integer[] face = new Integer[]{number - 1, number, number + 1};
        Integer[] flower = RandomUtils.randomNotRepeat(1, 4, 3);
        return getIds(flower, face);
    }

    /**
     * 同花
     *
     * @return
     */
    public Integer[] d() {
        Integer[] face = RandomUtils.randomNotRepeat(3, 14, 3);
        //顺子
        Arrays.sort(face);
        boolean isStraight = true;
        for (int i = 0; i < face.length - 1; i++) {
            if (face[i] - face[i + 1] != -1) {
                isStraight = false;
            }
        }
        //如果是顺着就把一个牌顺便替换
        if (ArrayUtils.contains(face, A12) || isStraight) {
            int temp = face[0] - 1;
            face[0] = temp;
        }
        int flowerNumber = RandomUtils.getRandom(1, 4);
        Integer[] flower = new Integer[]{flowerNumber, flowerNumber, flowerNumber};
        return getIds(flower, face);
    }

    /**
     * 同花顺
     *
     * @return
     */
    public Integer[] e() {
        int number = RandomUtils.getRandom(3, 13);
        Integer[] face = new Integer[]{number - 1, number, number + 1};
        int flowerNumber = RandomUtils.getRandom(1, 4);
        Integer[] flower = new Integer[]{flowerNumber, flowerNumber, flowerNumber};
        return getIds(flower, face);
    }

    /**
     * 豹子
     *
     * @return
     */
    public Integer[] f() {
        int faceNumber = RandomUtils.getRandom(2, 13);
        Integer[] face = new Integer[]{faceNumber, faceNumber, faceNumber};
        Integer[] flower = RandomUtils.randomNotRepeat(1, 4, 3);
        return getIds(flower, face);
    }

    /**
     * AAA
     *
     * @return
     */
    public Integer[] g() {
        Integer[] face = new Integer[]{14, 14, 14};
        Integer[] flower = RandomUtils.randomNotRepeat(1, 4, 3);
        return getIds(flower, face);
    }

    private Integer[] getIds(Integer[] flower, Integer[] faces) {
        Integer[] ids = new Integer[3];
        for (int i = 0; i < 3; i++) {
            ids[i] = CardManager.getInstance().cardTypeFace[flower[i]][faces[i]];
        }
        return ids;
    }

    public static void main(String[] args) {
        for(int i = 0;i<10;i++) {
            int number = RandomUtils.getRandom(3, 13);
            int x = RandomUtils.randomIndex(13);
            System.out.println(number+ " : " + x);
        }
    }
}
