package org.zgl.rooms.thousands_of.dto;

import org.zgl.utils.builder_clazz.ann.Protostuff;

import java.util.List;

@Protostuff
public class HistoryDtos {
    private List<HistoryDto> history;

    public HistoryDtos() {
    }

    public HistoryDtos(List<HistoryDto> history) {
        this.history = history;
    }

    public List<HistoryDto> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryDto> history) {
        this.history = history;
    }
}
