package org.zgl.updatedate;

import org.zgl.error.AppErrorCode;
import org.zgl.error.GenaryAppError;
import org.zgl.net.server.manage.OperateCommandAbstract;
import org.zgl.utils.builder_clazz.ann.Protocol;
import org.zgl.utils.builder_clazz.excel_init_data.ExcelUtils;

/**
 * @作者： big
 * @创建时间： 2018/6/26
 * @文件描述：重新读取静态数据表
 */
@Protocol("1036")
public class TimingInitDataTable extends OperateCommandAbstract {
    @Override
    public Object execute() {
        try {
            //if() 不是管理员账号不能更新
            //重新读取静态数据表
            ExcelUtils.init("excel");
        }catch (Exception e){
            new GenaryAppError(AppErrorCode.DATA_ERR);
        }
        return null;
    }
}
