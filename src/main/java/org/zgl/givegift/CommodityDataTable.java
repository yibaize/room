package org.zgl.givegift;

import org.zgl.utils.builder_clazz.ann.DataTable;
import org.zgl.utils.builder_clazz.excel_init_data.DataTableMessage;
import org.zgl.utils.builder_clazz.excel_init_data.StaticConfigMessage;

/**
 * 商品表
 */
@DataTable
public class CommodityDataTable implements DataTableMessage {
    /**id*/
    private final int id;
    /**所属商城id*/
    private final int shopId;
    /**昵称*/
    private final String name;
    /**描述*/
    private final String describe;
    /**售价*/
    private final long selling;
    /**商品的作用，这里需要自己扩展*/
    private final String effect;
    /**消耗类型*/
    private final int consumeType;
    /**数量*/
    private final int count;
    /**vip等级限制*/
    private final int VIPLimitLv;
    /**积分赠送数量*/
    private final int integral;
    public static CommodityDataTable get(int id){
        return StaticConfigMessage.getInstance().get(CommodityDataTable.class,id);
    }
    public CommodityDataTable() {
        this.id = 0;
        this.shopId = 0;
        this.name = "";
        this.describe="";
        this.selling = 0L;
        this.effect = "";
        this.consumeType = 1;
        this.count = 0;
        this.VIPLimitLv = 0;
        this.integral = 0;
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public int getShopId() {
        return shopId;
    }
    public String getDescribe() {
        return describe;
    }

    public long getSelling() {
        return selling;
    }

    public String getEffect() {
        return effect;
    }

    public int getConsumeType() {
        return consumeType;
    }

    public int getVIPLimitLv() {
        return VIPLimitLv;
    }

    public int getIntegral() {
        return integral;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public void AfterInit() {

    }
}
