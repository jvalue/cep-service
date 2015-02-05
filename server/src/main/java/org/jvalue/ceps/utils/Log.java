package org.jvalue.ceps.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class Log {

	private Log() { }


	public static void debug(String msg) {
		getLogger().debug(msg);
	}


	public static void debug(String msg, Throwable throwable) {
		getLogger().debug(msg, throwable);
	}


	public static void info(String msg) {
		getLogger().info(msg);
	}


	public static void info(String msg, Throwable throwable) {
		getLogger().info(msg, throwable);
	}


	public static void warn(String msg) {
		getLogger().warn(msg);
	}


	public static void warn(String msg, Throwable throwable) {
		getLogger().warn(msg, throwable);
	}


	public static void error(String msg) {
		getLogger().error(msg);
	}


	public static void error(String msg, Throwable throwable) {
		getLogger().error(msg, throwable);
	}


	private static Logger getLogger() {
		StackTraceElement[] stackElements = Thread.currentThread().getStackTrace();
		return LoggerFactory.getLogger(stackElements[3].getClassName());
	}

}
