package com.playcrab.config;

import java.io.FileInputStream;
import java.util.Properties;

public class RobotProperties {
	
	private static RobotProperties instance=new RobotProperties();
	
	private RobotProperties() {
		
	}
	
	public static RobotProperties getInstance() {
		return instance;
	}
	
	private String webSocketUrl;
	
	private int webSocketPort;
	
	private String httpUrl;
	
	private int httpPort;
	
	private String defaultAesKey;
	
	private String connectAesKey;
	
	private int threadCount;
	
	private int battleIdBegin;
	
	private int battleIdEnd;
	
	private int battleCount;
	
	private int idleTime;
	
	
	public void init() {
		try {
			String userDir = System.getProperty("user.dir");
			Properties properties=new Properties();
			System.out.println(userDir+"/config/robot.properties");
			properties.load(new FileInputStream(userDir+"/config/robot.properties"));
			this.webSocketUrl = properties.getProperty("robot.websocket.url");
			this.webSocketPort=Integer.parseInt(properties.getProperty("robot.websocket.port").trim());
			this.httpUrl=properties.getProperty("robot.http.url");
			this.httpPort=Integer.parseInt(properties.getProperty("robot.http.port").trim());
			this.defaultAesKey=properties.getProperty("robot.defaultAesKey");
			this.connectAesKey=properties.getProperty("robot.connectAesKey");
			this.threadCount=Integer.parseInt(properties.getProperty("robot.threadCount"));
			this.battleIdBegin=Integer.parseInt(properties.getProperty("robot.battleIdBegin"));
			this.battleIdEnd=Integer.parseInt(properties.getProperty("robot.battleIdEnd"));
			this.battleCount=Integer.parseInt(properties.getProperty("robot.battleCount"));
			this.idleTime=Integer.parseInt(properties.getProperty("robot.idleTime"));
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public int getWebSocketPort() {
		return webSocketPort;
	}
	
	@Deprecated
	public void setWebSocketPort(int webSocketPort) {
		this.webSocketPort = webSocketPort;
	}
	
	public String getWebSocketUrl() {
		return webSocketUrl;
	}
	
	@Deprecated
	public void setWebSocketUrl(String webSocketUrl) {
		this.webSocketUrl = webSocketUrl;
	}
	
	public int getHttpPort() {
		return httpPort;
	}
	
	@Deprecated
	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}
	
	public String getHttpUrl() {
		return httpUrl;
	}
	
	@Deprecated
	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}
	
	public String getDefaultAesKey() {
		return defaultAesKey;
	}
	
	@Deprecated
	public void setDefaultAesKey(String defaultAesKey) {
		this.defaultAesKey = defaultAesKey;
	}
	
	public String getConnectAesKey() {
		return connectAesKey;
	}
	
	@Deprecated
	public void setConnectAesKey(String connectAesKey) {
		this.connectAesKey = connectAesKey;
	}
	
	public int getThreadCount() {
		return threadCount;
	}
	
	@Deprecated
	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}
	
	public int getBattleCount() {
		return battleCount;
	}
	
	@Deprecated
	public void setBattleCount(int battleCount) {
		this.battleCount = battleCount;
	}
	
	public int getBattleIdBegin() {
		return battleIdBegin;
	}
	
	@Deprecated
	public void setBattleIdBegin(int battleIdBegin) {
		this.battleIdBegin = battleIdBegin;
	}
	
	public int getBattleIdEnd() {
		return battleIdEnd;
	}
	
	@Deprecated
	public void setBattleIdEnd(int battleIdEnd) {
		this.battleIdEnd = battleIdEnd;
	}

	public int getIdleTime() {
		return idleTime;
	}

	@Deprecated
	public void setIdleTime(int idleTime) {
		this.idleTime = idleTime;
	}
	
}
