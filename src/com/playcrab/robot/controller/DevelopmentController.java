package com.playcrab.robot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.playcrab.core.controller.BaseController;
import com.playcrab.core.dao.RobotDaoData;
import com.playcrab.core.domain.BuildingPosTemplate;
import com.playcrab.core.robot.BaseRobot;
import com.playcrab.core.type.ConnectTypeEnum;
import com.playcrab.robot.ProtocalMessage;
import com.playcrab.robot.Robot;
import com.playcrab.robot.timer.tasks.LogMethodStatusAction;

/**
 * @author xjp 创建时间：2018年9月30日 下午5:00:45 类说明如下: 内政相关的controller
 */
public class DevelopmentController extends BaseController<Robot> {

	private static final Logger logger = LoggerFactory.getLogger(DevelopmentController.class);
	ConnectTypeEnum connectTypeEnum = ConnectTypeEnum.CONNECT_BY_WEBSOCKET;

	public DevelopmentController(BaseRobot baseRobot, long intervalTime) {
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
		registerMethod("800");// 获取所有的建筑队列的数据
		registerMethod("801");// 升级建筑
		registerMethod("803");// 建造建筑
		registerMethod("805");// 立即完成建筑升级
		registerMethod("819");// 流亡重建
	};

	@Override
	public void onMessage(Map<String, Object> data) {
		String cmd = (String) data.get("cmd");
		
		if (cmd.equals("801")) {

		} else if (cmd.equals("803")) {

		} else if (cmd.equals("805")) {

		} else if (cmd.equals("819")) {

		} else if (cmd.equals("800")) { // 800

			Map map = (Map) data.get("list");
			autoCreateAllBuilding(map);
			autoUplevelAllBuilding(map);
			updateCastle();//升级城池
			LogMethodStatusAction.printLog(cmd);
		} else if (cmd.equals(ProtocalMessage.upCastleLevel + "")) {
			
		}
	}

	/**
	 * 升级城池
	 */
	private void updateCastle() {
		Map<String, Object> data = new HashMap<>();
		getRobot().sendMessageData(ProtocalMessage.upCastleLevel, data);
	}

	/**
	 * 自动升级所有的建筑
	 * 
	 * @param map
	 */
	private void autoUplevelAllBuilding(Map map) {
		if (map == null) {
			return;
		}
		int level = (int) ((Map) map.get("1")).get("buildLevel");// 官府等级
		Set keySet = map.keySet();
		for (Object key : keySet) {
			Map item = (Map) map.get(key);
			if ((int) item.get("pos") == 1) {// 给官府升级
				upBuildLevel(1);
			} else if ((int) item.get("buildLevel") < level) {
				upBuildLevel((int) item.get("pos"));
			}
		}
	}

	/**
	 * 自动创建所有的建筑
	 */
	private void autoCreateAllBuilding(Map map) {
		if (map == null) {
			return;
		}
		int level = getRobot().getCastleLevel();
		List<BuildingPosTemplate> buildingPosTemps = RobotDaoData.getInstance().getBuildingPosTemps();
		for (BuildingPosTemplate buildItem : buildingPosTemps) {
			if (buildItem.getOpenLevel() <= level && map.get(buildItem.getPos() + "") == null) {
				createBuilding(buildItem.getPos(), buildItem.getBuildingTypId());
			}
		}
	}

	/**
	 * 创建建筑
	 */
	public void createBuilding(int pos, int buildEntId) {
		Map<String, Object> data = new HashMap<>();
		data.put("buildEndId", buildEntId);
		data.put("pos", pos);
		getRobot().sendMessageData((short) 803, data);
	}

	/**
	 * 给建筑提升等级
	 * 
	 * @param buildEntId
	 */
	public void upBuildLevel(int pos) {
		if (!getRobot().isConnection() || !getRobot().isLogin()) {// 没有连接或者没有登录成功，不能发送数据
			return;
		}
		getRobot().asyTask(new Runnable() {
			@Override
			public void run() {
				try {
					String accId = getRobot().getId();
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("cmd", (short) 801);
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("pos", pos);// 国家
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

	}

	@Override
	public void initData() {

	}

}
