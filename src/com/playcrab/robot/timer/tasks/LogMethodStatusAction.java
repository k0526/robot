package com.playcrab.robot.timer.tasks;

import java.util.HashMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.playcrab.core.service.RobotCoreManager;
import com.playcrab.core.timer.FixTimeAction;
import com.playcrab.robot.status.RobotStatusManager;

public class LogMethodStatusAction implements FixTimeAction {

	private static final Logger logger = LoggerFactory.getLogger("robotStatus");

	@Override
	public void action() {
		HashMap<String, Long> methodTime = RobotStatusManager.getInstance().getMethodTime();
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("method use time:\t");
		for (Entry<String, Long> entry : methodTime.entrySet()) {
			stringBuffer.append(entry.getKey() + ":" + entry.getValue() + "ms\t");
		}
		int robotCount = RobotCoreManager.getInstance().getRobotCount();
		stringBuffer.append("robotCount:" + robotCount + "\t");
		logger.debug(stringBuffer.toString());
	}

	public static void printLog(String message) {
		logger.debug(message);
	}

}
