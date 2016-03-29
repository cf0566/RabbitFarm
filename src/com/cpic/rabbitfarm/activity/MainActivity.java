package com.cpic.rabbitfarm.activity;

import android.os.Bundle;

import com.cpic.rabbitfarm.R;
import com.cpic.rabbitfarm.base.BaseActivity;

public class MainActivity extends BaseActivity {

	// 记录上次点击返回键的时间
	private long lastTime;
	@Override
	protected void getIntentData(Bundle savedInstanceState) {
		
	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void initView() {
		
	}

	@Override
	protected void initData() {
		
	}

	@Override
	protected void registerListener() {
		
	}
	
	@Override
	public void onBackPressed() {
		// 获取本次点击的时间
		long currentTime = System.currentTimeMillis();
		long dTime = currentTime - lastTime;
		if (dTime < 2000) {
			finish();
		} else {
			showShortToast("再按一次退出程序");
			lastTime = currentTime;
		}
	}
	
}
