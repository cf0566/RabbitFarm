package com.cpic.rabbitfarm.popwindow;

import com.cpic.rabbitfarm.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

public class BuyCoinPop {

	private PopupWindow pw;
	private int screenWidth;
	private int screenHight;
	private Activity activity;
	private HttpUtils post;
	private String token;
	private RequestParams params;
	private Dialog dialog;

	/**
	 * 购买界面控件
	 */
	private ImageView ivDel, ivAdd;
	private TextView tvCount;
	private TextView tvSum;
	private Button btnBuy;
	private ImageView ivClose;

	private int count = 0;

	public BuyCoinPop(PopupWindow pw, int screenWidth, int screenHight, Activity activity, String token) {
		this.pw = pw;
		this.screenWidth = screenWidth;
		this.activity = activity;
		this.token = token;
		this.screenHight = screenHight;
	}

	/**
	 * 弹出活动主界面
	 */
	public void showBuyCoinPop() {
		View view = View.inflate(activity, R.layout.popwin_buy_rabbit_coin, null);
		pw = new PopupWindow(view, screenWidth, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);

		ivDel = (ImageView) view.findViewById(R.id.popwin_buy_rabbit_coin_ivdel);
		ivAdd = (ImageView) view.findViewById(R.id.popwin_buy_rabbit_coin_ivadd);
		tvCount = (TextView) view.findViewById(R.id.popwin_buy_rabbit_coin_tv_num);
		tvSum = (TextView) view.findViewById(R.id.popwin_buy_rabbit_coin_tv_sum);
		btnBuy = (Button) view.findViewById(R.id.popwin_buy_rabbit_coin_btn_ensure);
		ivClose = (ImageView) view.findViewById(R.id.popwin_buy_rabbit_coin_iv_close);
		
		tvCount.setText(count + "");
		tvSum.setText("合计： "+count+"元");
		
		WindowManager.LayoutParams params = activity.getWindow().getAttributes();
		params.alpha = 0.6f;
		activity.getWindow().setAttributes(params);
		pw.setBackgroundDrawable(new ColorDrawable());
		pw.setOutsideTouchable(false);
		pw.showAtLocation(view, Gravity.CENTER, 0, 0);
		pw.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				WindowManager.LayoutParams params = activity.getWindow().getAttributes();
				params.alpha = 1f;
				activity.getWindow().setAttributes(params);
			}
		});

		ivDel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (count >= 1) {
					count--;
					tvCount.setText(count+"");
					tvSum.setText("合计： "+count+"元");
				}else{
					tvCount.setText(0+"");
					tvSum.setText("合计： "+0+"元");
				}
			}
		});

		ivAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				count++;
				tvCount.setText(count+"");
				tvSum.setText("合计： "+count+"元");
			}
		});
		
		ivClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pw.dismiss();
			}
		});

	}
}
