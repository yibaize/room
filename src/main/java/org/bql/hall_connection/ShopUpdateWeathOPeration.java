package org.bql.hall_connection;

import org.bql.error.AppErrorCode;
import org.bql.error.GenaryAppError;
import org.bql.hall_connection.dto.RoomWeathDto;
import org.bql.hall_connection.dto.ShopBuySyncDto;
import org.bql.net.builder_clazz.NotifyCode;
import org.bql.net.http.HttpClient;
import org.bql.net.server.manage.OperateCommandAbstract;
import org.bql.player.PlayerFactory;
import org.bql.player.PlayerInfoDto;
import org.bql.player.PlayerRoom;
import org.bql.utils.builder_clazz.ann.Protocol;

/**
 * @作者： big
 * @创建时间： 2018/5/25
 * @文件描述：
 */
@Protocol("10008")
public class ShopUpdateWeathOPeration extends OperateCommandAbstract {
    @Override
    public Object execute() {
        PlayerRoom player = (PlayerRoom) getSession().getAttachment();
        PlayerInfoDto dto = player.getPlayer();
        ShopBuySyncDto weathDto = HttpClient.getInstance().syncPost(NotifyCode.SHOP_WEATH_UPDATE,dto.getAccount()+","+PlayerFactory.SYSTEM_PLAYER_ID,ShopBuySyncDto.class);
        if(weathDto != null){
            dto.setGold(weathDto.getGold());
            dto.setDiamond(weathDto.getDiamond());
            dto.setIntegral(weathDto.getIntegral());
            dto.setVipLv(weathDto.getVipLv());
        }else {
            new GenaryAppError(AppErrorCode.DATA_ERR);
        }
        return null;
    }
}
