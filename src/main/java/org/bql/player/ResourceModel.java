package org.bql.player;


import org.bql.utils.builder_clazz.ann.Protostuff;
@Protostuff
public class ResourceModel {
    /**资源id*/
    private int id;
    /**类型 道具、座驾、道具 ：对应商城id*/
    private int type;
    /**拥有数量*/
    private int count;

    public ResourceModel() {
    }

    public ResourceModel(int id, int type, int count) {
        this.id = id;
        this.type = type;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    public boolean reduce(int num){
        if(count < num)
            return false;
        count -= num;
        return true;
    }
}
