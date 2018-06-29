package org.zgl.redenvelope.cmd;

import org.zgl.error.AppErrorCode;
import org.zgl.error.GenaryAppError;
import org.zgl.net.server.manage.OperateCommandAbstract;
import org.zgl.redenvelope.dto.DBRedEvenlopes;
import org.zgl.redenvelope.dto.RedEnvelopesDto;
import org.zgl.remote.HttpProxyOutboundHandler;
import org.zgl.remote.IRedEvenlopes;
import org.zgl.utils.builder_clazz.ann.Protocol;
import org.zgl.utils.logger.LoggerUtils;

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
        RedEnvelopesDto redEnvelopesDto = null;
        try {
            redEnvelopesDto = redEvenlopes.queryRedEvenlopesList(0);
        }catch (Exception e){
            new GenaryAppError(AppErrorCode.SERVER_ERR);
        }
        if(redEnvelopesDto == null)
            redEnvelopesDto = new RedEnvelopesDto();
//        LoggerUtils.getLogicLog().info(redEnvelopesDto.getChatDtos().size());
        return redEnvelopesDto;
    }
}
