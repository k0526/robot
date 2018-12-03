package com.playcrab.core.util;

import java.text.SimpleDateFormat;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.ObjectMapper;

public class JacksonUtils {
	private static ObjectMapper mapper = null;
	static {
		mapper = new ObjectMapper();
		mapper.setVisibilityChecker(mapper.getSerializationConfig()
				.getDefaultVisibilityChecker().withFieldVisibility(
						JsonAutoDetect.Visibility.ANY).withGetterVisibility(
						JsonAutoDetect.Visibility.NONE).withSetterVisibility(
						JsonAutoDetect.Visibility.NONE).withIsGetterVisibility(
						JsonAutoDetect.Visibility.NONE).withCreatorVisibility(
						JsonAutoDetect.Visibility.NONE));
		mapper.getSerializationConfig().setDateFormat(
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}

	public static String toJson(Object value) {
		try {

			return mapper.writeValueAsString(value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T toJavaBean(String json, Class<T> targetCls) {
		try {

			return mapper.readValue(json, targetCls);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static JsonNode getJsonNode(String json) {
		try {

			return mapper.readTree(json);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
