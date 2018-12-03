package com.playcrab.core.timer;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SchedulerTask implements Job{
	
	private static final Logger logger=LoggerFactory.getLogger(SchedulerTask.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobData=context.getMergedJobDataMap();
		String className= jobData.getString("className");
		try {
			@SuppressWarnings("rawtypes")
			Class cls = Class.forName(className);
			FixTimeAction newInstance=(FixTimeAction)cls.newInstance();
			newInstance.action();
		} catch (Exception e) {
			logger.error("",e);
		}
		
		
	}
	
}
