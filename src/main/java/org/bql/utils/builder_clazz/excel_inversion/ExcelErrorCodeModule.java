package org.bql.utils.builder_clazz.excel_inversion;

import org.bql.utils.builder_clazz.ann.DataTable;
import org.bql.utils.builder_clazz.excel_init_data.DataTableMessage;

@DataTable
public class ExcelErrorCodeModule implements DataTableMessage {
    private final int id;
    private final String name;
    private final String value;

    public ExcelErrorCodeModule() {
        this.id = 0;
        this.name = "";
        this.value = "";
    }

    public ExcelErrorCodeModule(int id, String name, String value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public void AfterInit() {

    }
}