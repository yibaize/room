package org.zgl.remote;


import org.zgl.redenvelope.dto.DBRedEvenlopes;
import org.zgl.redenvelope.dto.FriendRedEnvelopesDto;
import org.zgl.redenvelope.dto.RedEnvelopesDto;

import java.util.List;

/**
 * @作者： big
 * @创建时间： 2018/6/23
 * @文件描述：红包操作
 */
public interface IRedEvenlopes {
    int insertRedEvenlopes(DBRedEvenlopes evenlopes);
    int updateRedEvenlopes(DBRedEvenlopes evenlopes);
    DBRedEvenlopes queryRedEvenlopesById(Integer id);
    DBRedEvenlopes queryRedEvenlopesLastId();
    RedEnvelopesDto queryRedEvenlopesList(Integer id);
    int deleteRedEvenlopes(Integer id);

    int handOutRedEnvelopes(Long uid,FriendRedEnvelopesDto friendRedEnvelopesDto);
    int updateRedEnvelopes(Long uid,Integer id);
    List<FriendRedEnvelopesDto> friendRedEnvelopesList(Long uid);
}
