package org.zgl.rooms.thousands_of.dto;

import org.zgl.utils.builder_clazz.ann.Protostuff;

import java.util.List;

/**
 * @作者： big
 * @创建时间： 2018/6/21
 * @文件描述：
 */
@Protostuff
public class JackpotDtoList {
    private List<JackpotDto> jackpotDtos;

    public JackpotDtoList() {
    }

    public JackpotDtoList(List<JackpotDto> jackpotDtoList) {
        this.jackpotDtos = jackpotDtoList;
    }

    public List<JackpotDto> getJackpotDtos() {
        return jackpotDtos;
    }

    public void setJackpotDtos(List<JackpotDto> jackpotDtos) {
        this.jackpotDtos = jackpotDtos;
    }
}
