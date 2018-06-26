package org.zgl.rooms.thousands_of.dto;

import org.zgl.utils.builder_clazz.ann.Protostuff;

@Protostuff
public class PositionDto {
    private int position;

    public PositionDto() {
    }

    public PositionDto(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
