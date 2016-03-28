package com.cpic.rabbitfarm.activity;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.cpic.rabbitfarm.R;
import com.cpic.rabbitfarm.base.BaseActivity;
import com.cpic.rabbitfarm.utils.UrlUtils;
import com.cpic.rabbitfarm.view.ProgressDialogHandle;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class LoginActivity extends BaseActivity{

	private LinearLayout ll;
	private Button btnMobile,btnQQ,btnWX;
	private PopupWindow pwLogin;
	private PopupWindow pwRegister;
	private PopupWindow pwForget;
	private PopupWindow pwSetPwd;
	private int screenWidth, screenHight;
	private int index = 0;
	/**
	 * 登录界面控件
	 */
	private EditText etName;
	private EditText etPwd;
	private ImageView ivDelete,ivForget;
	private Button btnLogin;
	private Button btnRegister;
	private Dialog dialog;
	
	private HttpUtils post;
	private RequestParams params;
	
	@Override
	protected void getIntentData(Bundle savedInstanceState) {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		screenHight = metrics.heightPixels;
	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_login);
	}

	@Override
	protected void initView() {
		ll = (LinearLayout) findViewById(R.id.activity_login_layout);
		btnMobile = (Button) findViewById(R.id.activity_login_btn_mobile);
		btnQQ = (Button) findViewById(R.id.activity_login_btn_qq);
		btnWX = (Button) findViewById(R.id.activity_login_btn_wx);
		dialog = ProgressDialogHandle.getProgressDialog(LoginActivity.this, null);
	}

	@Override
	protected void initData() {
		
	}

	@Override
	protected void registerListener() {
		btnMobile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showLoginPopupWindow();
				ll.setVisibility(View.GONE);
			}
		});
	}
	
	private void showLoginPopupWindow() {
		View view = View.inflate(LoginActivity.this,R.layout.popwin_loading, null);
		pwLogin = new PopupWindow(view, screenWidth / 2,LayoutParams.WRAP_CONTENT);
		pwLogin.setFocusable(true);
		etName = (EditText) view.findViewById(R.id.popwin_loading_et_name);
		etPwd = (EditText) view.findViewById(R.id.popwin_loading_et_pwd);
		ivDelete = (ImageView) view.findViewById(R.id.popwin_loading_delete);
		ivForget = (ImageView) view.findViewById(R.id.popwin_loading_iv_forget);
		btnLogin = (Button) view.findViewById(R.id.popwin_loading_btn_loading);
		btnRegister = (Button) view.findViewById(R.id.popwin_loading_btn_register);
		
		WindowManager.LayoutParams params = LoginActivity.this.getWindow().getAttributes();
		params.alpha = 1f;
		LoginActivity.this.getWindow().setAttributes(params);
		pwLogin.setBackgroundDrawable(new ColorDrawable());
		pwLogin.setOutsideTouchable(false);
		pwLogin.showAtLocation(view, Gravity.CENTER, 0, 0);
		pwLogin.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);  
		pwLogin.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				WindowManager.LayoutParams params = LoginActivity.this.getWindow().getAttributes();
				params.alpha = 1f;
				getWindow().setAttributes(params);
				ll.setVisibility(View.VISIBLE);
			}
		});
		ivDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pwLogin.dismiss();
			}
		});
		btnRegister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showRegisterPop();
				pwLogin.dismiss();
				ll.setVisibility(View.GONE);
				dialog.show();
			}

		});
		ivForget.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showForgetPop();
				pwLogin.dismiss();
				ll.setVisibility(View.GONE);
			}
		});
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (etName.getText().toString() == null 
						|| etPwd.getText().toString() == null
							||"".equals(etName.getText().toString())
								||"".equals(etPwd.getText().toString())) {
					
					showShortToast("用户名和密码不得为空");
					return;
				}
				loginAction();
			}

		});
	}
	/**
	 * 注册弹出框
	 */
	private void showRegisterPop() {
		View view = View.inflate(LoginActivity.this,R.layout.popwin_register, null);
		pwRegister = new PopupWindow(view, screenWidth / 2,LayoutParams.WRAP_CONTENT);
		pwRegister.setFocusable(true);
		
		WindowManager.LayoutParams params = LoginActivity.this.getWindow().getAttributes();
		params.alpha = 1f;
		LoginActivity.this.getWindow().setAttributes(params);
		pwRegister.setBackgroundDrawable(new ColorDrawable());
		pwRegister.setOutsideTouchable(false);
		pwRegister.showAtLocation(view, Gravity.CENTER, 0, 0);
		pwRegister.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);  
		pwRegister.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				WindowManager.LayoutParams params = LoginActivity.this.getWindow().getAttributes();
				params.alpha = 1f;
				getWindow().setAttributes(params);
				ll.setVisibility(View.VISIBLE);
			}
		});
	}
	
	/**
	 * 忘记密码弹出框
	 */
	private void showForgetPop() {
		View view = View.inflate(LoginActivity.this,R.layout.popwin_forget_pwd, null);
		pwForget = new PopupWindow(view, screenWidth / 2,LayoutParams.WRAP_CONTENT);
		pwForget.setFocusable(true);
		
		WindowManager.LayoutParams params = LoginActivity.this.getWindow().getAttributes();
		params.alpha = 1f;
		LoginActivity.this.getWindow().setAttributes(params);
		pwForget.setBackgroundDrawable(new ColorDrawable());
		pwForget.setOutsideTouchable(false);
		pwForget.showAtLocation(view, Gravity.CENTER, 0, 0);
		pwForget.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);  
		pwForget.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				WindowManager.LayoutParams params = LoginActivity.this.getWindow().getAttributes();
				params.alpha = 1f;
				getWindow().setAttributes(params);
				ll.setVisibility(View.VISIBLE);
			}
		});
	}
	
	
	/**
	 * 登录请求
	 */
	private void loginAction() {
		post = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("mobile", etName.getText().toString());
		params.addBodyParameter("pwd", etPwd.getText().toString());
		String url = UrlUtils.postUrl+UrlUtils.path_login;
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
			}
			
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				Log.i("oye", arg0.result);
			}
		});
	}
	
}
