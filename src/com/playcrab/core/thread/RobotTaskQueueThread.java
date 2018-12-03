package com.playcrab.core.thread;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 这个类，就是一个线程写了一个死循环
 * 然后，可以给这个线程添加需要执行的任务
 * 也可以不天添加，那这个线程就一直等待接受任务中
 * @author xjp
 * @date 2018年10月8日 下午3:41:08 
 */
public class RobotTaskQueueThread extends Thread{
	
	private int threadId=0;
	
	private volatile boolean run=true;
	
	private LinkedBlockingQueue<Runnable> taskQueue=new LinkedBlockingQueue<>();
	
	public RobotTaskQueueThread(int threadId) {
		this.threadId=threadId;
		setName("robot-task-queue-thread-"+threadId);
	}
	
	@Override
	public void run() {
		while(run) {
			try {
				Runnable poll = taskQueue.take();
				poll.run();//直接调用run方法，就不是启动一个线程了吧
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void addTask(Runnable runnable) {
		taskQueue.add(runnable);
	}
	
	
	public int getThreadId() {
		return threadId;
	}
	
}
