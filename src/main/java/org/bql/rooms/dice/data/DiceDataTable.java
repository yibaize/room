package org.bql.rooms.dice.data;

import org.bql.utils.builder_clazz.ann.DataTable;
import org.bql.utils.builder_clazz.excel_init_data.DataTableMessage;
import org.bql.utils.builder_clazz.excel_init_data.StaticConfigMessage;
import org.bql.utils.weightRandom.IWeihtRandom;

@DataTable
public class DiceDataTable implements DataTableMessage,IWeihtRandom {
    private final int id;
    /**概率*/
    private final int probability;
    public DiceDataTable() {
        this.id = 0;
        this.probability = 0;
    }
    public static DiceDataTable get(int id){
        return StaticConfigMessage.getInstance().get(DiceDataTable.class,id);
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
