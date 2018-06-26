package org.zgl.remote;

import org.zgl.player.PlayerInfoDto;

import java.util.List;

/**
 * @作者： big
 * @创建时间： 2018/6/15
 * @文件描述：
 */
public interface IBackHall {
    short backHall(List<PlayerInfoDto> weathDtos);
}
