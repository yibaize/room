package org.zgl.rooms.card;

import org.zgl.utils.builder_clazz.excel_init_data.DataTableMessage;
import org.zgl.utils.builder_clazz.excel_init_data.StaticConfigMessage;

public class CardDataTable implements DataTableMessage {
    private final int id;
    /**类型 红黑梅方*/
    private final int type;
    /**面值大小 1-13*/
    private final int face;
    public static CardDataTable get(int id){
        return StaticConfigMessage.getInstance().get(CardDataTable.class,id);
    }
    public CardDataTable() {
        this.id = 0;
        this.type = 0;
        this.face = 0;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public int getFace() {
        return face;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public void AfterInit() {

    }

    @Override
    public boolean equals(Object obj) {
        CardDataTable c = (CardDataTable) obj;
        return c.getId() == id;
    }

    @Override
    public String toString() {
        return "CardDataTable{" +
                "id=" + id +
                ", type=" + type +
                ", face=" + face +
                '}';
    }
}
