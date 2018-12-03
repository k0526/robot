package com.playcrab.robot.controller;

import java.util.HashMap;
import java.util.Map;

import com.playcrab.config.RobotProperties;
import com.playcrab.core.controller.BaseController;
import com.playcrab.core.robot.BaseRobot;
import com.playcrab.core.type.ConnectTypeEnum;
import com.playcrab.robot.ProtocalMessage;
import com.playcrab.robot.Robot;
import com.playcrab.robot.timer.tasks.LogMethodStatusAction;

public class ConnectInitController extends BaseController<Robot> {

	private int buildCD = 120;

	public ConnectInitController(BaseRobot baseRobot, long intervalTime) {
		super(baseRobot, intervalTime);
	}

	@Override
	public boolean canUse() {
		Robot robot = getRobot();
		// if (robot.isLogin()) {
		// return false;
		// }
		return true;
	}

	@Override
	public ConnectTypeEnum getConnectType() {
		return ConnectTypeEnum.CONNECT_BY_WEBSOCKET;
	}

	@Override
	public void init() {
		super.init();

		registerMethod("100");// 登录
		registerMethod("101");// 创建角色
		registerMethod("102");// 初始化玩家角色
		registerMethod(ProtocalMessage.getUserInfo + "");// 获取玩家城池等级

		getRobot().setWebsocketAesKey(RobotProperties.getInstance().getDefaultAesKey());
	}

	@Override
	public void onMessage(Map<String, Object> data) {
		String cmd = (String) data.get("cmd");
		if (cmd.equals("100")) {// 登录的协议回来了
			int userId = (int) data.get("userId");
			if (userId > 0) { // 登录成功
				getRobot().setLogin(true);
				getRobot().setUserId(userId);
				this.getSynData();
			} else if (userId == 0) { // 没有这个账号，需要注册
				this.register();
			}
		} else if (cmd.equals("101")) {// 注册的消息回来了 发送102协议，请求玩家数据
			int userId = (int) data.get("userId");
			getRobot().setLogin(true);
			getRobot().setUserId(userId);
			LogMethodStatusAction.printLog("注册成功 userId=" + userId);
			this.getSynData();
		} else if (cmd.equals("102")) {
			this.getUserData();
			LogMethodStatusAction.printLog("登录成功  机器人是" + getRobot().getId());
		} else if (cmd.equals(ProtocalMessage.getUserInfo + "")) {//
			
			int level = (int) data.get("casLevel");
			getRobot().setCastleLevel(level);// 保存城池等级
		}
		countUtil.addCount("connector.init");
	}

	@Override
	public void run() {
		connect();
		login();
		if (buildCD <= 0) {
			this.getBuildData();
			buildCD = 120;
		} else {
			buildCD--;
		}
	}

	/**
	 * 连接
	 */
	private void connect() {
		boolean connection = getRobot().isConnection();
		if (connection) {
			return;
		}
		getRobot().asyTask(new Runnable() {
			@Override
			public void run() {
				try {
					getRobot().connect();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 注册
	 */
	private void register() {
		boolean connection = getRobot().isConnection();
		if (!connection) {// 没有连接，不能发送数据
			return;
		}
		if (getRobot().isLogin()) {// 已经登录，不用再注册了
			return;
		}
		getRobot().asyTask(new Runnable() {
			@Override
			public void run() {
				try {
					String accId = getRobot().getId();
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("cmd", (short) 101);
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("headIcon", "hero26_jpg");// 形象
					data.put("userName", accId);// 角色id
					data.put("countryId", 11700002);// 国家
					params.put("data", data);
					getRobot().sendMessage(params);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 获取玩家的数据，给服务器初始化数据
	 */
	private void getSynData() {
		boolean connection = getRobot().isConnection();
		if (!connection) {// 没有连接，不能发送数据
			return;
		}
		if (!getRobot().isLogin()) {// 没有登录也不能发数据
			return;
		}
		getRobot().asyTask(new Runnable() {
			@Override
			public void run() {
				try {
					String accId = getRobot().getId();
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("cmd", ProtocalMessage.getSynData);
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("userName", accId);// 角色id 其实，不需要参数
					params.put("data", data);
					getRobot().sendMessage(params);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 登录成功之后，有些数据需要前端自己去获取 获取玩家的数据
	 */
	public void getUserData() {
		this.getBuildData();
		this.getTuanPanView();
		this.getUserInfo();
	}

	/**
	 * 获取玩家信息，主要是城池等级
	 */
	private void getUserInfo() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("targetUserId", getRobot().getUserId());
		getRobot().sendMessageData(ProtocalMessage.getUserInfo, data);
	}

	private void getTuanPanView() {
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

	/**
	 * 获取建筑队列的数据
	 */
	private void getBuildData() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", (short) 800);
		Map<String, Object> data = new HashMap<String, Object>();
		params.put("data", data);
		getRobot().sendMessage(params);
	}

	/**
	 * 登录
	 */
	private void login() {
		if (!getRobot().isConnection()) {
			return;
		}
		if (getRobot().isLogin()) {
			return;
		}
		getRobot().asyTask(new Runnable() {
			@Override
			public void run() {
				try {
					if (!countUtil.count("connector.init", 1)) {
						return;
					}
					String accId = getRobot().getId();
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("cmd", (short) 100);
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("openid", accId);
					data.put("platFormType", 1);
					params.put("data", data);
					getRobot().sendMessage(params);

					countUtil.addCount("connector.init");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 发送插入战斗信息
	 */
	private void sendBattleInfo() {
		if (getRobot().isLogin()) {
			return;
		}
		if (getRobot().getBattleInfo() == null) {
			return;
		}
		getRobot().sendMessage("debug.command2", getRobot().getBattleInfo());
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

}
