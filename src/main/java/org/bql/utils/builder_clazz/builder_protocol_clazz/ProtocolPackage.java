package org.bql.utils.builder_clazz.builder_protocol_clazz;

import java.util.List;

public class ProtocolPackage implements Comparable{
    private int clazzId;
    private String clazzName;
    private List<String> field;

    public int getClazzId() {
        return clazzId;
    }

    public void setClazzId(int clazzId) {
        this.clazzId = clazzId;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public List<String> getField() {
        return field;
    }

    public void setField(List<String> field) {
        this.field = field;
    }

    @Override
    public int compareTo(Object o) {
        ProtocolPackage p = (ProtocolPackage) o;
        return clazzId - ((ProtocolPackage) o).getClazzId();
    }
}
