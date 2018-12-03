package com.playcrab.core.util;

import java.util.HashMap;
import java.util.Map;

public class CountUtil {
	
	private Map<String, Integer> countMap=new HashMap<>();
	
	public boolean count(String key,int count) {
		if(count<=0) {
			return false;
		}
		if(!countMap.containsKey(key)) {
			return true;
		}
		if(countMap.get(key)<count) {
			return true;
		} 
		return false;
	}
	
	public void addCount(String key) {
		if(!countMap.containsKey(key)) {
			countMap.put(key, 1);
			return ;
		}
		countMap.put(key, countMap.get(key)+1);
	}
	
	
	
	
	
	
}
