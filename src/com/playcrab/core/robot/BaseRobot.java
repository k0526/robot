package com.playcrab.core.robot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.playcrab.core.controller.BaseController;
import com.playcrab.core.net.codefactory.IWebSocketDecoder;
import com.playcrab.core.net.websocket.NWSClient;
import com.playcrab.core.service.RobotCoreManager;
import com.playcrab.core.type.ConnectTypeEnum;

/**
 * 机器人基本类
 *
 */
public abstract class BaseRobot {
	
	/** 控制模块Map集合*/
	@SuppressWarnings("rawtypes")
	protected Map<Class<? extends BaseController>, BaseController> controllerMap=new HashMap<>();
	/** 机器人含有的 控制模块List集合*/
	@SuppressWarnings("rawtypes")
	protected List<BaseController> controllerList=new ArrayList<>();
	/** websocket连接客户端*/
	private NWSClient wsClient;
	/** 当前机器人的连接类型 */
	private ConnectTypeEnum connectTypeEnum;
	/** 解码器*/
	protected IWebSocketDecoder iWebSocketDecoder;
	
	/**
	 * 构造函数 必须实例化消息解码器
	 * @param iWebSocketDecoder
	 * @throws Exception 
	 */
	public BaseRobot(IWebSocketDecoder iWebSocketDecoder) throws Exception {
		
		this.iWebSocketDecoder=iWebSocketDecoder;
		connectTypeEnum=ConnectTypeEnum.CONNECT_BY_WEBSOCKET;
	}
	
	/**
	 * 注册控制模块
	 * @param controller
	 */
	@SuppressWarnings("rawtypes")
	protected void registerController(BaseController controller) {
		if(controllerMap.containsKey(controller.getClass())) {
			return ;
		}
		controller.init();
		controllerMap.put(controller.getClass(), controller);
		controllerList.add(controller);
	}
	
	/**
	 * 轮询触发事件
	 */
	public void run() {
//		controllerList.stream().filter(controller->{
//			return controller.canUse()&&controller.checkRun();
//		}).forEach( controller->{
//			asyTask(new Runnable() {
//				@Override
//				public void run() {
//					controller.run(); 
//				}
//			});
//		});		
		for (BaseController baseController : controllerList) {
			if (baseController.canUse()&&baseController.checkRun()) {			
				this.asyTask(new Runnable() {					
					@Override
					public void run() {
						baseController.run();
					}
				});	
			}
		}		
	}
	
	public void connect() throws Exception {
		wsClient=new NWSClient(iWebSocketDecoder, this);
		wsClient.connect();
	}
	
	/**
	 * 获取机器人连接类型 
	 * @return
	 */
	public ConnectTypeEnum getConnectTypeEnum() {
		return connectTypeEnum;
	}
	
	/**
	 * 发送信息
	 * @param str
	 */
	public void sendMessage(Map<String, Object> str) {
		switch (connectTypeEnum) {
			case CONNECT_BY_WEBSOCKET:
					try {
						wsClient.send(str);
					} catch (Exception e) {
						e.printStackTrace();
					}	
				break;
			default:
				break;
			}
	}
	
	/**
	 * 关闭连接
	 * @throws IOException 
	 */
	public void disconnect() throws Exception {
		switch (connectTypeEnum) {
			case CONNECT_BY_WEBSOCKET:
				wsClient.close();	
				break;
			default:
				break;
			}
	}
	
	/**
	 * 连接成功回调信息
	 */
	public abstract void connectSuccess();
	
	/**
	 * 消息回调
	 * @param arg0
	 */
	public void onMessage(Map<String, Object> data) {
//		JSONObject jsonObject=JSON.parseObject(arg0);
//		if(!jsonObject.containsKey("method")) {
//			return ;
//		}
//		String string = jsonObject.getString("method");
		try {
			String cmd = (String)data.get("cmd");
			
			for (BaseController baseController : controllerList) {
				if (baseController.canUse()&&baseController.hasMethod(cmd)) {
					baseController.onMessage(data);
				}
			}
//			controllerList.stream().filter(controller->{
//				return controller.canUse()&&controller.hasMethod(cmd);
//			}).forEach(controller->{
//				controller.onMessage(data);
//			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 异步任务 要经常使用
	 * @param runnable
	 */
	public void asyTask(Runnable runnable) {
		int threadId = getThreadId();
		RobotCoreManager.getInstance().addTask(threadId, runnable);
	}
	
	/**
	 * 错误回调
	 * @param e
	 */
	public abstract void onError(Throwable e);
	
	/**
	 * 连接断开回调
	 */
	public abstract void onClose();
	
	public abstract int getThreadId();
	
	public abstract String getId();
	
}
