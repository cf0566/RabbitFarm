   package com.cpic.rabbitfarm.activity;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cpic.rabbitfarm.R;
import com.cpic.rabbitfarm.base.BaseActivity;
import com.cpic.rabbitfarm.bean.LoginUser;
import com.cpic.rabbitfarm.utils.UrlUtils;
import com.cpic.rabbitfarm.view.ProgressDialogHandle;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {

	private LinearLayout ll;
	private Button btnMobile, btnQQ, btnWX;
	private PopupWindow pwLogin;
	private PopupWindow pwRegister;
	private PopupWindow pwForget;
	private int screenWidth, screenHight;
	private int index = 0;
	/**
	 * 登录界面控件
	 */
	private EditText etName;
	private EditText etPwd;
	private ImageView ivDelete, ivForget;
	private Button btnLogin;
	private Button btnRegister;
	private Dialog dialog;

	/**
	 * 注册界面控件
	 */
	private EditText etMobile;
	private EditText etCode;
	private Button btnSend;
	private Button btnEnsure;
	private CheckBox cBoxAgree;
	private EditText etSetPwd;
	private ImageView ivRegDel;

	/**
	 * 忘记密码界面控件
	 */
	private EditText etForgetMobile;
	private EditText etForgetCode;
	private Button btnForgetSend;
	private Button btnForgetEnsure;
	private EditText etSetForgetPwd;
	private EditText etSetAgain;
	private ImageView ivForDel;

	/**
	 * 加载页面控件
	 */
	private SeekBar sbar;
	private RelativeLayout relate;
	private static final int MAX_PROGRESS = 100;
	private int current_progress = 10;
	private static final int PRO = 1;
	private Handler handler;

	/**
	 * 网络请求
	 */
	private HttpUtils post;
	private RequestParams params;

	/**
	 * 用于保存用户信息
	 */
	private SharedPreferences sp;
	private SharedPreferences sharedPref;
	private Editor editor;

	/**
	 * 三方分享登录
	 */
	private UMShareAPI mShareAPI = null;
	
	
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
		 mShareAPI = UMShareAPI.get( this );
	}

	@Override
	protected void initView() {
		ll = (LinearLayout) findViewById(R.id.activity_login_layout);
		btnMobile = (Button) findViewById(R.id.activity_login_btn_mobile);
		btnQQ = (Button) findViewById(R.id.activity_login_btn_qq);
		btnWX = (Button) findViewById(R.id.activity_login_btn_wx);
		dialog = ProgressDialogHandle.getProgressDialog(LoginActivity.this, null);
		sbar = (SeekBar) findViewById(R.id.activity_login_sbar_loading);
		relate = (RelativeLayout) findViewById(R.id.activity_login_relate);
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
		btnQQ.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SHARE_MEDIA platform = null;
				 platform = SHARE_MEDIA.QQ;
				 mShareAPI.doOauthVerify(LoginActivity.this, platform, umAuthListener);
			}
		});
		btnWX.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SHARE_MEDIA platform = null;
				platform = SHARE_MEDIA.WEIXIN;
				 mShareAPI.doOauthVerify(LoginActivity.this, platform, umAuthListener);
			}
		});
		
		
	}

	private void showLoginPopupWindow() {
		View view = View.inflate(LoginActivity.this, R.layout.popwin_loading, null);
		pwLogin = new PopupWindow(view, screenWidth / 2, LayoutParams.WRAP_CONTENT);
		pwLogin.setFocusable(true);

		WindowManager.LayoutParams params = LoginActivity.this.getWindow().getAttributes();
		params.alpha = 1f;
		LoginActivity.this.getWindow().setAttributes(params);
		pwLogin.setBackgroundDrawable(new ColorDrawable());
		pwLogin.setOutsideTouchable(false);
		pwLogin.showAtLocation(view, Gravity.CENTER, 0, 0);
		pwLogin.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

		etName = (EditText) view.findViewById(R.id.popwin_loading_et_name);
		etPwd = (EditText) view.findViewById(R.id.popwin_loading_et_pwd);
		ivDelete = (ImageView) view.findViewById(R.id.popwin_loading_delete);
		ivForget = (ImageView) view.findViewById(R.id.popwin_loading_iv_forget);
		btnLogin = (Button) view.findViewById(R.id.popwin_loading_btn_loading);
		btnRegister = (Button) view.findViewById(R.id.popwin_loading_btn_register);

		sharedPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
		String name = sharedPref.getString("name", "");
		String password = sharedPref.getString("password", "");
		etName.setText(name);
		etPwd.setText(password);
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
				if (etName.getText().toString() == null || etPwd.getText().toString() == null
						|| "".equals(etName.getText().toString()) || "".equals(etPwd.getText().toString())) {

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
		View view = View.inflate(LoginActivity.this, R.layout.popwin_register, null);
		pwRegister = new PopupWindow(view, screenWidth / 2, LayoutParams.WRAP_CONTENT);
		pwRegister.setFocusable(true);

		etMobile = (EditText) view.findViewById(R.id.popwin_register_pwd_et_mobile);
		etCode = (EditText) view.findViewById(R.id.popwin_register_pwd_et_code);
		btnSend = (Button) view.findViewById(R.id.popwin_register_pwd_btn_send_code);
		btnEnsure = (Button) view.findViewById(R.id.popwin_register_pwd_btn_ensure_change);
		cBoxAgree = (CheckBox) view.findViewById(R.id.popwin_register_cbox_agree);
		etSetPwd = (EditText) view.findViewById(R.id.popwin_register_pwd_et_pwd);
		ivRegDel = (ImageView) view.findViewById(R.id.popwin_register_pwd_iv_close);

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
		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("".equals(etMobile.getText().toString()) || etMobile.getText() == null) {
					showShortToast("手机号码不得为空");
					return;
				}
				getMessageCode(etMobile);
			}
		});
		btnEnsure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("".equals(etMobile.getText().toString()) || etMobile.getText() == null) {
					showShortToast("手机号码不得为空");
					return;
				}
				if ("".equals(etCode.getText().toString()) || etCode.getText() == null) {
					showShortToast("验证码不得为空");
					return;
				}
				if ("".equals(etSetPwd.getText().toString()) || etSetPwd.getText() == null) {
					showShortToast("密码不得为空");
					return;
				}
				if (!cBoxAgree.isChecked()) {
					showShortToast("请同意《ToGame用户协议》");
					return;
				}
				registerAction();
			}
		});
		ivRegDel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pwRegister.dismiss();
			}
		});
	}

	/**
	 * 忘记密码弹出框
	 */
	private void showForgetPop() {
		View view = View.inflate(LoginActivity.this, R.layout.popwin_forget_pwd, null);
		pwForget = new PopupWindow(view, screenWidth / 2, LayoutParams.WRAP_CONTENT);
		pwForget.setFocusable(true);

		WindowManager.LayoutParams params = LoginActivity.this.getWindow().getAttributes();
		params.alpha = 1f;
		LoginActivity.this.getWindow().setAttributes(params);
		pwForget.setBackgroundDrawable(new ColorDrawable());
		pwForget.setOutsideTouchable(false);
		pwForget.showAtLocation(view, Gravity.CENTER, 0, 0);
		pwForget.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

		etForgetMobile = (EditText) view.findViewById(R.id.popwin_forget_pwd_et_mobile);
		etForgetCode = (EditText) view.findViewById(R.id.popwin_forget_pwd_et_code);
		btnForgetSend = (Button) view.findViewById(R.id.popwin_forget_pwd_btn_send_code);
		btnForgetEnsure = (Button) view.findViewById(R.id.popwin_forget_pwd_btn_ensure_change);
		etSetForgetPwd = (EditText) view.findViewById(R.id.popwin_forget_pwd_et_pwd);
		etSetAgain = (EditText) view.findViewById(R.id.popwin_forget_pwd_et_again_pwd);
		ivForDel = (ImageView) view.findViewById(R.id.popwin_forget_pwd_iv_close);

		sharedPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
		String name = sharedPref.getString("name", "");
		etForgetMobile.setText(name);

		pwForget.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				WindowManager.LayoutParams params = LoginActivity.this.getWindow().getAttributes();
				params.alpha = 1f;
				getWindow().setAttributes(params);
				ll.setVisibility(View.VISIBLE);
			}
		});
		btnForgetSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (etForgetMobile.getText() == null || "".equals(etForgetMobile.getText().toString())) {
					showShortToast("手机号码不得为空");
					return;
				}
				getMessageCode(etForgetMobile);
			}
		});
		btnForgetEnsure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("".equals(etForgetMobile.getText().toString()) || etForgetMobile.getText() == null) {
					showShortToast("手机号码不得为空");
					return;
				}
				if ("".equals(etForgetCode.getText().toString()) || etForgetCode.getText() == null) {
					showShortToast("验证码不得为空");
					return;
				}
				if ("".equals(etSetForgetPwd.getText().toString()) || etSetForgetPwd.getText() == null) {
					showShortToast("设置密码不得为空");
					return;
				}
				if ("".equals(etSetAgain.getText().toString()) || etSetAgain.getText() == null) {
					showShortToast("再次输入不得为空");
					return;
				}
				if (!etSetAgain.getText().toString().equals(etSetForgetPwd.getText().toString())) {
					showShortToast("两次输入密码不一致");
					return;
				}
				SetPwdAction();
			}
		});
		ivForDel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pwForget.dismiss();
			}
		});
	}

	/**
	 * 登录请求
	 */
	private void loginAction() {
		post = new HttpUtils();
		params = new RequestParams();
		sharedPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
		editor = sharedPref.edit();
		params.addBodyParameter("mobile", etName.getText().toString());
		params.addBodyParameter("pwd", etPwd.getText().toString());
		editor.putString("name", etName.getText().toString());
		editor.putString("password", etPwd.getText().toString());
		editor.commit();
		String url = UrlUtils.postUrl + UrlUtils.path_login;
		post.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			private LoginUser user;

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
				user = JSONObject.parseObject(arg0.result, LoginUser.class);
				int code = user.getCode();
				if (code == 1) {
					getUserInfo();
				} else {
					showShortToast(user.getMsg());
				}
			}

			private void getUserInfo() {
				editor.putString("token", user.getData().getToken());
				editor.putString("user_img", user.getData().getUser_img());
				editor.putString("farm_name", user.getData().getFarm_name());
				editor.putString("user_name", user.getData().getUser_name());
				editor.putString("level", user.getData().getLevel());
				editor.putString("balance", user.getData().getBalance());
				editor.apply();
				pwLogin.dismiss();
				ll.setVisibility(View.GONE);
				loadingPagerAction();
			}

		});
	}

	/**
	 * 获取短信验证码
	 */
	private void getMessageCode(EditText etMob) {
		post = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("mobile", etMob.getText().toString());
		String url = UrlUtils.postUrl + UrlUtils.path_getCode;
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
				showShortToast("获取短信验证码失败，请检查网络连接");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				JSONObject obj = JSON.parseObject(arg0.result);
				int code = obj.getIntValue("code");
				if (code == 1) {
					showShortToast("获取短信验证码成功，请注意查收短信");
				} else {
					showShortToast("获取短信验证码失败，请稍后重试");
				}
			}
		});
	}

	/**
	 * 注册请求
	 */
	private void registerAction() {
		post = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("mobile", etMobile.getText().toString());
		params.addBodyParameter("code", etCode.getText().toString());
		params.addBodyParameter("pwd", etSetPwd.getText().toString());
		String url = UrlUtils.postUrl + UrlUtils.path_register;
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
				showShortToast("注册失败，请检查网络连接");

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				LoginUser user = JSONObject.parseObject(arg0.result, LoginUser.class);
				int code = user.getCode();
				if (code == 1) {
					showShortToast("注册成功");
					sharedPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
					editor = sharedPref.edit();
					editor.putString("name", etMobile.getText().toString());
					editor.putString("password", etSetPwd.getText().toString());
					editor.commit();
					showLoginPopupWindow();
					pwRegister.dismiss();
					ll.setVisibility(View.GONE);
				} else if (code == 0) {
					showShortToast("注册失败，验证码错误");
				} else {
					showShortToast(user.getMsg());
				}
			}
		});
	}

	/**
	 * 忘记密码设置
	 */
	private void SetPwdAction() {
		post = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("mobile", etForgetMobile.getText().toString());
		params.addBodyParameter("pwd", etSetForgetPwd.getText().toString());
		params.addBodyParameter("code", etForgetCode.getText().toString());
		String url = UrlUtils.postUrl + UrlUtils.path_forgotPwd;
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
				showShortToast("提交失败，请检查网络连接");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				LoginUser user = JSONObject.parseObject(arg0.result, LoginUser.class);
				int code = user.getCode();
				if (code == 1) {
					showShortToast("提交成功");
					sharedPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
					editor = sharedPref.edit();
					editor.putString("name", etForgetMobile.getText().toString());
					editor.putString("password", etSetForgetPwd.getText().toString());
					editor.commit();
					showLoginPopupWindow();
					pwForget.dismiss();
					ll.setVisibility(View.GONE);
				} else if (code == 0) {
					showShortToast("提交失败，验证码错误");
				} else {
					showShortToast(user.getMsg());
				}
			}
		});
	}

	/**
	 * 加载页
	 */
	private void loadingPagerAction() {
		relate.setVisibility(View.VISIBLE);
		handler = new Handler(){  
             @Override  
            public void handleMessage(Message msg) {  
                super.handleMessage(msg);  
                  
                switch (msg.what) {  
                case PRO:  
                      if(current_progress>=MAX_PROGRESS){  
                    	Intent intent = new Intent(LoginActivity.this, MainActivity.class);
              			startActivity(intent);
              			finish();
                      }else{  
                          current_progress+=1;  
                          sbar.incrementProgressBy(1);  
                            
                          handler.sendEmptyMessageDelayed(PRO, 1);  
                      }  
                    break;  
  
                default:  
                    break;  
                }  
            }  
        };  
		 current_progress=current_progress>0?current_progress:0;  
	     sbar.setProgress(current_progress);  
	     handler.sendEmptyMessage(PRO);
	    
	}
	
	
	 /** auth callback interface**/
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText( getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText( getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };
    /** delauth callback interface**/
    private UMAuthListener umdelAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(getApplicationContext(), "delete Authorize succeed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText( getApplicationContext(), "delete Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText( getApplicationContext(), "delete Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("oye", "on activity re 2");
        mShareAPI.onActivityResult(requestCode, resultCode, data);
        Log.d("oye", "on activity re 3");
    }
	
	
}
