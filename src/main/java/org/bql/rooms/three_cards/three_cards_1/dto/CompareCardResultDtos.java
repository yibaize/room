package org.bql.rooms.three_cards.three_cards_1.dto;

import org.bql.utils.builder_clazz.ann.Protostuff;

import java.util.List;
@Protostuff
public class CompareCardResultDtos {
    /**牌局结束时所有玩家的牌的结果*/
    private List<CompareCardResultDto> resultDtos;

    public CompareCardResultDtos() {
    }

    public CompareCardResultDtos(List<CompareCardResultDto> resultDtos) {
        this.resultDtos = resultDtos;
    }

    public List<CompareCardResultDto> getResultDtos() {
        return resultDtos;
    }

    public void setResultDtos(List<CompareCardResultDto> resultDtos) {
        this.resultDtos = resultDtos;
    }
}
