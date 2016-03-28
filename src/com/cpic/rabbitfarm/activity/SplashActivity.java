package com.cpic.rabbitfarm.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;

import com.cpic.rabbitfarm.R;

/**
 * 开屏页
 *
 */
public class SplashActivity extends Activity {
	private LinearLayout rootLayout;
	private static final int sleepTime = 2000;

	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.activity_flash);
		super.onCreate(arg0);
		rootLayout = (LinearLayout) findViewById(R.id.splash_root);
		AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
		animation.setDuration(2000);
		rootLayout.startAnimation(animation);
	}
	@Override
	protected void onStart() {
		super.onStart();

		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
				}
				finish();
				startActivity(new Intent(SplashActivity.this,LoginActivity.class));
			}
		}).start();
	}
}
