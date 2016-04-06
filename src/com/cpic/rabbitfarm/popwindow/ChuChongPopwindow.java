package com.cpic.rabbitfarm.popwindow;

import com.alibaba.fastjson.JSONObject;
import com.cpic.rabbitfarm.R;
import com.cpic.rabbitfarm.utils.UrlUtils;
import com.cpic.rabbitfarm.view.ProgressDialogHandle;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

public class ChuChongPopwindow {
	
	private PopupWindow pw;
	private int screenWidth;
	private Activity activity;
	/**
	 * 复用的控件
	 */
	private ImageView ivClose;
	private ImageView ivBack;
	
	private int ChuChongCount;
	
	private String token;
	
	/**
	 * 除虫版的控件
	 */
	private ImageView ivAdd,ivDel;
	private TextView tvCount;
	private Button btnEnsure;
	
	/**
	 * 土地选择控件
	 */
	private CheckBox cbox1,cbox2,cbox3,cbox4,cbox5,cbox6,cbox7;
	private Button btnChu;
	private int count = 0;//选择土地的数量
	
	/**
	 * 网络请求相关
	 */
	private HttpUtils post;
	private RequestParams params;
	private Dialog dialog;
	
	public ChuChongPopwindow( PopupWindow pw, int screenWidth, Activity activity,int ChuChongCount,String token) {
		this.pw = pw;
		this.screenWidth = screenWidth;
		this.activity = activity;
		this.ChuChongCount = ChuChongCount;
		this.token = token;
	}
	
