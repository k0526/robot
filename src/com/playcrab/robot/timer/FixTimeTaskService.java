package com.playcrab.robot.timer;

import com.playcrab.core.timer.FixTimeScheduler;
import com.playcrab.robot.timer.tasks.LogMethodStatusAction;

public class FixTimeTaskService {
	
	private static FixTimeTaskService instance=new FixTimeTaskService();
	
	public static FixTimeTaskService getInstance() {
		return instance;
	}
	
	private FixTimeScheduler fixTimeScheduler=new FixTimeScheduler();
	
	private FixTimeTaskService() {
		fixTimeScheduler.addSchedulerTask("0 */1 * * * ?", LogMethodStatusAction.class);
		
		fixTimeScheduler.start();
	}
	
	public void shutdown() {
		fixTimeScheduler.shutdown();
	}
	
	
}
