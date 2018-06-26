package org.zgl.rooms.dice.dto;

import org.zgl.utils.builder_clazz.ann.Protostuff;

import java.util.List;

/**
 * @作者： big
 * @创建时间： 2018/6/5
 * @文件描述：
 */
@Protostuff
public class DiceHistoryDto {
    List<DiceCountDto> diceCountDtos;

    public DiceHistoryDto() {
    }

    public DiceHistoryDto(List<DiceCountDto> diceCountDtos) {

        this.diceCountDtos = diceCountDtos;
    }

    public List<DiceCountDto> getDiceCountDtos() {
        return diceCountDtos;
    }

    public void setDiceCountDtos(List<DiceCountDto> diceCountDtos) {
        this.diceCountDtos = diceCountDtos;
    }
}
