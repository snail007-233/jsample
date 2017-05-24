
package com.gomeplus.v.guppy;

import com.gomeplus.v.guppy.util.AppConfig;
import com.gomeplus.v.guppy.util.AppLogger;
import com.snail.http.server.SimpleHttpServer;
import java.io.File;
import jodd.exception.ExceptionUtil;

/**
 *
 * @author pengmeng
 */
public class Bootstrap {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		try {
			if (args.length == 1) {
				AppConfig.setPrePath(args[0]);
			} else {
				AppConfig.setPrePath("development");
			}
			//因为logback的影响,netty会输出调试信息，这里阻止netty输出调试信息
			//io.netty.util.internal.logging.InternalLoggerFactory.setDefaultFactory(new io.netty.util.internal.logging.JdkLoggerFactory());
			AppLogger.logger().info("Guppy service started");
			new SimpleHttpServer(new File(AppConfig.getServerIniFilePath())).run();
		} catch (Exception ex) {
			AppLogger.logger().error(ExceptionUtil.exceptionChainToString(ex));
		}
	}
}
