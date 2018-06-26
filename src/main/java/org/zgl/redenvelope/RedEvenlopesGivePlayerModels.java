package org.zgl.redenvelope;

import org.zgl.utils.builder_clazz.ann.Protostuff;

import java.util.List;

/**
 * @作者： big
 * @创建时间： 2018/6/23
 * @文件描述：
 */
@Protostuff
public class RedEvenlopesGivePlayerModels {
    private List<RedEvenlopesGivePlayerModel> redEvenlopesGivePlayerModelList;

    public RedEvenlopesGivePlayerModels() {
    }

    public RedEvenlopesGivePlayerModels(List<RedEvenlopesGivePlayerModel> redEvenlopesGivePlayerModelList) {
        this.redEvenlopesGivePlayerModelList = redEvenlopesGivePlayerModelList;
    }

    public List<RedEvenlopesGivePlayerModel> getRedEvenlopesGivePlayerModelList() {
        return redEvenlopesGivePlayerModelList;
    }

    public void setRedEvenlopesGivePlayerModelList(List<RedEvenlopesGivePlayerModel> redEvenlopesGivePlayerModelList) {
        this.redEvenlopesGivePlayerModelList = redEvenlopesGivePlayerModelList;
    }
}
