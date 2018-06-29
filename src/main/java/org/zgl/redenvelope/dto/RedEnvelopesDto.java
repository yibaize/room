package org.zgl.redenvelope.dto;

import org.zgl.utils.builder_clazz.ann.Protostuff;

import java.util.List;

/**
 * @作者： big
 * @创建时间： 2018/6/22
 * @文件描述：
 */
@Protostuff
public class RedEnvelopesDto {
    private List<DBRedEvenlopes> chatDtos;

    public RedEnvelopesDto() {
    }

    public RedEnvelopesDto(List<DBRedEvenlopes> chatDtos) {
        this.chatDtos = chatDtos;
    }

    public List<DBRedEvenlopes> getChatDtos() {
        return chatDtos;
    }

    public void setChatDtos(List<DBRedEvenlopes> chatDtos) {
        this.chatDtos = chatDtos;
    }
}
