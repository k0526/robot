package com.playcrab.core.thread;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.playcrab.core.robot.BaseRobot;
import com.playcrab.core.service.RobotCoreManager;

/**
 * 机器人轮询线程
 */
public class RobotLoopThread extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(RobotLoopThread.class);

	/** 线程ID */
	private int threadId = 0;

	/** 是否运行 */
	private volatile boolean run = true;

	/**
	 * 机器人轮询线程
	 * 
	 * @param threadId
	 *            线程ID
	 */
	public RobotLoopThread(int threadId) {
		this.threadId = threadId;
		setName("robot-loop-thread-" + threadId);
	}

	@Override
	public void run() {
		while (run) {
			ArrayList<BaseRobot> robotByThreadId = RobotCoreManager.getInstance().getRobotByThreadId(threadId);
			if (robotByThreadId == null || robotByThreadId.size() <= 0) {
				try {
					sleep(5000);//睡眠时间，也就是间隔时间
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			long start = System.currentTimeMillis();
			robotByThreadId.stream().forEach(robot -> {
				try {
					robot.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			long end = System.currentTimeMillis();
			long useTime = end - start;
			if (useTime < 1000) {
				try {
					sleep(1000 - useTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void setRun(boolean run) {
		this.run = run;
	}

}
