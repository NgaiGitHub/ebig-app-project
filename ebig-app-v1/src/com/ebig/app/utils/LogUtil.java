package com.ebig.app.utils;


public class LogUtil {

	public static void Log(Class cls, String message) {
		android.util.Log.i("--->["+cls.getName()+"]", message);
	}
	
	public static void Log(String tag, String message) {
		android.util.Log.i(tag, message);
	}

	public static void LogE(String tag, String message) {
		android.util.Log.e(tag, message);
	}
	public static void LogD(String tag, String message) {
		android.util.Log.d(tag, message);
		
	}
}
