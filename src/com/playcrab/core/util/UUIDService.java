package com.playcrab.core.util;

import java.util.UUID;

public class UUIDService {
	private static UUIDService instance=new UUIDService();
	public static UUIDService getInstance(){
		return instance;
	}	
	private Short id = 0;
	public long getId(int serverId) {
		synchronized (UUID.class) {
			if(id>=Short.MAX_VALUE){
				id=0;
			}else{
				id++;
			}
			return (serverId & 0xFFFF) << 48 | (System.currentTimeMillis()/1000 & 0xFFFFFFFF) << 16 | id & 0xFFFF;
		}
	}
}