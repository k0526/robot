package com.playcrab.robot.status;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class RobotStatusManager {
	
	private static RobotStatusManager instance=new RobotStatusManager();
	
	private HashMap<String, AtomicLong> methodTimeMap=new HashMap<>();
	
	private HashMap<String, AtomicLong> methodCountMap=new HashMap<>();
	
	private RobotStatusManager() {
		
	}
	
	public static RobotStatusManager getInstance() {
		return instance;
	}
	
	public void countMethodTime(String method,long time) {
		if(!methodTimeMap.containsKey(method)) {
			methodTimeMap.put(method, new AtomicLong(0l));
		}
		if(!methodCountMap.containsKey(method)) {
			methodCountMap.put(method, new AtomicLong(0l));
		}
		
		methodTimeMap.get(method).addAndGet(time);
		methodCountMap.get(method).incrementAndGet();
	}
	
	public HashMap<String, Long> getMethodTime(){
		HashMap<String, Long> dataMap=new HashMap<>();
		Set<String> keySet = methodTimeMap.keySet();
		for (String method : keySet) {
			long time=methodTimeMap.get(method).getAndSet(0l);
			if(!methodCountMap.containsKey(method)) {
				continue;
			}
			long count=methodCountMap.get(method).getAndSet(0l);
			long avgTime=count==0?0:time/count;
			dataMap.put(method, avgTime);
		}
		return dataMap;
	}
	
}
