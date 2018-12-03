package com.playcrab.robot;

import java.util.HashMap;
import java.util.Map;

import com.playcrab.core.robot.IRobotProduction;
import com.playcrab.core.service.RobotCoreManager;
import com.playcrab.config.RobotProperties;

/**
 * 生产机器人的类
 * @author xjp
 * @date 2018年10月16日 上午10:28:59 
 */
public class RobotFactory implements IRobotProduction {

	private int battleCount;
	
	private int battleIdBegin;
	
	private int battleIdEnd;
	
	private int battleId;
	
	public RobotFactory() {
		this.battleCount=RobotProperties.getInstance().getBattleCount();
		this.battleIdBegin=RobotProperties.getInstance().getBattleIdBegin();
		this.battleIdEnd=RobotProperties.getInstance().getBattleIdEnd();
		this.battleId=RobotProperties.getInstance().getBattleIdBegin();
	}
	
	@Override
	public void productionLine() {
		// 机器人生产线   生产机器人逻辑

		while (true) {
			int robotCount = RobotCoreManager.getInstance().getRobotCount();
			if (robotCount < this.battleCount) {
				try {
					String battleId = ""+this.battleId;
					String id1 = ""+this.battleId;
					Robot robot1 = new Robot(id1, battleId, id1);
					Map<String, Object> dataMap = new HashMap<>();
					dataMap.put("battleId", battleId);
					dataMap.put("rid1", robot1.getId());
					RobotCoreManager.getInstance().addRobot(robot1);
					this.battleId++;
					if(this.battleId==battleIdEnd) {
						this.battleId=battleIdBegin;
					}
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}