package com.playcrab.core.util;

/**
 * @Date: 2015年9月21日 下午4:38:38
 * @Author: zhuqd
 * @Description:
 */
public class Value {

	/**
	 * string to int
	 * 
	 * @param value
	 * @return
	 */
	public static int parserInt(String value) {
		if (StringHelper.isEmpty(value)) {
			return 0;
		}
		int number = 0;
		try {
			number = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return number;
	}

	/**
	 * string to long
	 * 
	 * @param value
	 * @return
	 */
	public static long parserLong(String value) {
		if (StringHelper.isEmpty(value)) {
			return 0;
		}
		long number = 0;
		try {
			number = Long.parseLong(value);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return number;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static double parserDouble(String value) {
		if (StringHelper.isEmpty(value)) {
			return 0d;
		}
		double number = 0;
		try {
			number = Double.parseDouble(value);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return number;
	}

	/**
	 * 将字符串转换成数组<br>
	 * 分隔符','
	 * 
	 * @param value
	 * @return
	 */
	public static String[] parserStringToArray(String value) {
		if (value == null || "".equals(value)) {
			return new String[0];
		}
		String[] array = value.split(",");
		return array;
	}

	/**
	 * 将字符串转换成数组
	 * 
	 * @param value
	 * @return
	 */
	public static int[] parserStringToIntArray(String value) {
		if (value == null || "".equals(value)) {
			return new int[0];
		}
		String[] arr = parserStringToArray(value);
		int[] array = new int[arr.length];
		for (int i = 0; i < arr.length; i++) {
			array[i] = parserInt(arr[i]);
		}
		return array;
	}

	/**
	 * 将字符串转换成数组
	 * 
	 * @param value
	 * @return
	 */
	public static long[] parserStringToLongArray(String value) {
		if (value == null || "".equals(value)) {
			return new long[0];
		}
		String[] arr = parserStringToArray(value);
		long[] array = new long[arr.length];
		for (int i = 0; i < arr.length; i++) {
			array[i] = parserInt(arr[i]);
		}
		return array;
	}

	/**
	 * double 转long
	 * 
	 * @return
	 */
	public static long DoubleToLong(Double value) {
		if (value == null) {
			return 0L;
		}
		return value.longValue();
	}

	/**
	 * double转int
	 * 
	 * @param value
	 * @return
	 */
	public static int DoubleToInt(Double value) {
		if (value == null) {
			return 0;
		}
		return value.intValue();
	}

	/**
	 * 获取double的
	 * 
	 * @param value
	 * @return
	 */
	public static double getDouble(Double value) {
		if (value == null) {
			return 0;
		}
		return value;
	}

	/**
	 * 获取int值
	 * 
	 * @param value
	 * @return
	 */
	public static int getInteger(Integer value) {
		if (value == null) {
			return 0;
		}
		return value;
	}

	/**
	 * 获取long
	 * 
	 * @param value
	 * @return
	 */
	public static long getLong(Long value) {
		if (value == null) {
			return 0;
		}
		return value;
	}

	/**
	 * 获取非null的string
	 * 
	 * @param string
	 * @return
	 */
	public static String getString(String string) {
		if (string == null) {
			return "";
		}
		return string;
	}
}
