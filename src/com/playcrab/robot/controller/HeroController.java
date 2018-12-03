package com.playcrab.robot.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.playcrab.core.controller.BaseController;
import com.playcrab.core.robot.BaseRobot;
import com.playcrab.core.type.ConnectTypeEnum;
import com.playcrab.robot.Robot;
import com.playcrab.robot.timer.tasks.LogMethodStatusAction;

/**
 * @author xjp 创建时间：2018年9月30日 下午5:00:45 类说明如下: 武将相关的controller
 */
public class HeroController extends BaseController<Robot> {

	private static final Logger logger = LoggerFactory.getLogger(HeroController.class);
	ConnectTypeEnum connectTypeEnum = ConnectTypeEnum.CONNECT_BY_WEBSOCKET;

	private int obtainHeroCD = 0;

	public HeroController(BaseRobot baseRobot, long intervalTime) {
		super(baseRobot, intervalTime);
	}

	@Override
	public boolean canUse() {
		Robot robot = (Robot) getRobot();
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
		registerMethod("302");
		registerMethod("325");// 抽武将
	};

	@Override
	public void onMessage(Map<String, Object> data) {
		String cmd = (String) data.get("cmd");
		if (cmd.equals("325")) {

		} else if (cmd.equals("302")) {

			Object object = data.get("errCode");
			if (object != null) {// 不等于null,说明体力不足了，需要暂停1个小时
				obtainHeroCD = 100000;
			}

		} else if (cmd.equals("805")) {

		} else if (cmd.equals("819")) {

		} else if (cmd.equals("800")) { // 800

		} else {

		}
	}

	/**
	 * 招募武将
	 * 
	 * @param num
	 */
	public void recuitHero(int num) {
		if (!getRobot().isConnection() || !getRobot().isLogin()) {// 没有连接或者没有登录成功，不能发送数据
			return;
		}
		getRobot().asyTask(new Runnable() {
			@Override
			public void run() {
				try {
					String accId = getRobot().getId();
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("cmd", (short) 302);
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("num", num);// 国家
					params.put("data", data);
					getRobot().sendMessage(params);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 酒馆抽奖
	 * 
	 * @param num
	 */
	public void extractAward(int num) {
		if (!getRobot().isConnection() || !getRobot().isLogin()) {// 没有连接或者没有登录成功，不能发送数据
			return;
		}
		getRobot().asyTask(new Runnable() {
			@Override
			public void run() {
				try {
					String accId = getRobot().getId();
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("cmd", (short) 325);
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("num", num);// 国家
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
		
		if (obtainHeroCD <= 0) {
			
			this.recuitHero(1);// 抽一个武将
		}else {
			obtainHeroCD -=10000;
		}
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}

}
