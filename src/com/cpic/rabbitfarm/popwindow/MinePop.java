package com.cpic.rabbitfarm.popwindow;

import java.io.File;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.cpic.rabbitfarm.R;
import com.cpic.rabbitfarm.utils.GlideRoundTransform;
import com.cpic.rabbitfarm.utils.MySeekBar;
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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MinePop {

	private PopupWindow pw;
	private PopupWindow pwTakePic;

	private int screenWidth;
	private int screenHight;
	private Activity activity;
	private ImageView ivClose;
	private String token;
	private SharedPreferences sp;

	private TextView tvLevel, tvId;
	private EditText tvName, tvFarm;
	private ImageView ivOpen, ivOff, ivSafe;
	private Button btnRecord, btnLogout;
	private MySeekBar sBar;
	private ImageView ivIcon;

	/**
	 * 调用相机的控件
	 */
	private TextView tvCamera, tvPhoto, tvCancel;
	private Intent intent;
	private Uri cameraUri;
	private File cameraPic;
	private static final int CAMERA = 0;
	private static final int PHOTO = 1;

	/**
	 * 安全中心控件
	 */
	private RadioGroup rg;
	private LinearLayout llInputId, llInputPwd,llHaveSet;
	private Button btnEnsureId;
	private EditText etName, etIdNum;

	private MediaPlayer mp;

	public MinePop(Activity activity, String token) {
		this.activity = activity;
		this.token = token;
	}

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
		tvName = (EditText) view.findViewById(R.id.popwin_userinfo_tv_name);
		tvLevel = (TextView) view.findViewById(R.id.popwin_userinfo_tv_level);
		tvFarm = (EditText) view.findViewById(R.id.popwin_userinfo_tv_farm_name);
		tvId = (TextView) view.findViewById(R.id.popwin_userinfo_tv_userid);
		ivOpen = (ImageView) view.findViewById(R.id.popwin_userinfo_ivsound);
		ivOff = (ImageView) view.findViewById(R.id.popwin_userinfo_ivnosound);
		ivSafe = (ImageView) view.findViewById(R.id.popwin_userinfo_iv_safe);
		btnRecord = (Button) view.findViewById(R.id.popwin_userinfo_btn_record);
		btnLogout = (Button) view.findViewById(R.id.popwin_userinfo_btn_logout);
		sBar = (MySeekBar) view.findViewById(R.id.popwin_userinfo_seekbar);
		ivIcon = (ImageView) view.findViewById(R.id.popwin_userinfo_iv_icon);
		if ("".equals(sp.getString("level", ""))) {
			tvLevel.setText("0级");
		} else {
			tvLevel.setText((int) Double.parseDouble(sp.getString("level", "")) + "级");
		}
		if ("".equals(sp.getString("level", ""))) {
			sBar.setProgress(0);
		} else {
			sBar.setProgress((int) Double.parseDouble(sp.getString("level", "")));
		}

		tvName.setText(sp.getString("alias_name", ""));
		tvFarm.setText(sp.getString("farm_name", ""));
		tvId.setText("账号ID： " + sp.getString("user_id", ""));
		Glide.with(activity).load(sp.getString("user_img", "")).transform(new GlideRoundTransform(activity, 80))
				.fitCenter().into(ivIcon);

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
		/**
		 * 点击头像
		 */
		ivIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showTakePic();
			}
		});

		/**
		 * 安全中心
		 */
		ivSafe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pw.dismiss();
				showSafeCenter();
			}
		});

		tvName.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (!hasFocus) {
					if ("".equals(tvName.getText().toString())) {
						Toast.makeText(activity, "请输入昵称", 0).show();
						return;
					}
					uploadIDInfo();
				}
			}
		});

		tvFarm.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (!hasFocus) {
					if ("".equals(tvFarm.getText().toString())) {
						Toast.makeText(activity, "请输入农场名称", 0).show();
						return;
					}
					uploadIDInfo();
				}
			}
		});
	}

	/**
	 * 选择照片
	 */
	public void showTakePic() {
		View view = View.inflate(activity, R.layout.popwin_select_pic, null);
		pwTakePic = new PopupWindow(view, screenWidth, LayoutParams.WRAP_CONTENT);
		pwTakePic.setFocusable(true);

		tvCamera = (TextView) view.findViewById(R.id.btn_camera);
		tvPhoto = (TextView) view.findViewById(R.id.btn_photo);
		tvCancel = (TextView) view.findViewById(R.id.btn_back);
		WindowManager.LayoutParams params = activity.getWindow().getAttributes();
		params.alpha = 0.6f;
		activity.getWindow().setAttributes(params);
		pwTakePic.setBackgroundDrawable(new ColorDrawable());
		pwTakePic.setOutsideTouchable(false);
		pwTakePic.showAtLocation(view, Gravity.CENTER, 0, 0);
		pwTakePic.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				WindowManager.LayoutParams params = activity.getWindow().getAttributes();
				params.alpha = 1f;
				activity.getWindow().setAttributes(params);
			}
		});
		tvCamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getPhotoFromCamera();
				pwTakePic.dismiss();
				pw.dismiss();
			}

		});
		tvPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getPhotoFromPic();
				pwTakePic.dismiss();
				pw.dismiss();
			}

		});
		tvCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pwTakePic.dismiss();
			}
		});
	}

	/**
	 * 安全中心代码
	 */
	public void showSafeCenter() {
		View view = View.inflate(activity, R.layout.popwin_user_safecenter, null);
		pw = new PopupWindow(view, screenWidth, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		sp = PreferenceManager.getDefaultSharedPreferences(activity);
		String is_checked = sp.getString("is_identified", "");
		String user_name = sp.getString("user_name", "");
		String identified_card = sp.getString("identified_card", "");

		llInputId = (LinearLayout) view.findViewById(R.id.popwin_user_info_safe_ll_input);
		llHaveSet = (LinearLayout) view.findViewById(R.id.popwin_user_info_safe_pwd_ll_change);
		llInputPwd = (LinearLayout) view.findViewById(R.id.popwin_user_info_safe_set_pwd_ll);
		
		btnEnsureId = (Button) view.findViewById(R.id.popwin_user_info_safe_btn_ensure_submit);
		rg = (RadioGroup) view.findViewById(R.id.popwin_user_info_safe_rg);
		etIdNum = (EditText) view.findViewById(R.id.popwin_user_info_safe_et_idnum);
		etName = (EditText) view.findViewById(R.id.popwin_user_info_safe_et_name);
		ivClose = (ImageView) view.findViewById(R.id.popwin_user_info_safe_iv_close);
		if (user_name == null || identified_card == null || "".endsWith(user_name) || "".equals(identified_card)) {
			btnEnsureId.setVisibility(View.VISIBLE);
		} else {
			btnEnsureId.setVisibility(View.GONE);
			etName.setText("姓名：" + user_name);
			etIdNum.setText("身份证号：" + identified_card);
			etName.setEnabled(false);
			etIdNum.setEnabled(false);
		}
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

		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.popwin_user_info_safe_rb_id:
					llInputId.setVisibility(View.VISIBLE);
					llInputPwd.setVisibility(View.GONE);
					break;
				case R.id.popwin_user_info_safe_rb_pwd:
					llInputId.setVisibility(View.GONE);
					llInputPwd.setVisibility(View.VISIBLE);
					break;

				default:
					break;
				}

			}
		});

		btnEnsureId.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("".equals(etName.getText().toString())) {
					Toast.makeText(activity, "姓名不得为空", 0).show();
					return;
				}
				if ("".equals(etIdNum.getText().toString())) {
					Toast.makeText(activity, "身份证号不得为空", 0).show();
					return;
				}
				uploadIDInfo();
			}

		});

		ivClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pw.dismiss();
			}
		});

	}

	private void uploadIDInfo() {
		HttpUtils post = new HttpUtils();
		RequestParams params = new RequestParams();
		final Dialog dialog = ProgressDialogHandle.getProgressDialog(activity, null);
		params.addBodyParameter("token", token);
		if (etName != null && !"".equals(etName.getText().toString())) {
			params.addBodyParameter("user_name", etName.getText().toString());
			params.addBodyParameter("identified_card", etIdNum.getText().toString());
		}
		if (tvName != null && !"".equals(tvName.getText().toString())) {
			params.addBodyParameter("alias_name", tvName.getText().toString());
		}
		if (tvFarm != null && !"".equals(tvFarm.getText().toString())) {
			params.addBodyParameter("farm_name", tvFarm.getText().toString());
		}

		String url = UrlUtils.postUrl + UrlUtils.path_modifyUserInfo;

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
				Toast.makeText(activity, "保存失败,请检查网络状况", 0).show();
				;
				if (dialog != null) {
					dialog.dismiss();
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				JSONObject obj = JSONObject.parseObject(arg0.result);
				int code = obj.getInteger("code");
				if (code == 1) {
					Toast.makeText(activity, "保存成功", 0).show();
					if (etName != null && !"".equals(etName.getText().toString())) {
						sp = PreferenceManager.getDefaultSharedPreferences(activity);
						Editor editor = sp.edit();
						editor.putString("user_name", etName.getText().toString());
						editor.putString("identified_card", etIdNum.getText().toString());
						editor.commit();
					}
					if (tvName != null && !"".equals(tvName.getText().toString())) {
						sp = PreferenceManager.getDefaultSharedPreferences(activity);
						Editor editor = sp.edit();
						editor.putString("alias_name", tvName.getText().toString());
						editor.commit();
					}
					if (tvName != null && !"".equals(tvFarm.getText().toString())) {
						sp = PreferenceManager.getDefaultSharedPreferences(activity);
						Editor editor = sp.edit();
						editor.putString("farm_name", tvFarm.getText().toString());
						editor.commit();
					}
				} else {
					Toast.makeText(activity, "保存失败，请重试", 0).show();
				}
			}
		});
	}

	/**
	 * 调用相机
	 */
	private void getPhotoFromCamera() {
		// 通过Intent调用系统相机
		intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		cameraPic = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/userIcon.jpg");
		cameraUri = Uri.fromFile(cameraPic);
		// 指定照片拍摄后的存储位置
		intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
		activity.startActivityForResult(intent, CAMERA);
	}

	private void getPhotoFromPic() {
		intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		intent.putExtra("return-data", true);
		activity.startActivityForResult(intent, PHOTO);
	}

	public void loadIcon(String path, final ImageView ivUser) {
		HttpUtils post = new HttpUtils();
		RequestParams params = new RequestParams();
		final SharedPreferences sp1 = PreferenceManager.getDefaultSharedPreferences(activity);
		final Dialog dialog = ProgressDialogHandle.getProgressDialog(activity, null);
		params.addBodyParameter("token", token);
		params.addBodyParameter("poster", new File(path));
		String url = UrlUtils.postUrl + UrlUtils.path_uploadUserImg;
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
				Toast.makeText(activity, "上传失败,请检查网络状况", 0).show();
				;
				if (dialog != null) {
					dialog.dismiss();
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				JSONObject obj = JSONObject.parseObject(arg0.result);
				int code = obj.getInteger("code");
				if (code == 1) {
					Toast.makeText(activity, "上传成功", 0).show();
					String url = obj.getString("data");
					Editor editor = sp1.edit();
					editor.putString("user_img", url);
					editor.commit();
					Glide.with(activity).load(url).transform(new GlideRoundTransform(activity, 80)).fitCenter()
							.into(ivIcon);
					Glide.with(activity).load(url).transform(new GlideRoundTransform(activity, 80)).fitCenter()
							.into(ivUser);
				} else {
					Toast.makeText(activity, "上传失败，请重试", 0).show();
				}
			}
		});
	}
}
