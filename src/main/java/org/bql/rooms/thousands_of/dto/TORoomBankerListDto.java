package org.bql.rooms.thousands_of.dto;

import org.bql.player.PlayerRoomBaseInfoDto;
import org.bql.utils.builder_clazz.ann.Protostuff;

import java.util.List;

/**
 * @作者： big
 * @创建时间： 2018/5/23
 * @文件描述：上庄列表
 */
@Protostuff
public class TORoomBankerListDto {
    private List<TOBankerListDto> bankers;

    public TORoomBankerListDto() {
    }

    public TORoomBankerListDto(List<TOBankerListDto> bankers) {
        this.bankers = bankers;
    }

    public List<TOBankerListDto> getBankers() {
        return bankers;
    }

    public void setBankers(List<TOBankerListDto> bankers) {
        this.bankers = bankers;
    }
}
