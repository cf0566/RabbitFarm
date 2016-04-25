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
import com.umeng.socialize.utils.Log;

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

public class ShiFeiPopwindow {
	
	private PopupWindow pw;
	private int screenWidth;
	private Activity activity;
	/**
	 * 复用的控件
	 */
	private ImageView ivClose;
	private ImageView ivBack;
	
	
	private String token;
	/**
	 * 赠送土地的控件
	 */
	private Button btnGive;
	
	
	/**
	 *	施肥的控件
	 */
	private ImageView ivAdd,ivDel;
	private TextView tvCount;
	private Button btnEnsure;
	
	/**
	 * 土地选择控件
	 */
	private CheckBox cbox1,cbox2,cbox3,cbox4,cbox5,cbox6,cbox7;
	private Button btnShi;
	private int count = 0;//选择土地的数量
	
	/**
	 * 网络请求相关
	 */
	private HttpUtils post;
	private RequestParams params;
	private Dialog dialog;
	
	public ShiFeiPopwindow( PopupWindow pw, int screenWidth, Activity activity,String token) {
		this.pw = pw;
		this.screenWidth = screenWidth;
		this.activity = activity;
		this.token = token;
	}
	
	/**
	 * 弹出土地选择
	 */
	public void showChoose2Pop() {
		View view = View.inflate(activity, R.layout.popwin_shifei_choose2, null);
		pw = new PopupWindow(view, screenWidth, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		count = 0;
		ivClose = (ImageView) view.findViewById(R.id.popwin_shifei_choose2_iv_close);
		cbox1 = (CheckBox) view.findViewById(R.id.popwin_shifei_choose2_cbox1);
		cbox2 = (CheckBox) view.findViewById(R.id.popwin_shifei_choose2_cbox2);
		cbox3 = (CheckBox) view.findViewById(R.id.popwin_shifei_choose2_cbox3);
		cbox4 = (CheckBox) view.findViewById(R.id.popwin_shifei_choose2_cbox4);
		cbox5 = (CheckBox) view.findViewById(R.id.popwin_shifei_choose2_cbox5);
		cbox6 = (CheckBox) view.findViewById(R.id.popwin_shifei_choose2_cbox6);
		cbox7 = (CheckBox) view.findViewById(R.id.popwin_shifei_choose2_cbox7);
		btnShi = (Button) view.findViewById(R.id.popwin_shifei_choose2_btn_shi);
		
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
		ivClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pw.dismiss();
			}
		});
		
		btnShi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Log.i("oye",tvCount.getText().toString()+"-----"+count);
				if (Integer.parseInt(tvCount.getText().toString()) == count) {
					ensureChuchong();
				}else{
					pw.dismiss();
					showMissLandPop();
					
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
	 * 确定施肥
	 */
	private void ensureChuchong() {
		dialog = ProgressDialogHandle.getProgressDialog(activity, null);
		post = new HttpUtils();
		params = new RequestParams();
		String url = UrlUtils.postUrl+UrlUtils.path_fertilization;
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
					showShiFeiSuccessPop();
				}else{
					Toast.makeText(activity, "除虫失败", 0).show();
				}
			}
		});
	}
	/**
	 * 弹出农家肥选择
	 */
	public void showChoose1Pop() {
		View view = View.inflate(activity, R.layout.popwin_shifei_choose1, null);
		pw = new PopupWindow(view, screenWidth, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		ivClose = (ImageView) view.findViewById(R.id.popwin_shifei_choose1_iv_close);
		ivAdd =  (ImageView) view.findViewById(R.id.popwin_shifei_choose1_iv_add);
		ivDel =  (ImageView) view.findViewById(R.id.popwin_shifei_choose1_iv_del);
		tvCount = (TextView) view.findViewById(R.id.popwin_shifei_choose1_tv_count);
		btnEnsure = (Button) view.findViewById(R.id.popwin_shifei_choose1_btn_ensure);
		
		WindowManager.LayoutParams params =	activity.getWindow().getAttributes();
		params.alpha = 0.6f;
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
				int landCount = Integer.parseInt(tvCount.getText().toString());
				landCount++;
				tvCount.setText(landCount+"");
			}
		});
		
		ivDel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int landCount = Integer.parseInt(tvCount.getText().toString());
				landCount--;
				if (landCount >= 0 ) {
					tvCount.setText(landCount+"");
				}else{
					Toast.makeText(activity, "土地数量不得为负", 0).show();
				}
			}
		});
		btnEnsure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Log.i("oye",tvCount.getText().toString());
				if (Integer.parseInt(tvCount.getText().toString())>0&&Integer.parseInt(tvCount.getText().toString())<=6) {
					pw.dismiss();
					showChoose2Pop();
				}else{
					pw.dismiss();
					showLessFei();
				}
			}
		});
	}
	/**
	 * 农家肥数量选择不足施肥数量情况
	 */
	private void showLessFei() {
		View view = View.inflate(activity, R.layout.popwin_shifei_lessfei, null);
		pw = new PopupWindow(view, screenWidth, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		ivClose = (ImageView) view.findViewById(R.id.popwin_shifei_lessfei_iv_close);
		ivBack = (ImageView) view.findViewById(R.id.popwin_shifei_lessfei_iv_back);
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
				showChoose1Pop();
			}
		});
	}
	/**
	 * 施肥成功Pop
	 */
	private void showShiFeiSuccessPop() {
		View view = View.inflate(activity, R.layout.popwin_shifei_success, null);
		pw = new PopupWindow(view, screenWidth, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		ivClose = (ImageView) view.findViewById(R.id.popwin_shifei_success_iv_close);
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
	 * 土地与施肥不符合情况
	 */
	private void showMissLandPop() {
		View view = View.inflate(activity, R.layout.popwin_shifei_missland, null);
		pw = new PopupWindow(view, screenWidth, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		ivClose = (ImageView) view.findViewById(R.id.popwin_shifei_miss_iv_close);
		ivBack = (ImageView) view.findViewById(R.id.popwin_shifei_miss_iv_back);
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
				showChoose1Pop();
			}
		});
	}
	
	/**
	 * 赠送农家肥
	 */
	public void showGiveFeiPop(){
		View view = View.inflate(activity, R.layout.popwin_shifei_givefei, null);
		pw = new PopupWindow(view, screenWidth, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		btnGive = (Button) view.findViewById(R.id.popwin_shifei_give_btn_ensure);
		ivClose = (ImageView) view.findViewById(R.id.popwin_shifei_give_iv_close);
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
		
		btnGive.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pw.dismiss();
				showChoose1Pop();
			}
		});
	}
}
