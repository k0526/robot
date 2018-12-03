package com.playcrab.core.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.playcrab.core.domain.BuildingPosTemplate;
import com.playcrab.core.pool.DBPool;

/**
 * 
 * @author xjp
 * @date 2018年11月12日 下午4:53:09
 */
public class RobotDaoData {

	private static Logger logger = LoggerFactory.getLogger(RobotDaoData.class);

	private static List<BuildingPosTemplate> list = null;

	private RobotDaoData() {
	}

	private static RobotDaoData robotDaoImpl = new RobotDaoData();

	public static RobotDaoData getInstance() {
		return robotDaoImpl;
	}

	/**
	 * 获取模板数据
	 * 
	 * @return
	 */
	public List<BuildingPosTemplate> getBuildingPosTemps() {
		if (list == null) {
			logger.debug("查询建筑队列的数据");
			list = this.getBuildingPosTemplate();
		}
		return list;
	}

	/**
	 * 获取建筑的配置表
	 */
	private List<BuildingPosTemplate> getBuildingPosTemplate() {
		String sql = "SELECT * FROM BuildingPosTemplate";
		Connection conn = DBPool.instance().getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		BuildingPosTemplate buildingPosTemplate = null;
		LinkedList<BuildingPosTemplate> list = new LinkedList<>();
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				buildingPosTemplate = new BuildingPosTemplate();
				buildingPosTemplate.setPos(rs.getInt("pos"));
				buildingPosTemplate.setBuildEntId(rs.getInt("buildEntId"));
				buildingPosTemplate.setBuildingTypId(rs.getInt("buildingTypId"));
				buildingPosTemplate.setCastleType(rs.getInt("castleType"));
				buildingPosTemplate.setOpenLevel(rs.getInt("openLevel"));
				list.add(buildingPosTemplate);
			}
		} catch (SQLException e) {
			logger.error("sql exception. {},{}", sql, e.getMessage());
		} finally {
			DBPool.instance().closeConnection(conn);
		}
		return list;
	}

}
