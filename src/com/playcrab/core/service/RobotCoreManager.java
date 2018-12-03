package com.playcrab.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.playcrab.core.robot.BaseRobot;
import com.playcrab.core.robot.IRobotProduction;
import com.playcrab.core.thread.RobotLoopThread;
import com.playcrab.core.thread.RobotTaskQueueThread;
import com.playcrab.robot.timer.tasks.LogMethodStatusAction;

/**
 * 机器人核心管理器
 */
public class RobotCoreManager {
	
	private boolean isInit=false;
	
	/** 机器人生产线*/
	private IRobotProduction iRobotProduction;
	/** 机器人生产线线程*/
	private Thread productionThread;
	
	private int threadCount=8;
	
	private HashMap<Integer, RobotLoopThread> robotLoopThreadMap=new HashMap<>();
	
	private HashMap<Integer, RobotTaskQueueThread> robotTaskThreadMap=new HashMap<>();
	
	private ConcurrentHashMap<Integer,Map<String,BaseRobot>> robotMap=new ConcurrentHashMap<>(); 
	
	
	private static RobotCoreManager instance=new RobotCoreManager();
	
	private RobotCoreManager() {
		
	}
	
	public static void initInstance(IRobotProduction iRobotProduction,int threadCount) {
		instance.initRobotCoreManager(iRobotProduction, threadCount);
	}
	
	public static RobotCoreManager getInstance() {
		// 没有想好机器人核心管理器在没有初始化时被使用的处理办法  直接关掉进程
		if(!instance.isInit) {
			System.exit(0);
		}
		return instance;
	}
	
	/**
	 * 机器人核心管理器
	 * @param iRobotProduction 机器人生产线
	 */
	public void initRobotCoreManager(IRobotProduction iRobotProduction,int threadCount) {
		this.iRobotProduction=iRobotProduction;
		this.threadCount=threadCount;
		this.isInit=true;
	}
	
	/**
	 * 开启机器人检测
	 */
	public void start() {
		
		for(int i=1;i<=threadCount;i++) {
			
			RobotLoopThread robotLoopThread=new RobotLoopThread(i);
			robotLoopThreadMap.put(i, robotLoopThread);
			robotLoopThread.start();
			
			RobotTaskQueueThread robotTaskQueueThread=new RobotTaskQueueThread(i);
			robotTaskThreadMap.put(i, robotTaskQueueThread);
			robotTaskQueueThread.start();
		}
		
		// 生产线线程 初始化的最后执行
		productionThread=new Thread(new Runnable() {
			@Override
			public void run() {
				iRobotProduction.productionLine();
			}
		});
		productionThread.setName("production_line_thread");
		productionThread.start();
	}
	
	public void addTask(int threadId,Runnable runnable) {
		if(!robotTaskThreadMap.containsKey(threadId)) {
			RobotTaskQueueThread robotTaskQueueThread=new RobotTaskQueueThread(threadId);
			robotTaskQueueThread.start();
			robotTaskThreadMap.put(threadId, robotTaskQueueThread);
		}
		robotTaskThreadMap.get(threadId).addTask(runnable);
	}
	
	public ArrayList<BaseRobot> getRobotByThreadId(int threadId){
		ArrayList<BaseRobot> temp=new ArrayList<>();
		if(!robotMap.containsKey(threadId)) {
			return temp;
		}
		Map<String, BaseRobot> map = robotMap.get(threadId);
		synchronized (map) {
			temp.addAll(map.values());
		}
		return temp;
	}

	public void addRobot(BaseRobot robot) {
		int threadId = robot.getThreadId();
		if(!robotMap.containsKey(threadId)) {
			robotMap.put(threadId, new HashMap<>());
		}
		Map<String, BaseRobot> map = robotMap.get(threadId);
		synchronized (map) {
			map.put(robot.getId(), robot);
		}
	}
	
	public void removeRobot(BaseRobot robot) {
		int threadId = robot.getThreadId();
		if(!robotMap.containsKey(threadId)) {
			return ;
		}
		Map<String, BaseRobot> map = robotMap.get(threadId);
		synchronized (map) {
			map.remove(robot.getId());
			LogMethodStatusAction.printLog("移除机器人 userId = "+robot.getId());
		}
	}
	
	public int getRobotCount() {
		int count=0;
		Collection<Map<String,BaseRobot>> values = robotMap.values();
		for (Map<String, BaseRobot> map : values) {
			synchronized (map) {
				count+=map.size();
			}
		}
		return count;
	}
	
	
}
