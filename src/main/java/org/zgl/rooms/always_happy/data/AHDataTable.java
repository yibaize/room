package org.zgl.rooms.always_happy.data;

import org.zgl.utils.builder_clazz.excel_init_data.DataTableMessage;
import org.zgl.utils.weightRandom.IWeihtRandom;

/**
 * @作者： big
 * @创建时间： 2018/5/24
 * @文件描述：
 */
public class AHDataTable implements DataTableMessage ,IWeihtRandom {
    private final int id;
    /**概率*/
    private final int probability;
    public AHDataTable(){
        this.id = 0;
        this.probability = 0;
    }
    @Override
    public int id() {
        return id;
    }

    public int getId() {
        return id;
    }

    public int getProbability() {
        return probability;
    }

    @Override
    public void AfterInit() {

    }

    @Override
    public int elementId() {
        return id;
    }

    @Override
    public int probability() {
        return probability;
    }
}
