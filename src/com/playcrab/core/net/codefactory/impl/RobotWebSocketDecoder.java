package com.playcrab.core.net.codefactory.impl;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import com.playcrab.core.net.codefactory.IWebSocketDecoder;
import com.playcrab.core.util.JacksonUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class RobotWebSocketDecoder implements IWebSocketDecoder {

	@Override
	public byte[] encodeMsg(Map<String, Object> msg) {
		Short cmd = (Short) msg.get("cmd");
		boolean hasData = msg.containsKey("data");
		byte[] dataArr = null;
		short len = 2;
		if (hasData) {
			String data = JacksonUtils.toJson(msg.get("data"));
			dataArr = data.getBytes(Charset.forName("utf-8"));
			len = (short) (dataArr.length + 2);
		}
		ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(len + 2);
		buf.writeShort(len);
		buf.writeShort(cmd);
		if (hasData) {
			buf.writeBytes(dataArr);
		}
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);

		return req;
	}

	@Override
	public Map<String, Object> decodeMsg(byte[] bytes) {
		ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(bytes.length);
		buffer.writeBytes(bytes);
		short len = buffer.readShort();
		byte compress_flag = buffer.readByte();
		short cmd = buffer.readShort();
		String data = null;
		if (buffer.readableBytes() > 0) {
			byte[] dataArr = new byte[len - 5];
			buffer.readBytes(dataArr);
			if (compress_flag == 1) {
				byte[] src;
				try {
					src = decompress_deflater(dataArr);
					data = new String(src, Charset.forName("utf-8"));
				} catch (DataFormatException e) {
					e.printStackTrace();
				}
			} else {
				data = new String(dataArr, Charset.forName("utf-8"));
			}
		}
		Map<String, Object> map = new HashMap<>();
		map.put("cmd", cmd + "");
		if (cmd != 1) {
			System.out.println("cmd =" + cmd + " data =" + data);// 这个地方打印出了所有的消息
			HashMap javaBean = JacksonUtils.toJavaBean(data, HashMap.class);
			map.putAll(javaBean);
		} else { // 这个是服务器的心跳协议
		}
		return map;
	}

	public static byte[] decompress_deflater(byte[] src) throws DataFormatException {
		byte[] result = new byte[1024];
		Inflater inf = new Inflater();
		inf.setInput(src);
		int value;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while (!inf.finished()) {
			value = inf.inflate(result);
			bos.write(result, 0, value);
		}
		inf.end();
		byte[] outputs = bos.toByteArray();
		return outputs;
	}
}
