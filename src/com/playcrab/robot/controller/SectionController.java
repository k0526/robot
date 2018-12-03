package com.playcrab.robot.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.playcrab.core.controller.BaseController;
import com.playcrab.core.robot.BaseRobot;
import com.playcrab.core.type.ConnectTypeEnum;
import com.playcrab.robot.Robot;

/**
 * @author xjp 创建时间：2018年9月30日 下午5:00:45 类说明如下: 副本相关的控制器
 */
public class SectionController extends BaseController<Robot> {

	private static final Logger logger = LoggerFactory.getLogger(SectionController.class);
	
	ConnectTypeEnum connectTypeEnum = ConnectTypeEnum.CONNECT_BY_WEBSOCKET;

	public SectionController(BaseRobot baseRobot, long intervalTime) {
		super(baseRobot, intervalTime);
	}

	@Override
	public void initData() {
		
	}
	
	@Override
	public boolean canUse() {
		Robot robot = (Robot) getRobot();
		if (!robot.isLogin()) {
			return false;
		}
		return true;
	}

	@Override
	public ConnectTypeEnum getConnectType() {
		return this.connectTypeEnum;
	}

	@Override
	public void onMessage(Map<String, Object> data) {

	}

	@Override
	public void run() {

	}

}
