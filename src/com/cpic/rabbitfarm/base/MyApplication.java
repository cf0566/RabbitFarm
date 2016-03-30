package com.cpic.rabbitfarm.base;

import com.umeng.socialize.PlatformConfig;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class MyApplication extends Application{
	/**
	 * 日志的开关，false：不打印Log；true：打印Log
	 */
	public static final boolean isDebug = false;
	
	/**
	 * 全局上下文
	 */
	private Context mContext;
	
	/**
	 * 屏幕的宽度
	 */
	public static int mDisplayWitdh;
	
	/**
	 * 屏幕的高度
	 */
	public static int mDisplayHeight;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		PlatformConfig.setWeixin("wxd930ea5d5a258f4f", "db426a9829e4b49a0dcac7b4162da6b6");
		PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba"); 
	}
}
