package com.gomeplus.v.guppy.util;

import com.alibaba.fastjson.JSONObject;
import java.io.File;
import jodd.io.FileUtil;

/**
 *
 * @author pengmeng
 */
public final class AppConfig {

	public static ConfigLoader config;
	private static String prePath = "development";
	private static final String VERSION = "1.0.20";

	public static String getVersion() {
		return VERSION;
	}

	public static String prePath(String path) {
		return "config/" + prePath + "/" + path;
	}

	public static String config(String key) {
		if (config == null) {
			String path = !"classes".equals(new File(Jar.getPath(AppConfig.class)).getName()) ? Jar.getPath(AppConfig.class) + "/" + prePath("") : prePath("");
			config = new ConfigLoader(path, false, true)
				.setCfgFilename("config.ini", false);
			AppLogger.logger().info("use config file [ " + path + "config.ini ]");
		}

		String value = config.getValue(key);
		return value == null ? "" : value;
	}

	public static String getConfigIniFilePath() {
		return prePath("config.ini");
	}

	public static String getConsumerFilePath() {
		return prePath("consumer.json");
	}

	public static String getServerIniFilePath() {
		return prePath("server.ini");
	}

	public static String getLogbackXmlFilePath() {
		return prePath("logback.xml");
	}

	public static String getErrorLogFilePath() {
		return "log/error.log";
	}

	public static String getLogFilePath(String name) {
		return "log/" + name + ".log";
	}

	public static JSONObject getConsumerConfig() {
		JSONObject config0 = new JSONObject();
		if (new File(AppConfig.getConfigIniFilePath()).exists()) {
			String content = null;
			try {
				content = FileUtil.readString(getConsumerFilePath());
				config0 = JSONObject.parseObject(content);
			} catch (Exception e) {
				content = content == null ? "" : (content.startsWith("{}") ? "" : content);
				if (!content.isEmpty()) {
					AppLogger.logger().warn("load consumer config error , content :" + content, e);
				}
			}
		}
		return config0;
	}

	public static void setPrePath(String aPrePath) {
		prePath = aPrePath;
	}
}
