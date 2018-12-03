package com.playcrab.core.pool;

import java.beans.PropertyVetoException;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.playcrab.core.dao.RobotDaoData;
import com.playcrab.core.util.FileUtil;
import com.playcrab.core.util.Value;

/**
 * @Date: 2015年9月30日 下午3:48:30
 * @Author: zhuqd
 * @Description:
 */
public class DBPool {
	private static Logger logger = LoggerFactory.getLogger(DBPool.class);
	private static DBPool pool = new DBPool();
	private ComboPooledDataSource ds = null;

	private DBPool() {

	}

	/**
	 * get instance
	 * 
	 * @return
	 */
	public static DBPool instance() {
		return pool;
	}

	/**
	 * @throws PropertyVetoException
	 * 
	 */
	public void init(String configName) throws PropertyVetoException {
		String scanPath = FileUtil.getProjectPath();
		File file = FileUtil.findFile(scanPath, configName);
		if (!file.exists()) {
			throw new RuntimeException("config path is null , db pool can not init.");
		}
		DBConfig config = getConfig(file.getAbsolutePath());
		ds = new ComboPooledDataSource();
		ds.setDriverClass("com.mysql.jdbc.Driver");
		ds.setJdbcUrl(config.getJdbcUrl());
		ds.setUser(config.getUser());
		ds.setPassword(config.getPassword());
		ds.setInitialPoolSize(config.getInitialPoolSize());
		ds.setMaxPoolSize(config.getMaxPoolSize());
		ds.setMinPoolSize(config.getMinPoolSize());
		ds.setMaxIdleTime(config.getMaxIdleTime());
		ds.setAcquireIncrement(config.getAcquireIncrement());
		ds.setMaxStatementsPerConnection(config.getMaxStatementsPerConnection());
		ds.setNumHelperThreads(config.getNumHelperThreads());
		ds.setCheckoutTimeout(config.getCheckoutTimeout());
		ds.setIdleConnectionTestPeriod(config.getIdleConnectionTestPeriod());
		System.out.println("DBPool init done ...");
		
		initData();
	}
	
	/**
	 * 获取配置表里面的数据
	 */
	private void initData() {
		
		RobotDaoData.getInstance().getBuildingPosTemps();		
	}

	/**
	 * 读取配置
	 * 
	 * @param path
	 * @return
	 */
	private DBConfig getConfig(String path) {
		Map<String, String> map = FileUtil.readProperties(path);
		DBConfig config = new DBConfig();
		config.setJdbcUrl(map.get("jdbcUrl"));
		config.setUser(map.get("user"));
		config.setPassword(map.get("password"));
		config.setInitialPoolSize(Value.parserInt(map.get("initialPoolSize")));
		config.setMaxPoolSize(Value.parserInt(map.get("maxPoolSize")));
		config.setMinPoolSize(Value.parserInt(map.get("minPoolSize")));
		config.setMaxIdleTime(Value.parserInt(map.get("maxIdleTime")));
		config.setAcquireIncrement(Value.parserInt(map.get("acquireIncrement")));
		config.setMaxStatementsPerConnection(Value.parserInt(map.get("maxStatementsPerConnection")));
		config.setNumHelperThreads(Value.parserInt(map.get("numHelperThreads")));
		config.setCheckoutTimeout(Value.parserInt(map.get("checkoutTimeout")));
		config.setIdleConnectionTestPeriod(Value.parserInt(map.get("idleConnectionTestPeriod")));
		return config;
	}
	
	/**
	 * 获取连接
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() {
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			logger.error("dbpool error.{}", e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 关闭
	 */
	public void close() {
		ds.close();
	}

	/**
	 * 关闭连接
	 * 
	 * @param conn
	 */
	public void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("close connenction error ", e);
			}
		}
	}

}
