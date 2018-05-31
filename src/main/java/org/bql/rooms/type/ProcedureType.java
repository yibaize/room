package org.bql.rooms.type;

/**
 * @作者： big
 * @创建时间： 2018/5/22
 * @文件描述：手续费类型 输的收百分之1 赢的收百分之1
 */
public enum ProcedureType {
    WIN(0.05F,1),
    LOSE(0.01F,2);
    private float id;
    private int result;
    private ProcedureType(float id,int result) {
        this.id = id;
        this.result = result;
    }
    public float id(){
        return id;
    }
    public int getResult(){
        return result;
    }
}
