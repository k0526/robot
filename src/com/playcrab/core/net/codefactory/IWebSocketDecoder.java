package com.playcrab.core.net.codefactory;

import java.util.Map;

public interface IWebSocketDecoder {
		
	public byte[] encodeMsg(Map<String, Object> str);	
	
	public Map<String, Object> decodeMsg(byte[] bytes);
	
}
