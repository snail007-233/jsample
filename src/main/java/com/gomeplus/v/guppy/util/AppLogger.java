package com.gomeplus.v.guppy.util;

import ch.qos.logback.core.joran.spi.JoranException;
import com.snail.http.SimpleRequest;
import com.snail.http.SimpleResponse;
import com.snail.http.utils.ExceptionLogger;
import com.snail.http.utils.AccessLogger;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jodd.exception.ExceptionUtil;
import org.slf4j.LoggerFactory;

/**
 *
 * @author pengmeng
 */
public class AppLogger implements ExceptionLogger, AccessLogger {

	private static boolean inited = false;

	private static void init() {
		if (inited) {
			return;
		}
		try {
			LogBackConfigLoader.load(AppConfig.getLogbackXmlFilePath());
			inited = true;
		} catch (IOException | JoranException ex) {
			Logger.getLogger(AppLogger.class.getName()).log(Level.SEVERE, "Applogger.init", ex);
		}
	}

	public static org.slf4j.Logger logger() {
		init();
		return LoggerFactory.getLogger("logger");
	}

	public static org.slf4j.Logger logger(String logger) {
		init();
		return LoggerFactory.getLogger(logger);
	}

	public static void error(String msg) {
		//异常上报，上报位置待定
	}

	@Override
	public void write(String domain, Throwable thrwbl) {
		logger("sssl_error").error("[ " + domain + " ]" + ExceptionUtil.exceptionChainToString(thrwbl));
	}

	@Override
	public void write(SimpleRequest request, SimpleResponse response) {
		String method = request.getMethod().toString();
		logger("access_log").info(
			method + (method.length() == 3 ? " " : "")
			+ " - " + request.hostnameAndPort()
			+ " - " + request.getUri()
			+ " - " + response.http().getStatus().code()
			+ " - " + request.clientIP()
			+ " - " + request.headers().get("Referer")
			+ " - " + request.headers().get("User-Agent")
		);
	}
}
