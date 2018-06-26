package org.zgl.redenvelope;

import org.zgl.error.AppErrorCode;
import org.zgl.error.GenaryAppError;
import org.zgl.net.server.manage.OperateCommandAbstract;
import org.zgl.remote.HttpProxyOutboundHandler;
import org.zgl.remote.IRedEvenlopes;
import org.zgl.utils.builder_clazz.ann.Protocol;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者： big
 * @创建时间： 2018/6/22
 * @文件描述：
 */
@Protocol("1032")
public class OpenRedEnvelopesOperation extends OperateCommandAbstract {
    @Override
    public Object execute() {
        IRedEvenlopes redEvenlopes = HttpProxyOutboundHandler.getRemoteProxyObj(IRedEvenlopes.class);
        List<DBRedEvenlopes> list = new ArrayList<>();
        try {
            list = redEvenlopes.queryRedEvenlopesList(0);
        }catch (Exception e){
            new GenaryAppError(AppErrorCode.SERVER_ERR);
        }
        return new RedEnvelopesDto(list);
    }
}
