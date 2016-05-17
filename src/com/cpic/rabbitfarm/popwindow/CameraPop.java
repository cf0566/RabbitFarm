package com.cpic.rabbitfarm.popwindow;

import com.cpic.rabbitfarm.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

public class CameraPop {

	private PopupWindow pw;
	private int screenWidth;
	private Activity activity;
	private ImageView ivClose;
	private WebView web;
	private SharedPreferences sp;
	
	public CameraPop( PopupWindow pw, int screenWidth, Activity activity,String token) {
		this.pw = pw;
		this.screenWidth = screenWidth;
		this.activity = activity;
	}
	
	/**
	 * 弹出土地选择
	 */
	public void showLookCameraPop() {
		View view = View.inflate(activity, R.layout.popwin_camera, null);
		pw = new PopupWindow(view, screenWidth*4/5, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		ivClose = (ImageView) view.findViewById(R.id.pop_camera_iv_close);
		web = (WebView) view.findViewById(R.id.pop_camera_web);
		
		
		WindowManager.LayoutParams params =	activity.getWindow().getAttributes();
		params.alpha = 0.6f;
		activity.getWindow().setAttributes(params);
		pw.setBackgroundDrawable(new ColorDrawable());
		pw.setOutsideTouchable(false);
		pw.showAtLocation(view, Gravity.CENTER, 0, 0);
		pw.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		pw.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				WindowManager.LayoutParams params = activity.getWindow().getAttributes();
				params.alpha = 1f;
				activity.getWindow().setAttributes(params);
			}
		});

		sp = PreferenceManager.getDefaultSharedPreferences(activity);
		String url = sp.getString("video_url", "");
		WebSettings webSetting = web.getSettings();
		webSetting.setJavaScriptEnabled(true);
		web.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		web.loadUrl(url);
		
		
		ivClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pw.dismiss();
			}
		});
	}
}
