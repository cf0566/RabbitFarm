package com.cpic.rabbitfarm.base;

import java.util.Map;

import com.easemob.EMCallBack;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.domain.User;
import com.umeng.socialize.PlatformConfig;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class MyApplication extends Application {
	/*-------------------------------------环信声明部分------------------------------------*/

	public static Context applicationContext;
	private static MyApplication instance;
	// login user name
	public final String PREF_USERNAME = "username";

	/**
	 * 当前用户nickname,为了苹果推送不是userid而是昵称
	 */
	public static String currentUserNick = "";
	public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();

	/*-------------------------------------环信声明部分------------------------------------*/

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
		PlatformConfig.setWeixin("wx5d86349ad6e98c96", "bf8e4ec09ee63fcc7a8f76b8a8b45baf");
		PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");

		/*-------------------------------------引用环信部分------------------------------------*/

		instance = this;
		applicationContext = this;
		hxSDKHelper.onInit(applicationContext);

		/*-------------------------------------引用环信部分------------------------------------*/

	}
	
/*-------------------------------------引用环信部分------------------------------------*/
	
	public static MyApplication getInstance() {
		return instance;
	}
 
	/**
	 * 获取内存中好友user list
	 *
	 * @return
	 */
	public Map<String, User> getContactList() {
	    return hxSDKHelper.getContactList();
	}

	/**
	 * 设置好友user list到内存中
	 *
	 * @param contactList
	 */
	public void setContactList(Map<String, User> contactList) {
	    hxSDKHelper.setContactList(contactList);
	}

	/**
	 * 获取当前登陆用户名
	 *
	 * @return
	 */
	public String getUserName() {
	    return hxSDKHelper.getHXId();
	}

	/**
	 * 获取密码
	 *
	 * @return
	 */
	public String getPassword() {
		return hxSDKHelper.getPassword();
	}

	/**
	 * 设置用户名
	 *
	 * @param user
	 */
	public void setUserName(String username) {
	    hxSDKHelper.setHXId(username);
	}

	/**
	 * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
	 * 内部的自动登录需要的密码，已经加密存储了
	 *
	 * @param pwd
	 */
	public void setPassword(String pwd) {
	    hxSDKHelper.setPassword(pwd);
	}

	/**
	 * 退出登录,清空数据
	 */
	public void logout(final EMCallBack emCallBack) {
		// 先调用sdk logout，在清理app中自己的数据
	    hxSDKHelper.logout(emCallBack);
	}
	
	/*-------------------------------------引用环信部分------------------------------------*/
	
}