	/**
	 * 弹出土地选择
	 */
	public void showChooseLandPop() {
		View view = View.inflate(activity, R.layout.popwin_chuchong_choose_land, null);
		pw = new PopupWindow(view, screenWidth, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		ivClose = (ImageView) view.findViewById(R.id.popwin_chuchong_choose_land_iv_close);
		cbox1 = (CheckBox) view.findViewById(R.id.popwin_chuchong_choose_land_cbox1);
		cbox2 = (CheckBox) view.findViewById(R.id.popwin_chuchong_choose_land_cbox2);
		cbox3 = (CheckBox) view.findViewById(R.id.popwin_chuchong_choose_land_cbox3);
		cbox4 = (CheckBox) view.findViewById(R.id.popwin_chuchong_choose_land_cbox4);
		cbox5 = (CheckBox) view.findViewById(R.id.popwin_chuchong_choose_land_cbox5);
		cbox6 = (CheckBox) view.findViewById(R.id.popwin_chuchong_choose_land_cbox6);
		cbox7 = (CheckBox) view.findViewById(R.id.popwin_chuchong_choose_land_cbox7);
		btnChu = (Button) view.findViewById(R.id.popwin_chuchong_choose_land_btn_chu);
		
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
		
		btnChu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (Integer.parseInt(tvCount.getText().toString()) == count) {
					ensureChuchong();
				}else{
					pw.dismiss();
					showMissMatchBanPop();
				}
			}

		});
		
		/**
		 * 全选监听
		 */
		cbox7.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					cbox1.setChecked(true);
					cbox2.setChecked(true);
					cbox3.setChecked(true);
					cbox4.setChecked(true);
					cbox5.setChecked(true);
					cbox6.setChecked(true);
				}
			}
		});
		cbox1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					count += 1;
				}else{
					cbox7.setChecked(false);
					count -= 1;
				}
			}
		});
		cbox2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					count += 1;
				}else{
					cbox7.setChecked(false);
					count -= 1;
				}
			}
		});
		cbox3.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					count += 1;
				}else{
					cbox7.setChecked(false);
					count -= 1;
				}
			}
		});
		cbox4.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					count += 1;
				}else{
					cbox7.setChecked(false);
					count -= 1;
				}
			}
		});
		cbox5.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					count += 1;
				}else{
					cbox7.setChecked(false);
					count -= 1;
				}
			}
		});
		cbox6.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					count += 1;
				}else{
					cbox7.setChecked(false);
					count -= 1;
				}
			}
		});
	}
	
	/**
	 * 确定除虫
	 */
	private void ensureChuchong() {
		dialog = ProgressDialogHandle.getProgressDialog(activity, null);
		post = new HttpUtils();
		params = new RequestParams();
		String url = UrlUtils.postUrl+UrlUtils.path_weeding;
		params.addBodyParameter("token", token);
		params.addBodyParameter("count", count+"");
		post.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();
				if (dialog != null) {
					dialog.show();
				}
			}
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				if (dialog != null) {
					dialog.dismiss();
				}
				Toast.makeText(activity, "除虫失败，请检查网络连接", 0).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				JSONObject obj = JSONObject.parseObject(arg0.result);
				int code = obj.getIntValue("code");
				if (code == 1) {
					pw.dismiss();
					showChuchongSuccessPop();
				}else{
					Toast.makeText(activity, "除虫失败", 0).show();
				}
			}
		});
	}
	/**
	 * 弹出除虫版选择
	 */
	public void showChooseBanPop() {
		View view = View.inflate(activity, R.layout.popwin_chuchong_choose_ban, null);
		pw = new PopupWindow(view, screenWidth, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		ivClose = (ImageView) view.findViewById(R.id.popwin_chuchong_choose_ban_iv_close);
		ivAdd =  (ImageView) view.findViewById(R.id.popwin_chuchong_choose_ban_iv_add);
		ivDel =  (ImageView) view.findViewById(R.id.popwin_chuchong_choose_ban_iv_del);
		tvCount = (TextView) view.findViewById(R.id.popwin_chuchong_choose_ban_tv_count);
		btnEnsure = (Button) view.findViewById(R.id.popwin_chuchong_choose_ban_btn_ensure);
		
		WindowManager.LayoutParams params =	activity.getWindow().getAttributes();
		params.alpha = 1f;
		activity.getWindow().setAttributes(params);
		pw.setBackgroundDrawable(new ColorDrawable());
		pw.setOutsideTouchable(false);
		pw.showAtLocation(view, Gravity.CENTER, 0, 0);
		pw.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		
		tvCount.setText(1+"");
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
		
		ivAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int count = Integer.parseInt(tvCount.getText().toString());
				count++;
				tvCount.setText(count+"");
			}
		});
		
		ivDel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int count = Integer.parseInt(tvCount.getText().toString());
				count--;
				if (count >= 0 ) {
					tvCount.setText(count+"");
				}else{
					Toast.makeText(activity, "土地数量不得为负", 0).show();
				}
			}
		});
		btnEnsure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (ChuChongCount >= Integer.parseInt(tvCount.getText().toString())&&Integer.parseInt(tvCount.getText().toString())>0) {
					pw.dismiss();
					showChooseLandPop();
				}else{
					pw.dismiss();
					showLessBanPop();
				}
			}
		});
	}
	/**
	 * 灭虫板不足情况
	 */
	private void showMissMatchBanPop() {
		View view = View.inflate(activity, R.layout.popwin_chuchong_lessban, null);
		pw = new PopupWindow(view, screenWidth, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		ivClose = (ImageView) view.findViewById(R.id.popwin_chuchong_lessban_iv_close);
		ivBack = (ImageView) view.findViewById(R.id.popwin_chuchong_lessban_iv_back);
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
		ivBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pw.dismiss();
				showChooseBanPop();
			}
		});
	}
	/**
	 * 除虫成功Pop
	 */
	private void showChuchongSuccessPop() {
		View view = View.inflate(activity, R.layout.popwin_chuchong_success, null);
		pw = new PopupWindow(view, screenWidth, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		ivClose = (ImageView) view.findViewById(R.id.popwin_chuchong_success_iv_close);
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
	}
	/**
	 * 土地与灭虫版不符合情况
	 */
	private void showLessBanPop() {
		View view = View.inflate(activity, R.layout.popwin_chuchong_moreban, null);
		pw = new PopupWindow(view, screenWidth, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		ivClose = (ImageView) view.findViewById(R.id.popwin_chuchong_moreban_iv_close);
		ivBack = (ImageView) view.findViewById(R.id.popwin_chuchong_moreban_iv_back);
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
		ivBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pw.dismiss();
				showChooseBanPop();
			}
		});
	}
	
	/**
	 * 弹出没有除虫版
	 */
	public void showNoBanPop(){
		View view = View.inflate(activity, R.layout.popwin_chuchong_nochuchongban, null);
		pw = new PopupWindow(view, screenWidth, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		ivClose = (ImageView) view.findViewById(R.id.popwin_noban_iv_close);
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
	}
}
