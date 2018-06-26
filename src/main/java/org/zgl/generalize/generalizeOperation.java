package org.zgl.generalize;

import org.zgl.error.AppErrorCode;
import org.zgl.error.GenaryAppError;
import org.zgl.net.server.manage.OperateCommandAbstract;
import org.zgl.remote.HttpProxyOutboundHandler;
import org.zgl.remote.IGeneralize;

/**
 * @作者： big
 * @创建时间： 2018/6/15
 * @文件描述：
 */
public class generalizeOperation extends OperateCommandAbstract {
    /**推广人的uid*/
    private final long targetUid;
    public generalizeOperation(long targetUid) {
        this.targetUid = targetUid;
    }

    @Override
    public Object execute() {
        IGeneralize generalize = HttpProxyOutboundHandler.getRemoteProxyObj(IGeneralize.class);
//        if(!generalize.generalize(targetUid)){
//            //没有这个uid 推广失败
//            new GenaryAppError(AppErrorCode.NOT_UID);
//        }
        //推广成功
        //通知所有人
        return null;
    }
}
