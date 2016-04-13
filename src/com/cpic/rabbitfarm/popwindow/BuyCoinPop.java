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
import com.pingplusplus.android.Pingpp;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
     * 支付支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";
    private static final String AlIPAY = "1";

	/**
	 * 购买界面控件
	 */
	private ImageView ivDel, ivAdd;    
	private EditText etCount;
	private TextView tvSum;
	private Button btnBuy;
	private ImageView ivClose;
	
	private TextView tvType;
	private RadioGroup rgroup;
	private LinearLayout ll;
	
	private SharedPreferences sp;
	
	private TextView tvSuccessBuyCount,tvSuccessSum,tvSuccessZong;
	private Button btnLookRecord;
	private boolean isCharge = false;
	private int money;

	public BuyCoinPop(PopupWindow pw, int screenWidth, int screenHight, Activity activity, String token,boolean isCharge,int money) {
		this.pw = pw;
		this.screenWidth = screenWidth;
		this.activity = activity;
		this.token = token;
		this.screenHight = screenHight;
		this.isCharge = isCharge;
		this.money = money;
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
		etCount = (EditText) view.findViewById(R.id.popwin_buy_rabbit_coin_et_num);
		tvSum = (TextView) view.findViewById(R.id.popwin_buy_rabbit_coin_tv_sum);
		btnBuy = (Button) view.findViewById(R.id.popwin_buy_rabbit_coin_btn_ensure);
		ivClose = (ImageView) view.findViewById(R.id.popwin_buy_rabbit_coin_iv_close);
		
		ll = (LinearLayout) view.findViewById(R.id.popwin_buy_rabbit_coin_ll);
		tvType = (TextView) view.findViewById(R.id.popwin_buy_rabbit_coin_pay_type);
		rgroup = (RadioGroup) view.findViewById(R.id.popwin_buy_rabbit_coin_rg);
		
		tvSuccessBuyCount = (TextView) view.findViewById(R.id.popwin_buy_rabbit_coin_tv_sucess_count);
		tvSuccessSum = (TextView) view.findViewById(R.id.popwin_buy_rabbit_coin_tv_buy_some);
		tvSuccessZong = (TextView) view.findViewById(R.id.popwin_buy_rabbit_coin_tv_buy_zong);
		btnLookRecord = (Button) view.findViewById(R.id.popwin_buy_rabbit_coin_btn_look);
		if (!isCharge) {
			tvSuccessBuyCount.setVisibility(View.GONE);
			tvSuccessSum.setVisibility(View.GONE);
			tvSuccessZong.setVisibility(View.GONE);
			btnLookRecord.setVisibility(View.GONE);
			ll.setVisibility(View.VISIBLE);
			tvType.setVisibility(View.VISIBLE);
			rgroup.setVisibility(View.VISIBLE);
			btnBuy.setVisibility(View.VISIBLE);
			tvSum.setVisibility(View.VISIBLE);
		}else{
			tvSuccessBuyCount.setVisibility(View.VISIBLE);
			tvSuccessSum.setVisibility(View.VISIBLE);
			tvSuccessZong.setVisibility(View.VISIBLE);
			btnLookRecord.setVisibility(View.VISIBLE);
			ll.setVisibility(View.GONE);
			tvType.setVisibility(View.GONE);
			rgroup.setVisibility(View.GONE);
			btnBuy.setVisibility(View.GONE);
			tvSum.setVisibility(View.GONE);
		}
		etCount.setText(0 + "");
		tvSum.setText("合计： "+0+"元");
		
		sp = PreferenceManager.getDefaultSharedPreferences(activity);
		int zong = (int)Double.parseDouble(sp.getString("balance", "0"))+money;
		tvSuccessBuyCount.setText("共计："+ money +"个");
		tvSuccessSum.setText("您已成功购买兔币"+ money +"个");
		tvSuccessZong.setText("您的兔币总额为"+ zong +"个");
		
		
		WindowManager.LayoutParams params1 = activity.getWindow().getAttributes();
		params1.alpha = 0.6f;
		activity.getWindow().setAttributes(params1);
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
		
		btnLookRecord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pw.dismiss();
				MinePop pop = new MinePop(pw, screenWidth, screenHight, activity, token);
				pop.showRecordList();
			}
		});
		/**
		 * 监听输入款输入的数字
		 */
		etCount.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				tvSum.setText("合计： "+s.toString()+"元");
			}
		});
		

		ivDel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int count = Integer.parseInt(etCount.getText().toString());
				if (count >= 1) {
					count--;
					etCount.setText(count+"");
					tvSum.setText("合计： "+count+"元");
				}else{
					etCount.setText(0+"");
					tvSum.setText("合计： "+0+"元");
				}
			}
		});
		
		ivAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int count = Integer.parseInt(etCount.getText().toString());
				count ++;
				etCount.setText(count+"");
				tvSum.setText("合计： "+count+"元");
				tvSuccessBuyCount.setText("共计："+count+"个");
			}
		});
		
		ivClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pw.dismiss();
			}
		});
		
		btnBuy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ("0".equals(etCount.getText().toString())) {
					Toast.makeText(activity, "金币购买数量不能为0", 0).show();
					return;
				}
				post = new HttpUtils();
				params =  new RequestParams();
				dialog = ProgressDialogHandle.getProgressDialog(activity, null);
				String url = UrlUtils.postUrl+UrlUtils.path_buyCurrency;
				params.addBodyParameter("token", token);
				params.addBodyParameter("amount", etCount.getText().toString());
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
							editor.putString("count_coin", etCount.getText().toString());
							editor.commit();
							pw.dismiss();
						}else{
							Toast.makeText(activity, "获取订单信息失败"+obj.getString("msg"), 0).show();
						}
					}
				});
			}
		});
	}
	
}
