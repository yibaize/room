package org.bql.robot;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.ParseException;

public class QuartzManager {
	private static SchedulerFactory sf = new StdSchedulerFactory();
	/**
	 * @Title: addJob
	 * @Description: 添加Job
	 * @param jobName
	 * @param jobGroupName
	 * @param triggerGroupName
	 * @param job
	 * @param
	 * @throws SchedulerException
	 * @throws ParseException
	 * @return void
	 * @throws
	 */
	public static void addJob(String jobName, String jobGroupName, String triggerGroupName,Class<? extends Job> job, int timer)throws SchedulerException {
		Scheduler sched = sf.getScheduler();
		JobDetail jobDetail = JobBuilder.newJob(job).withIdentity(jobName,jobGroupName).build();	// 任务名，任务组，任务执行类
		Trigger trigger = TriggerBuilder.newTrigger().
				withIdentity(jobName,triggerGroupName)// 触发器名,触发器组
				.startNow()//立即执行
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInSeconds(timer)//设置时间
						.repeatForever()).build();
		// 触发器
		sched.scheduleJob(jobDetail, trigger);
		if (!sched.isShutdown())
			sched.start();
	}

	/**
	 * @Title: modifyJobTime
	 * @Description: 修改任务触发时间
	 * @param triggerName
	 * @param triggerGroupName
	 * @param time
	 * @throws SchedulerException
	 * @throws ParseException
	 * @return void
	 * @throws
	 */
	public static void modifyJobTime(String triggerName,String triggerGroupName, String time) throws SchedulerException,ParseException {
//		Scheduler sched = sf.getScheduler();
//		Trigger trigger = sched.getTrigger(triggerName, triggerGroupName);
//		if (trigger != null) {
//			CronTrigger ct = (CronTrigger) trigger;
//			// 修改时间
//			ct.setCronExpression(time);
//			// 重启触发器
//			sched.resumeTrigger(triggerName, triggerGroupName);
//		}
	}
	/**
	 * @Title: removeJob
	 * @Description: 移除任务
	 * @param jobName
	 * @param jobGroupName
	 * @param triggerGroupName
	 * @throws SchedulerException
	 * @return void
	 * @throws
	 */
	public static void removeJob(String jobName, String jobGroupName, String triggerGroupName)
			throws SchedulerException {
//		Scheduler sched = sf.getScheduler();
//		sched.pauseTrigger(jobName, triggerGroupName);// 停止触发器
//		sched.unscheduleJob(jobName, triggerGroupName);// 移除触发器
//		sched.deleteJob(jobName, jobGroupName);// 删除任务
	}

}