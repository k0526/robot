package com.playcrab.core.timer;

import java.util.ArrayList;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *
 */
public class FixTimeScheduler {
	
	private static final Logger logger=LoggerFactory.getLogger(FixTimeScheduler.class);
	
	private Scheduler scheduler;
	private int count=0;
	private Object lock=new Object();
	private List<SchedulerInfo> infos=new ArrayList<SchedulerInfo>();
	
	public void start() {
		try {
			synchronized (lock) {
				scheduler=StdSchedulerFactory.getDefaultScheduler();
				scheduler.start();
				for(int i=0;i<infos.size();i++) {
					SchedulerInfo info=infos.get(i);
					try {
						scheduler.scheduleJob(info.getJob(),info.getTrigger());
					} catch (Exception e) {
						logger.error("",e);
					}
				}
			}
		} catch (Exception e) {
			logger.error("",e);
		}
	}
	
	public Scheduler getScheduler() {
		return scheduler;
	}
	
	public void shutdown() {
		try {
			scheduler.shutdown();
		} catch (Exception e) {
			logger.error("",e);
		}
	}
	
	public void addSchedulerTask(String cron,Class<? extends FixTimeAction> cls) {
		count+=1;
		JobDetail job = JobBuilder.newJob(SchedulerTask.class).withIdentity("job"+count,"SchedulerTaskGroup").usingJobData("className",cls.getCanonicalName()).build();
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger"+count,"SchedulerTaskGroup").withSchedule(CronScheduleBuilder.cronSchedule(cron)).forJob("job"+count,"SchedulerTaskGroup").build();
		synchronized (lock) {
			if(scheduler==null) {
				infos.add(new SchedulerInfo(job, trigger));
			} else {
				try {
					scheduler.scheduleJob(job, trigger);	
				} catch (Exception e) {
					logger.error("",e);
				}
			}
		}
		
		
	}
	
	
	
	
	private class SchedulerInfo {
		private JobDetail job;
		private Trigger trigger;
		
		public SchedulerInfo(JobDetail job,Trigger trigger) {
			this.job=job;
			this.trigger=trigger;
		}
		
		public JobDetail getJob() {
			return this.job;
		}
		
		public Trigger getTrigger() {
			return this.trigger;
		}
	}
	
	
	
}





