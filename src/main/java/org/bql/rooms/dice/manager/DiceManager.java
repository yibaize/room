package org.bql.rooms.dice.manager;

import org.bql.rooms.dice.data.DiceDataTable;
import org.bql.utils.builder_clazz.excel_init_data.StaticConfigMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DiceManager {
    private static DiceManager instance;
    private static final List<DiceDataTable> DICE_DATE;
    static {
        Map<Serializable,Object> o = StaticConfigMessage.getInstance().getMap(DiceDataTable.class);
        DICE_DATE = new ArrayList<>(o.size());
        for(Object ox : o.values()){
            DICE_DATE.add((DiceDataTable) ox);
        }
    }
    public static DiceManager getInstance() {
        if(instance == null){
            instance = new DiceManager();
        }
        return instance;
    }
    public List<DiceDataTable> getDiceDate(){
        return DICE_DATE;
    }
}
