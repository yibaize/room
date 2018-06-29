package org.zgl.rooms.dice.data;

import org.zgl.rooms.dice.dto.DiceCountDto;
import org.zgl.utils.RandomUtils;
import org.zgl.utils.StringUtils;
import org.zgl.utils.builder_clazz.ann.DataTable;
import org.zgl.utils.builder_clazz.excel_init_data.DataTableMessage;
import org.zgl.utils.builder_clazz.excel_init_data.StaticConfigMessage;
import org.zgl.utils.weightRandom.IWeihtRandom;

import java.util.Arrays;

@DataTable
public class DiceDataTable implements DataTableMessage,IWeihtRandom {
    private final int id;
    /**概率*/
    private final int probability;
    /**点数*/
    private final int count;
    private final DiceCountDto splitCount;
    public DiceDataTable() {
        this.id = 0;
        this.probability = 0;
        this.count = 0;
        this.splitCount = null;
    }
    public static DiceDataTable get(int id){
        return StaticConfigMessage.getInstance().get(DiceDataTable.class,id);
    }

    public int getCount() {
        return count;
    }
    public int getProbability() {
        return probability;
    }

    private DiceCountDto splitCount4Init(String value){
        DiceCountDto d = new DiceCountDto();
        String[] str = StringUtils.split(value,",");
        int one = Integer.parseInt(str[0]);
        int two = Integer.parseInt(str[1]);
        d.setOne(one);
        d.setTwo(two);
        return d;
    }

    public static void main(String[] args) {
        for(int i = 0;i<10;i++){
            System.out.println(RandomUtils.getRandom(0,1));
        }
    }

    public DiceCountDto getSplitCount() {
        return splitCount;
    }

    @Override
    public int id() {
        return id;
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
