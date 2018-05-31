package org.bql.hall_connection;

import org.bql.net.client.GameClient;
import org.bql.net.http.HttpClient;
import org.bql.net.message.ClientRequest;
import org.bql.net.message.ClientResponse;
import org.bql.net.message.Msg;
import org.bql.net.server.manage.OperateCommandAbstract;
import org.bql.player.PlayerRoom;
import org.bql.utils.ProtostuffUtils;
import org.bql.utils.builder_clazz.ann.Protocol;

/**
 * 商城购买东西
 */
@Protocol("7")
public class ShopBuyOperation extends OperateCommandAbstract {
    private final int commodityId;

    public ShopBuyOperation(int commodityId) {
        this.commodityId = commodityId;
    }

    @Override
    public Object execute() {
        PlayerRoom p = (PlayerRoom) getSession().getAttachment();
        Msg m = new Msg(p.getPlayer().getAccount()+","+commodityId);
        byte[] buf = ProtostuffUtils.serializer(m);
        ClientRequest response = new ClientRequest(getCmdId(),buf);
        //请求大厅数据
        ClientResponse response1 = GameClient.getInstance().writeAndFuture(response);
        return null;
    }
}
