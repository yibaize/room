package org.bql.rooms.thousands_of.dto;

import org.bql.utils.builder_clazz.ann.Protostuff;

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
