package org.zgl.rooms.thousands_of.dto;

import org.zgl.utils.builder_clazz.ann.Protostuff;

/**
 * 历史记录
 */
@Protostuff
public class HistoryDto {
    /**当前第几局*/
    private long count;
    private boolean one;
    private boolean two;
    private boolean three;
    private boolean four;

    public HistoryDto() {
    }

    public HistoryDto(long count, boolean one, boolean two, boolean three, boolean four) {
        this.count = count;
        this.one = one;
        this.two = two;
        this.three = three;
        this.four = four;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public boolean isOne() {
        return one;
    }

    public void setOne(boolean one) {
        this.one = one;
    }

    public boolean isTwo() {
        return two;
    }

    public void setTwo(boolean two) {
        this.two = two;
    }

    public boolean isThree() {
        return three;
    }

    public void setThree(boolean three) {
        this.three = three;
    }

    public boolean isFour() {
        return four;
    }

    public void setFour(boolean four) {
        this.four = four;
    }
}
