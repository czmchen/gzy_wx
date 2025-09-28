package com.weixin.jobs;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.weixin.util.DateUtils;

@DisallowConcurrentExecution
public class GZYTestJob implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("mytest.....start..............."+DateUtils.nowDate(DateUtils.DATETIME_FORMATE));
		try {
			Thread.sleep(6000);
			System.out.println("mytest........6000............"+DateUtils.nowDate(DateUtils.DATETIME_FORMATE));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
