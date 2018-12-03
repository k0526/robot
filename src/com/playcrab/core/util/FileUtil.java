package com.playcrab.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Date: 2015年8月10日 下午5:54:29
 * @Author: zhuqd
 * @Description: 文件工具类
 */
public class FileUtil {

	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * 获取项的path
	 * 
	 * @return
	 */
	public static String getProjectPath() {
		return System.getProperty("user.dir");
	}

	/**
	 * 获取bin的路径
	 * 
	 * @return
	 */
	public static String getBinPath() {
		return getProjectPath() + "/bin";
	}

	/**
	 * 读取properties文件
	 * 
	 * @param path
	 * @return
	 */
	public static Map<String, String> readProperties(String path) {
		if (StringHelper.isEmpty(path)) {
			throw new RuntimeException("properties file path is null");
		}
		Map<String, String> map = new HashMap<>();
		Properties props = new Properties();
		InputStream inputStream;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(path));
			props.load(inputStream);
			Enumeration<?> en = props.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				String value = props.getProperty(key);
				map.put(key, value);
			}
		} catch (IOException e) {
			throw new RuntimeException("read properties file error", e);
		}
		return map;

	}

	/**
	 * 读取properties文件
	 * 
	 * @param properties
	 * @return
	 */
	public static Map<String, String> readProperties(File properties) {
		return readProperties(properties.getPath());
	}

	/**
	 * 查找文件，若有多个同名文件，返回第一个
	 * 
	 * @param name
	 * @return if not find ,return null
	 */
	public static File findFile(String path, String name) {
		File file = new File(path);
		if (!file.exists()) {
			return null;
		}
		boolean withSuffix = false;
		if (name.contains(".")) {
			withSuffix = true;
		}
		return compareFile(file, name, withSuffix);
	}

	/**
	 * 
	 * @param file
	 * @param name
	 * @param withSuffix
	 * @return
	 */
	private static File compareFile(File file, String name, boolean withSuffix) {
		if (file.isFile()) {
			// System.out.println(file.getName());
			String prefix;
			if (!withSuffix) {
				prefix = file.getName().split("\\.")[0].trim();
			} else {
				prefix = file.getName().trim();
			}
			if (prefix.endsWith(name)) {
				return file;
			}
		}
		File find = null;
		if (file.isDirectory()) {
			File[] list = file.listFiles();
			for (File f : list) {
				find = compareFile(f, name, withSuffix);
				if (find != null) {
					return find;
				}
			}
		}
		return null;
	}

	/**
	 * 将文件转成一个字符串
	 * 
	 * @param configFile
	 * @return
	 * @throws IOException
	 */
	public static String fileToString(File configFile) throws IOException {
		// BufferedReader reader = new BufferedReader(new InputStreamReader(
		// new FileInputStream(file), encoding));
		String text = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile), "utf-8"),
				1024 * 8); // 默认最大8Mcache
		String line = null;
		while ((line = reader.readLine()) != null) {
			text += line;
		}
		reader.close();
		return text;
	}

	/**
	 * 复制文件到
	 * 
	 * @param srcPath
	 * @param destPath
	 * @return
	 */
	public static boolean copyTo(String srcPath, String destPath) {
		File srcFile = new File(srcPath);
		if (!srcFile.exists() || srcFile.isDirectory()) {
			logger.error("file is not exsit or is a directory");
			return false;
		}
		try {
			File file = new File(destPath);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			//
			FileOutputStream out = new FileOutputStream(file, false);
			BufferedReader reader = new BufferedReader(new FileReader(srcFile), 1024 * 8);// 默认最大8Mcache
			String line = "";
			while ((line = reader.readLine()) != null) {
				out.write(line.getBytes("utf-8"));
				out.write("\r\n".getBytes("utf-8"));
			}
			out.flush();
			reader.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 获取文件后缀
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileType(File file) {
		if (!file.exists()) {
			return "";
		}
		String name = file.getName();
		if (name.endsWith("properties")) {
			return "properties";
		} else if (name.endsWith("xml")) {
			return "xml";
		} else if (name.endsWith("json")) {
			return "json";
		}
		return "";
	}
}
