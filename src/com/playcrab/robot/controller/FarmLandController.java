package com.playcrab.robot.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.playcrab.core.controller.BaseController;
import com.playcrab.core.robot.BaseRobot;
import com.playcrab.core.type.ConnectTypeEnum;
import com.playcrab.robot.Robot;

/**
 * 领地
 * 
 * @author xjp
 * @date 2018年10月17日 下午5:18:37
 */
public class FarmLandController extends BaseController<Robot> {

	private static final Logger logger = LoggerFactory.getLogger(FarmLandController.class);
	ConnectTypeEnum connectTypeEnum = ConnectTypeEnum.CONNECT_BY_WEBSOCKET;

	private int turnCD = 0;

	public FarmLandController(BaseRobot baseRobot, long intervalTime) {
		super(baseRobot, intervalTime);
	}

	@Override
	public boolean canUse() {
		Robot robot = getRobot();
		if (!robot.isLogin()) { // 所有的协议都是，不让登录就不能使用
			return false;
		}
		return true;
	}

	@Override
	public ConnectTypeEnum getConnectType() {
		return this.connectTypeEnum;
	}

	@Override
	public void init() {
		super.init();
		registerMethod("2401");// 转转盘
	};

	@Override
	public void onMessage(Map<String, Object> data) {
		String cmd = (String) data.get("cmd");
		if (cmd.equals("2401")) {
			Object object = data.get("errCode");
			if (object != null) {// 不等于null,说明体力不足了，需要暂停1个小时
				turnCD = 50000;
			}
		} else if (cmd.equals("800")) { // 800

		} else {

		}
	}

	/**
	 * 获取转盘视图
	 */
	public void getTuanPanView() {
		if (!getRobot().isConnection() || !getRobot().isLogin()) {// 没有连接或者没有登录成功，不能发送数据
			return;
		}
		getRobot().asyTask(new Runnable() {
			@Override
			public void run() {
				try {
					String accId = getRobot().getId();
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("cmd", (short) 2400);
					Map<String, Object> data = new HashMap<String, Object>();
					params.put("data", data);
					getRobot().sendMessage(params);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void tuanPan() {
		if (!getRobot().isConnection() || !getRobot().isLogin()) {// 没有连接或者没有登录成功，不能发送数据
			return;
		}
		getRobot().asyTask(new Runnable() {
			@Override
			public void run() {
				try {
					String accId = getRobot().getId();
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("cmd", (short) 2401);
					Map<String, Object> data = new HashMap<String, Object>();
					params.put("data", data);
					getRobot().sendMessage(params);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void run() {
		Robot robot = getRobot();
		if (turnCD <= 0) {
			this.getTuanPanView();//让服务器添加数据
			this.tuanPan();
		} else {
			System.out.println("turnCD =" + turnCD+" userId ="+robot.getId());
			turnCD -= 10000;
		}
	}

	@Override
	public void initData() {
		
	}

}
