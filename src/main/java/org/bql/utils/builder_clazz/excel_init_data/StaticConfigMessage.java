package org.bql.utils.builder_clazz.excel_init_data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StaticConfigMessage {
    private Map<Class<?>,Map<Serializable,Object>> allData = new HashMap<>();
    private static StaticConfigMessage instance;
    public static StaticConfigMessage getInstance(){
        if(instance == null)
            instance = new StaticConfigMessage();
        return instance;
    }

    /**
     * 添加导表数据
     * @param clazz
     * @param data
     */
    public void put(Class<?> clazz,Map<Serializable,Object> data){
        allData.put(clazz,data);
    }

    /**
     *获取导表数据对应的类
     * @param clazz
     * @param id
     * @param <T>
     * @return
     */
    public <T> T get(Class<T> clazz, Serializable id){
        Map<Serializable,Object> map = allData.get(clazz);
        if(map == null)
            return null;
        return (T) map.get(id);
    }
    public Map<Serializable,Object> getMap(Class<?> clazz){
        if(!allData.containsKey(clazz))
            return new HashMap<>();
        return allData.get(clazz);
    }
}