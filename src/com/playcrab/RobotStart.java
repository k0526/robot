package com.playcrab;

import java.beans.PropertyVetoException;

import com.playcrab.config.RobotProperties;
import com.playcrab.core.pool.DBPool;
import com.playcrab.core.service.RobotCoreManager;
import com.playcrab.robot.RobotFactory;
import com.playcrab.robot.timer.FixTimeTaskService;

public class RobotStart {

	public static void main(String[] args) {
		init();
		start();
	}

	public static void init() {
		RobotProperties.getInstance().init();
	}

	public static void start() {
		try {
			DBPool.instance().init("db_config.properties");
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		RobotCoreManager.initInstance(new RobotFactory(), RobotProperties.getInstance().getThreadCount());
		RobotCoreManager.getInstance().start();
		FixTimeTaskService.getInstance();
	}

	public static void stop() {

	}

}
