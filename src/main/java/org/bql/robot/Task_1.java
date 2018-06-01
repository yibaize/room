package org.bql.robot;

import org.bql.rooms.RoomAbs;
import org.bql.rooms.RoomFactory;
import org.bql.rooms.three_cards.three_cards_1.manage.FRoomManager;
import org.bql.rooms.three_cards.three_cards_1.manage.FirstRooms;
import org.bql.utils.DateUtils;
import org.bql.utils.logger.LoggerUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.List;

public class Task_1 implements Job {
    private final List<RoomAbs> rooms = RoomFactory.getInstance().allRoom();
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        for(RoomAbs r : rooms){
            //计时开始;
            r.submin(() -> {
                r.timer();
            });
        }
//        LoggerUtils.getLogicLog().info("当前时间------>>>"+new Date().toLocaleString());
    }
}
