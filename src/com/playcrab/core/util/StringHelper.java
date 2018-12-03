package com.playcrab.core.util;

/**
 * @Date: 2015年9月21日 下午3:34:29
 * @Author: zhuqd
 * @Description:字符串处理
 */
public class StringHelper {

	/**
	 * 检查字符串是否为空
	 * 
	 * @param string
	 * @return false is null, true is not null
	 */
	public static boolean isEmpty(String string) {
		if (string == null || "".equals(string)) {
			return true;
		}
		return false;
	}
}
