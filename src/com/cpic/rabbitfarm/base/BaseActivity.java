package com.cpic.rabbitfarm.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
/**
 * 
 * @author Taylor
 * 
 * name:Activity的基类
 *
 */
public abstract class BaseActivity extends FragmentActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getIntentData(savedInstanceState);
		loadXml();
		initView();
		initData();
		registerListener();
	}
	
	/**
	 * 得到上一个Activity传来的Intent数据
	 * @param savedInstanceState
	 */
	protected abstract void getIntentData(Bundle savedInstanceState);
	/**
	 * 加载布局
	 */
	protected abstract void loadXml() ;
	/**
	 * 初始化控件
	 */
	protected abstract void initView();
	/**
	 * 初始化数据
	 */
	protected abstract void initData() ;
	/**
	 * 设置监听
	 */
	protected abstract void registerListener();
	
	/**
	 * Toast长显示
	 * @param msg
	 */
	protected void showLongToast(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
	/**
	 * Toast短显示
	 * @param msg
	 */
	protected void showShortToast(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
}
