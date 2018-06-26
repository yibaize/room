package org.zgl.robot;

import org.zgl.error.GenaryAppError;
import org.zgl.rooms.RoomAbs;
import org.zgl.rooms.RoomFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

public class Task_1 implements Job {
    private final List<RoomAbs> rooms = RoomFactory.getInstance().allRoom();
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        for(RoomAbs r : rooms){
            //计时开始;
            r.submin(() -> {
                try {
                    r.timer();
                }catch (GenaryAppError e){
                    e.printStackTrace();
                }
            });
        }
//        LoggerUtils.getLogicLog().info("当前时间------>>>"+new Date().toLocaleString());
    }
}
