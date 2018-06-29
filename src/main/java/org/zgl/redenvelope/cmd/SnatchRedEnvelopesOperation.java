package org.zgl.redenvelope.cmd;

import org.zgl.error.AppErrorCode;
import org.zgl.error.GenaryAppError;
import org.zgl.net.server.manage.OperateCommandAbstract;
import org.zgl.player.PlayerInfoDto;
import org.zgl.player.PlayerRoom;
import org.zgl.redenvelope.dto.DBRedEvenlopes;
import org.zgl.redenvelope.dto.DrawRedEvenlopesPlayerDto;
import org.zgl.redenvelope.model.RedEvenlopesGivePlayerModel;
import org.zgl.remote.HttpProxyOutboundHandler;
import org.zgl.remote.IBackHall;
import org.zgl.remote.IRedEvenlopes;
import org.zgl.utils.DateUtils;
import org.zgl.utils.JsonUtils;
import org.zgl.utils.RandomUtils;
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

        List<RedEvenlopesGivePlayerModel> playerModelList = JsonUtils.jsonParseList(dbRedEvenlopes.getGivePlayer(),RedEvenlopesGivePlayerModel.class);
        if(playerModelList == null){
            playerModelList = new ArrayList<>();
        }

        //平均分
        PlayerRoom player = (PlayerRoom) getSession().getAttachment();
        PlayerInfoDto infoDto = player.getPlayer();
        for(RedEvenlopesGivePlayerModel re:playerModelList){
            if(re.getUid() == infoDto.getUid())
                new GenaryAppError(AppErrorCode.RED_ENVELOPES_IS_GET);
        }
        if(dbRedEvenlopes == null)
            new GenaryAppError(AppErrorCode.DATA_ERR);//没有这个红包
        if (dbRedEvenlopes.getNum() == null || dbRedEvenlopes.getNum() <= 0){
            //红包没了
            new GenaryAppError(AppErrorCode.DATA_ERR);
        }
        int getGold = 0;
        if(dbRedEvenlopes.getRedEvenlopesType() == 1){
            //随机
            int num = dbRedEvenlopes.getNum();
            int money = dbRedEvenlopes.getResidueGold();
            if(num == 1){
                getGold = money;
                dbRedEvenlopes.setLastEditTime(DateUtils.currentTime());
            }else {
                //随机获取金币
                getGold = RandomUtils.getRandom(1,money);
            }
            dbRedEvenlopes.setResidueGold(money - getGold);
            dbRedEvenlopes.setNumed(dbRedEvenlopes.getNumed() + 1);
            dbRedEvenlopes.setNum(num - 1);

        }else if(dbRedEvenlopes.getRedEvenlopesType() == 2){
            int num = dbRedEvenlopes.getNum();
            int money = dbRedEvenlopes.getResidueGold();
            if(num == 1){
                getGold = money;
                dbRedEvenlopes.setLastEditTime(DateUtils.currentTime());
            }else {
                getGold = money / num;
            }
            dbRedEvenlopes.setResidueGold(money - getGold);
            dbRedEvenlopes.setNumed(dbRedEvenlopes.getNumed() + 1);
            dbRedEvenlopes.setNum(num - 1);
        }

        RedEvenlopesGivePlayerModel playerModel = new RedEvenlopesGivePlayerModel();
        playerModel.setGetCount(getGold);
        playerModel.setHeadIcon(infoDto.getHeadIcon());
        playerModel.setUid(infoDto.getUid());
        playerModel.setUserName(infoDto.getUsername());
        playerModel.setCreateTime(System.currentTimeMillis()+"");

        playerModelList.add(playerModel);
        infoDto.insertGold(getGold);
        dbRedEvenlopes.setGivePlayer(JsonUtils.jsonSerialize(playerModelList));
        IRedEvenlopes iRedEvenlopes1 = HttpProxyOutboundHandler.getRemoteProxyObj(IRedEvenlopes.class);
        iRedEvenlopes1.updateRedEvenlopes(dbRedEvenlopes);

        IBackHall iBackHall = HttpProxyOutboundHandler.getRemoteProxyObj(IBackHall.class);
        List<PlayerInfoDto> l = new ArrayList<>();
        l.add(infoDto);
        iBackHall.backHall(l);
        return new DrawRedEvenlopesPlayerDto(infoDto.getUid(),dbRedEvenlopes.getUserName(),getGold,infoDto.getGold());
    }

    public static void main(String[] args) {
        System.out.println(new java.util.Date().getTime());
        System.out.println(System.currentTimeMillis());
    }
}
