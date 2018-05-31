package org.bql.rooms.thousands_of.dto;

import org.bql.utils.builder_clazz.ann.Protostuff;

@Protostuff
public class PlayerWeathUpdateDto {
    private long weath;

    public PlayerWeathUpdateDto() {
    }

    public PlayerWeathUpdateDto(long weath) {
        this.weath = weath;
    }

    public long getWeath() {
        return weath;
    }

    public void setWeath(long weath) {
        this.weath = weath;
    }
}
