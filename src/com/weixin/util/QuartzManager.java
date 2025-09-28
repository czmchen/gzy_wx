package com.weixin.util;

import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

/****
 * 
 * @author 陈周敏
 * @date 2017年3月21日 上午11:14:39
 * @Description 定时任务管理类
 * @version V1.0
 */
public class QuartzManager {

	private static final String QUARTZ_SCHEDULER = "scheduler";

	/*****
	 * 
	 * @author 陈周敏
	 * @date 2017年3月21日 上午11:05:49
	 * @description
	 * @param jobName   任务名
	 * @param jobClass  任务
	 * @param corn      时间设置，参考quartz说明文档
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void addJob(String jobName, String triggerGroup, Class jobClass, String corn, Date startTime,
			Date endTime) {
		try {
			Scheduler sched = ((org.quartz.impl.StdScheduler) SpringUtil.getBean(QUARTZ_SCHEDULER));// 从DataSource取值 new
			// StdSchedulerFactory().getScheduler();
			TriggerBuilder objTriggerBuilder = TriggerBuilder.newTrigger();
			JobDetail job = JobBuilder.newJob(jobClass).withIdentity(jobName, Scheduler.DEFAULT_GROUP).build();// 任务名，任务组，任务执行类
			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(corn);// corn表达式设定

			objTriggerBuilder.withIdentity(triggerGroup, Scheduler.DEFAULT_GROUP).withSchedule(cronScheduleBuilder);

			if (startTime != null) {
				objTriggerBuilder.startAt(startTime);
			}
			if (endTime != null) {
				objTriggerBuilder.endAt(startTime);
			}

			CronTrigger trigger = (CronTrigger) objTriggerBuilder.build();// 触发器名,触发器组
			sched.scheduleJob(job, trigger);

			// 启动
			if (!sched.isShutdown()) {
				sched.start();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*****
	 * 
	 * @author 陈周敏
	 * @date 2017年3月21日 上午11:05:49
	 * @description
	 * @param jobName  任务名
	 * @param jobClass 任务
	 * @param corn     时间设置，参考quartz说明文档
	 */
	@SuppressWarnings("rawtypes")
	public static void addJob(String jobName, String triggerGroup, Class jobClass, String corn) {
		addJob(jobName, triggerGroup, jobClass, corn, null, null);
	}

	/**
	 * @Description: 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
	 * 
	 * @param jobName
	 * @param time
	 * @version V2.0
	 */
	@SuppressWarnings("rawtypes")
	public static void updateJobTime(String jobName, String triggerGroup, String corn) {
		try {
			Scheduler sched = ((org.quartz.impl.StdScheduler) SpringUtil.getBean(QUARTZ_SCHEDULER));// 从DataSource取
																									// new
																									// StdSchedulerFactory().getScheduler();
			CronTrigger trigger = (CronTrigger) sched.getTrigger(new TriggerKey(jobName, triggerGroup));
			if (trigger == null) {
				return;
			}
			String oldTime = trigger.getCronExpression();
			if (!oldTime.equalsIgnoreCase(corn)) {
				JobDetail jobDetail = sched.getJobDetail(JobKey.jobKey(jobName, Scheduler.DEFAULT_GROUP));
				Class objJobClass = jobDetail.getJobClass();
				removeJob(jobName, triggerGroup);
				addJob(jobName, triggerGroup, objJobClass, corn);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*****
	 * 
	 * @author 陈周敏
	 * @date 2017年3月21日 上午11:42:09
	 * @description 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
	 * @param jobName
	 */
	public static void removeJob(String jobName, String triggerGroup) {
		try {
			Scheduler sched = ((org.quartz.impl.StdScheduler) SpringUtil.getBean(QUARTZ_SCHEDULER));// Scheduler
																									// sched
																									// = new
																									// StdSchedulerFactory().getScheduler();
			sched.deleteJob(JobKey.jobKey(jobName));// 删除任务
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/****
	 * 
	 * @author 陈周敏
	 * @date 2017年3月21日 上午11:52:09
	 * @description 启动所有定时任务
	 */
	public static void startJobs() {
		try {
			Scheduler sched = ((org.quartz.impl.StdScheduler) SpringUtil.getBean(QUARTZ_SCHEDULER));// new
																									// StdSchedulerFactory().getScheduler();
			sched.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/****
	 * 
	 * @author 陈周敏
	 * @date 2017年3月21日 上午11:52:25
	 * @description 关闭所有定时任务
	 */
	public static void shutdownJobs() {
		try {
			Scheduler sched = ((org.quartz.impl.StdScheduler) SpringUtil.getBean(QUARTZ_SCHEDULER));// new
																									// StdSchedulerFactory().getScheduler();
			if (!sched.isShutdown()) {
				sched.shutdown();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}