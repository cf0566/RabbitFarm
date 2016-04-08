package com.cpic.rabbitfarm.popwindow;

import com.cpic.rabbitfarm.R;
import com.umeng.socialize.utils.Log;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

public class MessageMainPop {

	private PopupWindow pw;
	private int screenWidth;
	private Activity activity;
	private ImageView ivClose;
	private ImageView ivMessage;
	private String token;
	
	private ImageView ivActivity,ivPlant,ivFriend;
	private LinearLayout llActivity,llPlant,llFriend;
	
	private int activityUnread,MessageUnread;
	
	public MessageMainPop(PopupWindow pw, int screenWidth, Activity activity,String token,int activityUnread,int MessageUnread) {
		this.pw = pw;
		this.screenWidth = screenWidth;
		this.activity = activity;
		this.token = token;
		this.activityUnread = activityUnread;
		this.MessageUnread = MessageUnread;
	}
	
	/**
	 * 弹出消息选择
	 */
	public void showMessageMainPop() {
		View view = View.inflate(activity, R.layout.popwin_message_main, null);
		pw = new PopupWindow(view, screenWidth*4/5, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		
		ivClose = (ImageView) view.findViewById(R.id.popwin_message_main_iv_close);
		ivActivity = (ImageView) view.findViewById(R.id.popwin_message_main_iv_activity_tis);
		ivPlant = (ImageView) view.findViewById(R.id.popwin_message_main_iv_plant_tis);
		ivFriend = (ImageView) view.findViewById(R.id.popwin_message_main_iv_friend_tis);
		llActivity = (LinearLayout) view.findViewById(R.id.popwin_message_main_ll_activity);
		llPlant = (LinearLayout) view.findViewById(R.id.popwin_message_main_ll_plant);
		llFriend = (LinearLayout) view.findViewById(R.id.popwin_message_main_ll_friends);
		
		if (activityUnread != 0) {
			ivActivity.setVisibility(View.VISIBLE);
		}else{
			ivActivity.setVisibility(View.INVISIBLE);
		}
		if (MessageUnread != 0) {
			ivPlant.setVisibility(View.VISIBLE);
		}else{
			ivPlant.setVisibility(View.INVISIBLE);
		}
		
		WindowManager.LayoutParams params =	activity.getWindow().getAttributes();
		params.alpha = 1f;
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
		
		ivClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pw.dismiss();
			}
		});
		
		
		llActivity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pw.dismiss();
				ActivityPop pop = new ActivityPop(pw, screenWidth, activity, token);
				pop.showActivityMainPop();
			}
		});
		llPlant.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(activity, "庄家成熟通知", 0).show();
			}
		});
		llFriend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(activity, "好友信息", 0).show();
			}
		});
		
	}
}
