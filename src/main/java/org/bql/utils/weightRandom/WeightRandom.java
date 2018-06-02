package org.bql.utils.weightRandom;

import org.bql.utils.logger.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 权重随机算法
 */
public class WeightRandom {
    private static Random random = new Random();
    private static final Logger logger = LoggerFactory.getLogger(WeightRandom.class);
    /**
     * @param element 要权重的元素
     * @return
     */
    public static int awardPosition(List<? extends IWeihtRandom> element) {
        List<WeightCategory> categorys = new ArrayList<WeightCategory>(element.size());
        for (IWeihtRandom e : element) {
            IWeihtRandom iw = e;
            categorys.add(new WeightCategory(iw.elementId(), iw.probability()));
        }
        return random(categorys);
    }

    private static int random(List<WeightCategory> categorys) {
        Integer weightSum = 0;
        for (WeightCategory wc : categorys) {
            weightSum += wc.getWeight();
        }

        if (weightSum <= 0) {
            throw new IndexOutOfBoundsException("权重随机异常=" + weightSum.toString());
        }
        Integer n = random.nextInt(weightSum); // n in [0, weightSum)
        Integer m = 0;
        for (WeightCategory wc : categorys) {
            if (m <= n && n < m + wc.getWeight()) {
                return wc.getCategory();
            }
            m += wc.getWeight();
        }
        if (weightSum <= 0)
            throw new NullPointerException("权重随机异常");
        return -1;
    }

}

class WeightCategory {
    private int category;
    private int weight;


    public WeightCategory() {
        super();
    }

    public WeightCategory(int category, int weight) {
        super();
        this.setCategory(category);
        this.setWeight(weight);
    }


    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}