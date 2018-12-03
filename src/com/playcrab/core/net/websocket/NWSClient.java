package com.playcrab.core.net.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.ws.WebSocket;
import org.asynchttpclient.ws.WebSocketByteListener;
import org.asynchttpclient.ws.WebSocketUpgradeHandler;

import com.playcrab.core.net.codefactory.IWebSocketDecoder;
import com.playcrab.core.robot.BaseRobot;
import com.playcrab.config.RobotProperties;

public class NWSClient {

	public static AsyncHttpClientConfig cf = new DefaultAsyncHttpClientConfig.Builder().setRequestTimeout(300000)
			.setWebSocketMaxFrameSize(2621440).build();
	public static AsyncHttpClient httpClient = new DefaultAsyncHttpClient(cf);

	public static AsyncHttpClient websocketClient = new DefaultAsyncHttpClient(cf);

	public WebSocket websocket = null;

	private IWebSocketDecoder iWebSocketDecoder;

	private BaseRobot iRobot;

	public NWSClient(IWebSocketDecoder decoder, BaseRobot iRobot) throws Exception {
		this.iWebSocketDecoder = decoder;
		this.iRobot = iRobot;
		init();
	}

	public void init() throws Exception {
		
	}

	public void connect() throws InterruptedException, ExecutionException {
		String webSocketUrl = RobotProperties.getInstance().getWebSocketUrl();
		int webSocketPort = RobotProperties.getInstance().getWebSocketPort();
		websocket = websocketClient.prepareGet(webSocketUrl + ":" + webSocketPort + "/websocket")
				.execute(new WebSocketUpgradeHandler.Builder()
						.addWebSocketListener(new WebSocketByteListener() {
							
							@Override
							public void onOpen(WebSocket websocket) {
								iRobot.connectSuccess();
							}
							
							@Override
							public void onError(Throwable t) {
								iRobot.onError(t);
							}
							
							@Override
							public void onClose(WebSocket websocket) {
								iRobot.onClose();
							}
							
							@Override
							public void onMessage(byte[] message) {
								Map<String, Object> decodeMsg = iWebSocketDecoder.decodeMsg(message);
								iRobot.onMessage(decodeMsg);
							}
						})
						.build()).get();
	}

	public void send(Map<String, Object> str) throws Exception {
		if (iWebSocketDecoder == null) {
			throw new Exception("iWebSocketDecoder can't be null");
		}
		byte[] encodeMsg = iWebSocketDecoder.encodeMsg(str);
		if(websocket!=null) {
			websocket.sendMessage(encodeMsg);	
		}	
	}

	public void close() throws IOException {
		websocket.close();
	}

}
