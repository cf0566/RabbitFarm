package com.cpic.rabbitfarm.popwindow;

import java.io.File;
import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.cpic.rabbitfarm.R;
import com.cpic.rabbitfarm.activity.LoginActivity;
import com.cpic.rabbitfarm.activity.MainActivity;
import com.cpic.rabbitfarm.bean.LoginUser;
import com.cpic.rabbitfarm.bean.OrderList;
import com.cpic.rabbitfarm.bean.OrderListData;
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
import com.pingplusplus.android.Pingpp;

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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
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
	
	private Button btnChangePwd;
	
	/**
	 * 设置支付密码
	 */
	private EditText etMobile,etCode,etPwd,etAgain;
	private ImageView ivSendCode;
	private Button btnEnsureChange;
	
	
	/**
	 * 订单记录的控件
	 */
	private ListView lv;
	private ArrayList<OrderListData> datas;
	private RecordAdapter adapter;
	private ImageView ivNo;
	
	/**
	 * 音乐播放器
	 */
	private MediaPlayer mp;
	private HttpUtils post;
	private RequestParams params;
	private Dialog dialog;
	
	private static final String AlIPAY = "1";
	

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
				if (!mp.isPlaying()) {
					mp = MediaPlayer.create(activity, R.raw.test);
					mp.setLooping(true);
					mp.start();
				}
			}
		});
		ivOff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mp.isPlaying()) {
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
		btnRecord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pw.dismiss();
				showRecordList();
			}

		});
		btnLogout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pw.dismiss();
				Intent intent = new Intent(activity, LoginActivity.class);
				activity.startActivity(intent);
				activity.finish();
			}
		});
	}
	
	
	/**
	 * 充值记录的弹出框
	 */
	public void showRecordList() {
		View view = View.inflate(activity, R.layout.popwin_user_record, null);
		pw = new PopupWindow(view, screenWidth, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		lv = (ListView) view.findViewById(R.id.popwin_user_info_record_lv);
		ivClose = (ImageView) view.findViewById(R.id.popwin_user_info_record_iv_close);
		ivNo =  (ImageView) view.findViewById(R.id.popwin_user_info_record_iv);
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
		
		loadList();
	}
	/**
	 * 获取列表数据源
	 */
	private void loadList() {
		HttpUtils post = new HttpUtils();
		RequestParams params = new RequestParams();
		final Dialog dialog = ProgressDialogHandle.getProgressDialog(activity, null);
		params.addBodyParameter("token", token);
		params.addBodyParameter("type", "2");

		String url = UrlUtils.postUrl + UrlUtils.path_orderList;

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
				if (dialog != null) {
					dialog.dismiss();
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				OrderList list = JSONObject.parseObject(arg0.result, OrderList.class);
				int code = list.getCode();
				if (code == 1) {
					datas = list.getData();
					if (datas.size() != 0) {
						lv.setVisibility(View.VISIBLE);
						adapter = new RecordAdapter();
						adapter.setDatas(datas);
						lv.setAdapter(adapter);
						ivNo.setVisibility(View.GONE);
					}else{
						ivNo.setVisibility(View.VISIBLE);
						lv.setVisibility(View.GONE);
					}
				}else{
					Toast.makeText(activity, "获取数据失败"+list.getMsg(), 0).show();
				}
			}
		});
	}
	
	public class RecordAdapter extends BaseAdapter{

		private ArrayList<OrderListData> datas;
		
		public void setDatas(ArrayList<OrderListData> datas){
			this.datas = datas;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return datas == null ? 0 : datas.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder ;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(activity, R.layout.item_user_info_list, null);
				holder.tvNum = (TextView) convertView.findViewById(R.id.item_user_info_list_tv_ordernum);
				holder.tvTime = (TextView) convertView.findViewById(R.id.item_user_info_list_tv_ordertime);
				holder.tvStatus = (TextView) convertView.findViewById(R.id.item_user_info_list_tv_status);
				holder.tvCount = (TextView) convertView.findViewById(R.id.item_user_info_list_tv_coin_count);
				holder.tvSum = (TextView) convertView.findViewById(R.id.item_user_info_list_tv_sum_count);
				
				holder.btnPay = (Button) convertView.findViewById(R.id.item_user_info_list_btn_pay);
				holder.btnDel = (Button) convertView.findViewById(R.id.item_user_info_list_btn_del);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.tvNum.setText("订单编号： "+datas.get(position).getOrder_id());
			holder.tvTime.setText("下单时间： "+datas.get(position).getCreate_time());
			if ("0".equals(datas.get(position).getPay_status())) {
				holder.tvStatus.setText("待支付");
				holder.btnPay.setVisibility(View.VISIBLE);
			}else if ("1".equals(datas.get(position).getPay_status())){
				holder.tvStatus.setText("交易成功");
				holder.btnPay.setVisibility(View.GONE);
			}
			holder.tvCount.setText("兔币： "+datas.get(position).getTotal());
			holder.tvSum.setText(datas.get(position).getTotal());
			
			holder.btnDel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					delOrderList(datas.get(position).getOrder_id());
				}
			});
			
			holder.btnPay.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					post = new HttpUtils();
					params =  new RequestParams();
					dialog = ProgressDialogHandle.getProgressDialog(activity, null);
					String url = UrlUtils.postUrl+UrlUtils.path_buyCurrency;
					params.addBodyParameter("token", token);
					params.addBodyParameter("amount", datas.get(position).getTotal());
					params.addBodyParameter("pay_id", AlIPAY);
					post.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

						@Override
						public void onStart() {
							super.onStart();
							if (dialog!=null) {
								dialog.show();
							}
						}
						@Override
						public void onFailure(HttpException arg0, String arg1) {
							if (dialog!=null) {
								dialog.dismiss();
							}
							Toast.makeText(activity, "获取订单信息失败，请检查网络连接", 0).show();
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							
							if (dialog!=null) {
								dialog.dismiss();
							}
							JSONObject obj = JSONObject.parseObject(arg0.result);
							int code = obj.getIntValue("code");
							if (code == 1) {
								Pingpp.createPayment(activity, obj.getString("data"));
								sp = PreferenceManager.getDefaultSharedPreferences(activity);
								Editor editor = sp.edit();
								editor.putString("count_coin", datas.get(position).getTotal());
								editor.putInt("is_mine",1);
								editor.commit();  
							}else{
								Toast.makeText(activity, "获取订单信息失败"+obj.getString("msg"), 0).show();
							}
						}
					});
				}
			});
			
			return convertView;
		}
		
		class ViewHolder{
			TextView tvNum,tvTime,tvStatus,tvCount,tvSum;
			Button btnPay,btnDel;
		}
	}
	
	
	/**
	 * 删除订单
	 */
	private void delOrderList(String orderId) {
		HttpUtils post = new HttpUtils();
		RequestParams params = new RequestParams();
		final Dialog dialog = ProgressDialogHandle.getProgressDialog(activity, null);
		params.addBodyParameter("token", token);
		params.addBodyParameter("order_id", orderId);

		String url = UrlUtils.postUrl + UrlUtils.path_orderRemove;

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
				Toast.makeText(activity, "删除失败,请检查网络状况", 0).show();
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
				int code = obj.getIntValue("code");
				if (code == 1) {
					Toast.makeText(activity, "删除成功", 0).show();
					loadList();
				}else{
					Toast.makeText(activity, "删除失败"+obj.getString("msg"), 0).show();
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
		String user_name = sp.getString("user_name", "");
		String identified_card = sp.getString("identified_card", "");
		final String set_paycode = sp.getString("set_paycode", "");

		llInputId = (LinearLayout) view.findViewById(R.id.popwin_user_info_safe_ll_input);
		llHaveSet = (LinearLayout) view.findViewById(R.id.popwin_user_info_safe_pwd_ll_change);
		llInputPwd = (LinearLayout) view.findViewById(R.id.popwin_user_info_safe_set_pwd_ll);
		
		btnChangePwd  = (Button) view.findViewById(R.id.popwin_user_info_safe_pwd_btn_change);
		btnEnsureId = (Button) view.findViewById(R.id.popwin_user_info_safe_btn_ensure_submit);
		
		/**
		 * 设置支付密码接口
		 */
		etMobile = (EditText) view.findViewById(R.id.popwin_user_info_safe_set_pwd_et_mobile);
		etCode = (EditText) view.findViewById(R.id.popwin_user_info_safe_set_pwd_et_code);
		etPwd = (EditText) view.findViewById(R.id.popwin_user_info_safe_set_pwd_et_pwd);
		etAgain = (EditText) view.findViewById(R.id.popwin_user_info_safe_set_pwd_et_again);
		ivSendCode = (ImageView) view.findViewById(R.id.popwin_user_info_safe_set_pwd_iv_sendcode);
		btnEnsureChange = (Button) view.findViewById(R.id.popwin_user_info_safe_set_pwd_btn_submit);
		
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
					llHaveSet.setVisibility(View.GONE);
					break;
				case R.id.popwin_user_info_safe_rb_pwd:
					if ("1".equals(set_paycode)) {
						llInputId.setVisibility(View.GONE);
						llHaveSet.setVisibility(View.VISIBLE);
					}else{
						llInputId.setVisibility(View.GONE);
						llInputPwd.setVisibility(View.VISIBLE);
					}
					break;

				default:
					break;
				}

			}
		});
		
		btnChangePwd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				llHaveSet.setVisibility(View.GONE);
				llInputPwd.setVisibility(View.VISIBLE);
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
		
		btnEnsureChange.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changePwdAction();
			}

		});
		ivSendCode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ("".equals(etMobile.getText().toString())) {
					Toast.makeText(activity, "手机号码不得为空", 0).show();
					return;
				}
				getMessageCode(etMobile);
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
		dialog = ProgressDialogHandle.getProgressDialog(activity, null);
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
				Toast.makeText(activity, "获取短信验证码失败，请检查网络连接", 0).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				JSONObject obj = JSON.parseObject(arg0.result);
				int code = obj.getIntValue("code");
				if (code == 1) {
					Toast.makeText(activity, "获取短信验证码成功，请注意查收短信", 0).show();
				} else {
					Toast.makeText(activity, "获取短信验证码失败，请稍后重试", 0).show();
				}
			}
		});
	}
	
	/**
	 * 修改密码
	 */
	private void changePwdAction() {
		if ("".equals(etMobile.getText().toString())) {
			Toast.makeText(activity, "手机号码不得为空", 0).show();
			return;
		}
		if ("".equals(etCode.getText().toString())) {
			Toast.makeText(activity, "验证码不得为空", 0).show();
			return;
		}
		if ("".equals(etPwd.getText().toString())) {
			Toast.makeText(activity, "密码不得为空", 0).show();
			return;
		}
		if ("".equals(etAgain.getText().toString())) {
			Toast.makeText(activity, "请再次输入密码", 0).show();
			return;
		}
		if (!(etAgain.getText().toString()).equals(etAgain.getText().toString())) {
			Toast.makeText(activity, "两侧输入的密码不一致", 0).show();
			return;
		}
		post = new HttpUtils();
		params = new RequestParams();
		dialog = ProgressDialogHandle.getProgressDialog(activity, null);
		params.addBodyParameter("token", token);
		params.addBodyParameter("mobile", etMobile.getText().toString());
		params.addBodyParameter("code", etCode.getText().toString());
		params.addBodyParameter("pwd", etPwd.getText().toString());
		String url = UrlUtils.postUrl + UrlUtils.path_setPayCode;
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
				Toast.makeText(activity, "设置失败，请检查网络连接", 0).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				JSONObject obj = JSONObject.parseObject(arg0.result);
				int code = obj.getIntValue("code");
				if (code == 1) {
					Toast.makeText(activity, "设置成功", 0).show();
					llHaveSet.setVisibility(View.VISIBLE);
					llInputPwd.setVisibility(View.GONE);
				} else if (code == 0) {
					Toast.makeText(activity, "设置失败，验证码错误", 0).show();
				} else {
					Toast.makeText(activity, "设置失败", 0).show();
				}
			}
		});
	}

	/**
	 * 修改个人信息
	 */
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
						MainActivity a = (MainActivity) activity;
						a.setUserName(tvName.getText().toString());
					}
					if (tvFarm != null && !"".equals(tvFarm.getText().toString())) {
						sp = PreferenceManager.getDefaultSharedPreferences(activity);
						Editor editor = sp.edit();
						editor.putString("farm_name", tvFarm.getText().toString());
						editor.commit();
						MainActivity a = (MainActivity) activity;
						a.setFarmName(tvFarm.getText().toString());
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
