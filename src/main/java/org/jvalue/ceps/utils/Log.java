package org.jvalue.ceps.utils;

import org.apache.log4j.Logger;


public final class Log {

	private Log() { }


	public static void debug(String msg) {
		Logger.getLogger(getCallerName()).debug(msg);
	}

	public static void debug(String msg, Throwable throwable) {
		Logger.getLogger(getCallerName()).debug(msg, throwable);
	}


	public static void info(String msg) {
		Logger.getLogger(getCallerName()).info(msg);
	}

	public static void info(String msg, Throwable throwable) {
		Logger.getLogger(getCallerName()).info(msg, throwable);
	}


	public static void warn(String msg) {
		Logger.getLogger(getCallerName()).warn(msg);
	}

	public static void warn(String msg, Throwable throwable) {
		Logger.getLogger(getCallerName()).warn(msg, throwable);
	}


	public static void error(String msg) {
		Logger.getLogger(getCallerName()).error(msg);
	}

	public static void error(String msg, Throwable throwable) {
		Logger.getLogger(getCallerName()).error(msg, throwable);
	}


	private static String getCallerName() {
		return Thread.currentThread().getStackTrace()[3].getClassName();
	}

}
