package org.bql.hall_connection.dto;

import org.bql.utils.builder_clazz.ann.Protostuff;

import java.util.List;

/**
 * 防止很多人跟新财富时要请求很多次的情况
 */
@Protostuff
public class RoomWeathDtos {
    List<RoomWeathDto> weathDtos;

    public RoomWeathDtos() {
    }

    public RoomWeathDtos(List<RoomWeathDto> weathDtos) {
        this.weathDtos = weathDtos;
    }

    public List<RoomWeathDto> getWeathDtos() {
        return weathDtos;
    }

    public void setWeathDtos(List<RoomWeathDto> weathDtos) {
        this.weathDtos = weathDtos;
    }
}
