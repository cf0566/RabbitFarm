package com.cpic.rabbitfarm.popwindow;

import com.bumptech.glide.Glide;
import com.cpic.rabbitfarm.R;
import com.cpic.rabbitfarm.activity.MainActivity;
import com.cpic.rabbitfarm.utils.GlideRoundTransform;
import com.cpic.rabbitfarm.utils.MySeekBar;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.SeekBar;
import android.widget.TextView;

public class MinePop {

	private PopupWindow pw;
	private int screenWidth;
	private int screenHight;
	private Activity activity;
	private ImageView ivClose;
	private String token;
	private SharedPreferences sp;

	private TextView tvName, tvLevel, tvFarm, tvId;
	private ImageView ivOpen, ivOff;
	private Button btnRecord, btnLogout;
	private MySeekBar sBar;
	private ImageView ivIcon;

	private MediaPlayer mp;

	public MinePop(PopupWindow pw, int screenWidth, int screenHight, Activity activity, String token, MediaPlayer mp) {
		this.pw = pw;
		this.screenWidth = screenWidth;
		this.screenHight = screenHight;
		this.activity = activity;
		this.token = token;
		this.mp = mp;
	}
	public MinePop(PopupWindow pw, int screenWidth, int screenHight, Activity activity, String token) {
		this.pw = pw;
		this.screenWidth = screenWidth;
		this.screenHight = screenHight;
		this.activity = activity;
		this.token = token;
	}

	public void showMineMainPop() {
		View view = View.inflate(activity, R.layout.popwin_user_info, null);
		pw = new PopupWindow(view, screenWidth, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		
		sp = PreferenceManager.getDefaultSharedPreferences(activity);
		ivClose = (ImageView) view.findViewById(R.id.popwin_user_info_iv_close);
		tvName = (TextView) view.findViewById(R.id.popwin_userinfo_tv_name);
		tvLevel = (TextView) view.findViewById(R.id.popwin_userinfo_tv_level);
		tvFarm = (TextView) view.findViewById(R.id.popwin_userinfo_tv_farm_name);
		tvId = (TextView) view.findViewById(R.id.popwin_userinfo_tv_userid);
		ivOpen = (ImageView) view.findViewById(R.id.popwin_userinfo_ivsound);
		ivOff = (ImageView) view.findViewById(R.id.popwin_userinfo_ivnosound);
		btnRecord = (Button) view.findViewById(R.id.popwin_userinfo_btn_record);
		btnLogout = (Button) view.findViewById(R.id.popwin_userinfo_btn_logout);
		sBar = (MySeekBar) view.findViewById(R.id.popwin_userinfo_seekbar);
		ivIcon = (ImageView) view.findViewById(R.id.popwin_userinfo_iv_icon);
		tvName.setText("昵称： " + sp.getString("user_name", ""));
		tvLevel.setText((int) Double.parseDouble(sp.getString("level", "")) + "级");
		sBar.setProgress((int) Double.parseDouble(sp.getString("level", "")));

		tvFarm.setText("农场名称： " + sp.getString("farm_name", ""));
		tvId.setText("账号ID： " + sp.getString("user_id", ""));
		Glide.with(activity).load(sp.getString("user_img", "")).transform(new GlideRoundTransform(activity, 80))
				.fitCenter().into(ivIcon);

		WindowManager.LayoutParams params = activity.getWindow().getAttributes();
		params.alpha = 1f;
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

		ivClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pw.dismiss();
			}
		});

		ivOpen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mp.start();  
			}
		});
		ivOff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mp != null) {
					mp.stop();
				}
			}
		});
	}

}
