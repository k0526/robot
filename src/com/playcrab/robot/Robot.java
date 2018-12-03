package com.playcrab.robot;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.playcrab.config.RobotProperties;
import com.playcrab.core.controller.BaseController;
import com.playcrab.core.net.codefactory.IWebSocketDecoder;
import com.playcrab.core.net.codefactory.impl.RobotWebSocketDecoder;
import com.playcrab.core.robot.BaseRobot;
import com.playcrab.core.service.RobotCoreManager;
import com.playcrab.core.util.HashUtil;
import com.playcrab.robot.controller.ConnectInitController;
import com.playcrab.robot.controller.DevelopmentController;
import com.playcrab.robot.controller.FarmLandController;
import com.playcrab.robot.controller.HeroController;
import com.playcrab.robot.controller.SectionController;
import com.playcrab.robot.status.RobotStatusManager;

/**
 * 机器人类
 * 
 * @author xjp
 * @date 2018年10月15日 下午4:05:03
 */
public class Robot extends BaseRobot {

	public Robot(String id, String battleId, String opponent) throws Exception {
		this(new RobotWebSocketDecoder());

		this.id = id;
		this.battleId = battleId;
		this.opponent = opponent;
		this.lastMethodTime = System.currentTimeMillis();
		this.idleAliveTime = RobotProperties.getInstance().getIdleTime();

		// TODO 此处注册机器人能够处理的控制类
		registerController(new ConnectInitController(this, 2000));
		registerController(new SectionController(this, 2000));
		registerController(new DevelopmentController(this, 2000));
		registerController(new HeroController(this, 10000));
		registerController(new FarmLandController(this, 10000));
	}

	public Robot(IWebSocketDecoder iWebSocketDecoder) throws Exception {
		super(iWebSocketDecoder);
	}

	private boolean isConnection = false;

	private boolean isLogin = false;

	private JSONObject battleInfo;

	private String id; // accId 这个就是账号了，客户端，拿到userId也没有用。不可能把userId传给服务器

	private int userId;//玩家id

	private int castleLevel;//城池等级
	
	private String battleId;

	private int requestId = 0;

	private int team = 0;

	private int battleStep = 0;

	private String opponent = "";

	private String lastMethod = "";

	private long lastMethodTime = 0;

	private int idleAliveTime = 0;

	/**
	 * 设置websocket连接AESkey
	 * 
	 * @param aesKey
	 */
	public void setWebsocketAesKey(String aesKey) {
		RobotWebSocketDecoder decoder = (RobotWebSocketDecoder) iWebSocketDecoder;
	}

	@Override
	public void connectSuccess() {
		this.isConnection = true;
		System.out.println("connectSuccess 连接成功!");
	}

	public boolean isConnection() {
		return isConnection;
	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	@Override
	public String getId() {
		return id;
	}

	public String getBattleId() {
		return battleId;
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}

	public int getBattleStep() {
		return battleStep;
	}

	public void setBattleStep(int battleStep) {
		this.battleStep = battleStep;
	}

	public JSONObject getBattleInfo() {
		return battleInfo;
	}

	public void setBattleInfo(JSONObject battleInfo) {
		this.battleInfo = battleInfo;
	}

	public String getOpponent() {
		return opponent;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getCastleLevel() {
		return castleLevel;
	}

	public void setCastleLevel(int castleLevel) {
		this.castleLevel = castleLevel;
	}

	/*
	 * 处理返回来的结果
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void onMessage(Map<String, Object> data) {
		// JSONObject parse = (JSONObject) JSON.parse(data);
		super.onMessage(data);
		// if (!parse.containsKey("method")) {
		// asyTask(new Runnable() {
		// public void run() {
		// @Override
		// BaseController baseController =
		// controllerMap.get(ConnectInitController.class);
		// baseController.onMessage(data);
		// }
		// });
		// } else {
		String method = (String) data.get("cmd");
		if (this.lastMethod != null && method.equals(this.lastMethod)) {
			long now = System.currentTimeMillis();
			RobotStatusManager.getInstance().countMethodTime(method, now - lastMethodTime);
		}
		// }
		// TODO 消息分发 这里的消息要怎么分发呢？
	}

	@Override
	public void onError(Throwable e) {
		try {
			disconnect();
			e.printStackTrace();
		} catch (Exception e2) {
		}
	}

	@Override
	public void onClose() {
		RobotCoreManager.getInstance().removeRobot(this);
	}

	@Override
	public int getThreadId() {
		int threadCount = RobotProperties.getInstance().getThreadCount();
		int hash = HashUtil.hash(battleId);
		return hash % threadCount + 1;
	}

	@Override
	public void run() {
		super.run();
		if (System.currentTimeMillis() - lastMethodTime > this.idleAliveTime) {
			try {
				disconnect();
			} catch (Exception e) {
				RobotCoreManager.getInstance().removeRobot(this);
				e.printStackTrace();
			}
		}
	}

	@Override
	public void connect() throws Exception {
		setWebsocketAesKey(RobotProperties.getInstance().getDefaultAesKey());
		super.connect();
	}

	public void reset() {
		this.isConnection = false;
		this.isLogin = false;
		this.team = 0;
		controllerList.stream().forEach(controller -> {
			controller.init();
		});
	}

	public void sendMessage(String method, JSONObject jsonObject) {
		// Map<String, Object> dataMap = new HashMap<>();
		// dataMap.put("method", method);
		// dataMap.put("id", requestId++);
		// dataMap.put("uniqueId", UUIDService.getInstance().getId(1));
		// dataMap.put("params", jsonObject);
		// JSONObject jsonObject2 = new JSONObject(dataMap);
		// this.lastMethod = method; // 记录发送的命令
		// this.lastMethodTime = System.currentTimeMillis();
		// super.sendMessage(jsonObject2.toJSONString());
	}

	/**
	 * 发送消息
	 */
	public void sendMessageData(short cmd, Map data) {
		if (!this.isConnection() || !this.isLogin()) {// 没有连接或者没有登录成功，不能发送数据
			return;
		}
		String accId = this.getId();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", cmd);
		params.put("data", data);
		this.sendMessage(params);
	}

	@Override
	public void sendMessage(Map<String, Object> str) {
		super.sendMessage(str);
		this.lastMethodTime = System.currentTimeMillis();
	}

}
