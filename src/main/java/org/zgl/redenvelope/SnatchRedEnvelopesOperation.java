package org.zgl.redenvelope;

import org.zgl.error.AppErrorCode;
import org.zgl.error.GenaryAppError;
import org.zgl.hall_connection.BackHall;
import org.zgl.net.server.manage.OperateCommandAbstract;
import org.zgl.player.PlayerInfoDto;
import org.zgl.player.PlayerRoom;
import org.zgl.remote.HttpProxyOutboundHandler;
import org.zgl.remote.IBackHall;
import org.zgl.remote.IRedEvenlopes;
import org.zgl.rooms.thousands_of.dto.PlayerWeathUpdateDto;
import org.zgl.utils.builder_clazz.ann.Protocol;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者： big
 * @创建时间： 2018/6/22
 * @文件描述：
 */
@Protocol("1034")
public class SnatchRedEnvelopesOperation extends OperateCommandAbstract {
    private final int redEvenlopesId;
    public SnatchRedEnvelopesOperation(int redEvenlopesId) {
        this.redEvenlopesId = redEvenlopesId;
    }

    @Override
    public Object execute() {
        IRedEvenlopes iRedEvenlopes = HttpProxyOutboundHandler.getRemoteProxyObj(IRedEvenlopes.class);
        DBRedEvenlopes dbRedEvenlopes = iRedEvenlopes.queryRedEvenlopesById(redEvenlopesId);
        if(dbRedEvenlopes == null)
            new GenaryAppError(AppErrorCode.DATA_ERR);//没有这个红包
        if (dbRedEvenlopes.getNum() == null || dbRedEvenlopes.getNum() <= 0){
            //红包没了
            new GenaryAppError(AppErrorCode.DATA_ERR);
        }
        int getGold = 0;
        if(dbRedEvenlopes.getRedEvenlopesType() == 1){
            //随机

        }else if(dbRedEvenlopes.getRedEvenlopesType() == 2){
            int num = dbRedEvenlopes.getNum();
            int money = dbRedEvenlopes.getResidueGold();
            if(num == 1){
                getGold = money;
            }else {
                getGold = money / num;
            }
            dbRedEvenlopes.setResidueGold(money - getGold);
            dbRedEvenlopes.setNumed(dbRedEvenlopes.getNumed() + 1);
            dbRedEvenlopes.setNum(num - 1);
        }
        //平均分
        PlayerRoom player = (PlayerRoom) getSession().getAttachment();
        PlayerInfoDto infoDto = player.getPlayer();
        return null;
    }
}
