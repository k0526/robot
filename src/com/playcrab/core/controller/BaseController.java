package com.playcrab.core.controller;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.playcrab.core.robot.BaseRobot;
import com.playcrab.core.type.ConnectTypeEnum;
import com.playcrab.core.util.CountUtil;

/**
 * 机器人控制模块基本类
 * 
 * @param <T>
 */
public abstract class BaseController<T extends BaseRobot> {
	/** 机器人 */
	private BaseRobot baseRobot;
	/** method处理集合 */
	private Set<String> methodSet = new HashSet<>();

	protected CountUtil countUtil = new CountUtil();

	private long lastRunTime = 0;

	private long intervalTime = 0;

	/**
	 * 构造函数
	 * 
	 * @param baseRobot
	 */
	public BaseController(BaseRobot baseRobot, long intervalTime) {
		this.baseRobot = baseRobot;
		this.intervalTime = intervalTime;
	}

	/**
	 * 查看method是否被当前controller处理
	 * 
	 * @param method
	 * @return
	 */
	public boolean hasMethod(String method) {
		return methodSet.contains(method);
	}

	/**
	 * 注册method到当前controller中
	 * 
	 * @param method
	 */
	protected void registerMethod(String method) {
		methodSet.add(method);
	}

	/**
	 * 当前controller是否可用
	 * 
	 * @return
	 */
	public abstract boolean canUse();

	/**
	 * 获取当前controller通信类型
	 * 
	 * @return
	 */
	public abstract ConnectTypeEnum getConnectType();

	/**
	 * 初始化controller
	 */
	public void init() {
		countUtil = new CountUtil();
	}

	/**
	 * 初始化数据
	 */
	public abstract void initData() ;

	/**
	 * 消息处理接口 接收回来的消息
	 * 
	 * @param jsonObject
	 */
	public abstract void onMessage(Map<String, Object> data);

	/**
	 * 定时接口 这个就是发送协议的接口
	 */
	public abstract void run();

	public boolean checkRun() {
		long now = System.currentTimeMillis();
		if (now - lastRunTime < intervalTime) {
			return false;
		}
		lastRunTime = now;
		return true;
	}

	/**
	 * 在父类获取子类的方法
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected T getRobot() {
		return (T) baseRobot;
	}

}
