package org.zgl.robot;

import org.quartz.Job;
import org.quartz.SchedulerException;
public class JobMgr {
	private static JobMgr jobMgr;
	private static Task_1 task_1 = new Task_1();
	private JobMgr() {

	}

	public Task_1 getTask_1() {
		return task_1;
	}

	public static JobMgr getInstance() {
		if (jobMgr == null) {
			jobMgr = new JobMgr();
		}
		return jobMgr;
	}

	/**
	 * @Title: initJobs
	 * @Description: 初始化所有任务
	 * @return void
	 * @throws
	 */
	public void initJobs(String jobName, String jobGroupName, String triggerGroupName, Class<? extends Job> job, int timer) {
		try {
			// 添加Job定时任务
			QuartzManager.addJob(jobName,jobGroupName,triggerGroupName, job,timer);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Title: stopJobs
	 * @Description: 关闭所有的任务
	 * @return void
	 * @throws
	 */

	public void stopJobs(String jobName, String jobGroupName, String triggerGroupName) {
		try {
			QuartzManager.removeJob(jobName,jobGroupName,triggerGroupName);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	private void initTask(){
		JobMgr.getInstance().initJobs(Task_1.class.getSimpleName(),
				"task_1Group","task_1TrggerGroup",Task_1.class,
				1);
	}
	public void start(){
		initTask();
	}
	public static void main(String[] args) throws InterruptedException {
//				JobMgr.getInstance().initJobs(Task_1.class.getSimpleName(),
//				"task_1Group","task_1TrggerGroup",new Task_1(),
//				"0/2 * * * * ?");
//		JobMgr.getInstance().initJobs(Task_1.class.getSimpleName(),
//				"task_2Group","task_2TrggerGroup",new Task_2(),
//				"0/1 * * * * ?");
//		Thread.sleep(5000);
		JobMgr.getInstance().initJobs(Task_1.class.getSimpleName(),
				"task_1Group","task_1TrggerGroup",Task_1.class,
				1);
//		JobMgr.getInstance().stopJobs(Task_2.class.getSimpleName(),"task_2Group","task_2TrggerGroup");
	}
}