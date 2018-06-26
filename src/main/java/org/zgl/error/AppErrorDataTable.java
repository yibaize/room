package org.zgl.error;

import org.zgl.utils.builder_clazz.ann.DataTable;
import org.zgl.utils.builder_clazz.excel_init_data.DataTableMessage;
import org.zgl.utils.builder_clazz.excel_init_data.StaticConfigMessage;

/**
 * 作者： 白泽
 * 时间： 2017/11/13.
 * 描述：
 */
@DataTable
public class AppErrorDataTable implements DataTableMessage {
    private final int id;
    private final String name;
    private final String value;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public AppErrorDataTable() {
        this.id = 0;
        this.name = "";
        this.value = "";
    }
    public static AppErrorDataTable get(int id){
        return StaticConfigMessage.getInstance().get(AppErrorDataTable.class,id);
    }
    @Override
    public int id() {
        return id;
    }

    @Override
    public void AfterInit() {

    }
}
