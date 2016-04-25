package com.cpic.rabbitfarm.popwindow;

import java.util.ArrayList;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.cpic.rabbitfarm.R;
import com.cpic.rabbitfarm.bean.Market;
import com.cpic.rabbitfarm.bean.MarketBuy;
import com.cpic.rabbitfarm.bean.MarketData;
import com.cpic.rabbitfarm.utils.GlideRoundTransform;
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
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

public class MarketPop {

	private PopupWindow pw;
	private int screenWidth;
	private int screenHight;
	private Activity activity;
	private String token;
	private ImageView ivClose;
	private GridView gv;
	private Button btnEnsure;
	private TextView tvSum;

	private ArrayList<MarketData> datas;
	private HttpUtils post;
	private RequestParams params;
	private Dialog dialog;
	private MarketAdapter adapter;
	private int sum = 0;
	
	private LinearLayout llUnPay,llPaySuccess,llLess;

	private SharedPreferences sp;
	
	private final static int UNPAY = 0;
	private final static int PAYSUCCESS = 1;
	private final static int LESS_MONEY = 2;
	/**
	 * 确认订单的pop
	 */
	private GridView gvBuy;
	private TextView tvCount, tvMoney, tvTis;
	private Button btnMath;
	private ImageView ivTitle;
	private TextView tvPaySuccess;
	private ImageView ivChucang;
	private Button btnPay;
	
	private  ArrayList<MarketBuy> myList ;
	
	
	/**
	 * 输入密码Pop
	 */
	private EditText etPwd;
	private Button btnSet;

	private ArrayList<MarketBuy> list = new ArrayList<MarketBuy>();
	

	public MarketPop(PopupWindow pw, int screenWidth, int screenHight, Activity activity, String token) {
		this.pw = pw;
		this.screenWidth = screenWidth;
		this.activity = activity;
		this.token = token;
		this.screenHight = screenHight;
	}

	public void showMarketMainPop() {
		View view = View.inflate(activity, R.layout.popwin_market, null);
		pw = new PopupWindow(view, screenWidth, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		WindowManager.LayoutParams params1 = activity.getWindow().getAttributes();
		params1.alpha = 0.6f;
		activity.getWindow().setAttributes(params1);
		gv = (GridView) view.findViewById(R.id.popwin_market_gv);
		ivClose = (ImageView) view.findViewById(R.id.popwin_market_iv_close);
		btnEnsure = (Button) view.findViewById(R.id.popwin_market_btn_ensure);
		tvSum = (TextView) view.findViewById(R.id.popwin_market_tv_count);
		
		tvSum.setText(sum + "");
		loadList();
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
		
		btnEnsure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 0; i < list.size(); i++) {
					if (Integer.parseInt(tvSum.getText().toString()) == 0) {
						Toast.makeText(activity, "请选择至少一种种子", 0).show();
					} else {
						pw.dismiss();
						myList = new ArrayList<MarketBuy>();
						for (int j = 0; j < list.size(); j++) {
							if (list.get(j).getCount() != 0) {
								myList.add(list.get(j));
							}
						}
						showMarketEnsurePop(tvSum.getText().toString(),UNPAY,myList);
					}
				}
			}
		});
	}

	public void showMarketEnsurePop(final String sumCoin,int type,final ArrayList<MarketBuy> myList) {
		View view = View.inflate(activity, R.layout.popwin_market_ensure, null);
		pw = new PopupWindow(view, screenWidth, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
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
		
		ivClose = (ImageView) view.findViewById(R.id.popwin_market_ensure_iv_close);

		gvBuy = (GridView) view.findViewById(R.id.popwin_market_ensure_gv);
		tvCount = (TextView) view.findViewById(R.id.popwin_market_ensure_tv_shanping);
		tvMoney = (TextView) view.findViewById(R.id.popwin_market_ensure_tv_money);
		tvTis = (TextView) view.findViewById(R.id.popwin_market_ensure_tv_tis);
		btnMath = (Button) view.findViewById(R.id.popwin_market_ensure_btn_math);
		tvPaySuccess = (TextView) view.findViewById(R.id.popwin_market_ensure_tv_pay_success);
		ivTitle = (ImageView) view.findViewById(R.id.popwin_market_ensure_iv_title);
		btnPay = (Button) view.findViewById(R.id.popwin_market_ensure_btn_add);
		
		llUnPay = (LinearLayout) view.findViewById(R.id.popwin_market_ll_unpay);
		llPaySuccess = (LinearLayout) view.findViewById(R.id.popwin_market_ll_pay_success);
		llLess =  (LinearLayout) view.findViewById(R.id.popwin_market_ll_less);
		ivChucang = (ImageView) view.findViewById(R.id.popwin_market_ensure_iv_chucang);
		tvCount.setText(myList.size() + "");
		tvMoney.setText(sumCoin);
	
		EnsureAdapter adapter = new EnsureAdapter();
		adapter.setDatas(myList);
		gvBuy.setAdapter(adapter);
		if (type == 0) {
			sp = PreferenceManager.getDefaultSharedPreferences(activity);
			String coin = sp.getString("balance", "0");
			int coinnum = (int) Double.parseDouble(coin);
			int last = (int) Double.parseDouble(coin) - (int)Double.parseDouble(sumCoin);
			if (last >= 0) {
				llUnPay.setVisibility(View.VISIBLE);
				llPaySuccess.setVisibility(View.GONE);
				llLess.setVisibility(View.GONE);
				ivTitle.setImageResource(R.drawable.gm_querendd);
				tvTis.setText("您当前拥有的兔币数为" + coinnum + "个，支付成功以后还有" + last + "个");
			}else{
				llUnPay.setVisibility(View.GONE);
				llPaySuccess.setVisibility(View.GONE);
				llLess.setVisibility(View.VISIBLE);
				ivTitle.setImageResource(R.drawable.gm_querendd);
			}
		}else if (type == 1) {
			llUnPay.setVisibility(View.GONE);
			llPaySuccess.setVisibility(View.VISIBLE);
			tvPaySuccess.setText("您已成功支付"+sumCoin+"兔币");
			ivTitle.setImageResource(R.drawable.gm_zfcg);
			ivChucang.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					pw.dismiss();
					StoreRoomPop pop = new StoreRoomPop(pw, screenWidth, screenHight, activity, token);
					pop.showStoreMainPop();
				}
			});
		}
		
  		ivClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pw.dismiss();
			}
		});

		btnMath.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pw.dismiss();
				ensureOrder(myList,sumCoin);
			}

		});
		btnPay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BuyCoinPop pop = new BuyCoinPop(pw, screenWidth, screenHight, activity, token, false, 0);
				pw.dismiss();
				pop.showBuyCoinPop();
			}
		});
		
	}
	/**
	 * 确认订单结算
	 */
	private void ensureOrder(final ArrayList<MarketBuy> myList,final String sumCoin) {
		post = new HttpUtils();
		params = new RequestParams();
		dialog = ProgressDialogHandle.getProgressDialog(activity, null);
		String url = UrlUtils.postUrl + UrlUtils.path_buyGoods;
		params.addBodyParameter("token", token);
		String good_id = "";
		String good_count = "";
		for (int i = 0; i < myList.size()-1 ; i++) {
			good_id += myList.get(i).getGoods_id()+",";
			good_count += myList.get(i).getCount()+",";
		}
		good_id += myList.get(myList.size()-1).getGoods_id();
		good_count += myList.get(myList.size()-1).getCount();

		params.addBodyParameter("goods_id", good_id);
		params.addBodyParameter("goods_number", good_count);
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
				// TODO Auto-generated method stub
				if (dialog != null) {
					dialog.dismiss();
				}
				Toast.makeText(activity, "确认结算失败,请检查网络连接", 0).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				JSONObject obj = JSONObject.parseObject(arg0.result);
				int code = obj.getIntValue("code");
				if (code == 1) {
					String data = obj.getString("data");
					showInputPwdPop(data,sumCoin,myList);
				}
			}
		});

	}
	/**
	 * 输入密码界面
	 */
	public void showInputPwdPop(final String data,final String sumCoin,final ArrayList<MarketBuy> myList) {
		View view = View.inflate(activity, R.layout.popwin_market_setpwd, null);
		pw = new PopupWindow(view, screenWidth, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		WindowManager.LayoutParams params1 = activity.getWindow().getAttributes();
		params1.alpha = 0.6f;
		activity.getWindow().setAttributes(params1);
		ivClose = (ImageView) view.findViewById(R.id.popwin_market_setpwd_iv_close);
		etPwd = (EditText) view.findViewById(R.id.popwin_market_setpwd_et_pwd);
		btnSet = (Button) view.findViewById(R.id.popwin_market_setpwd_btn_pay);
		
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
		btnSet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ("".equals(etPwd.getText().toString())) {
					Toast.makeText(activity, "密码不得为空", 0).show();
				}else{
					inputPwd(data,sumCoin,myList);
				}
			}

		});
	}
	/**
	 * 输入密码请求
	 */
	private void inputPwd(String data,final String sumCoin,final ArrayList<MarketBuy> myList) {
		post = new HttpUtils();
		params = new RequestParams();
		dialog = ProgressDialogHandle.getProgressDialog(activity, null);
		String url = UrlUtils.postUrl + UrlUtils.path_payGoods;
		params.addBodyParameter("token", token);
		params.addBodyParameter("order_id", data);
		params.addBodyParameter("pwd", etPwd.getText().toString());
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
				Toast.makeText(activity, "确认支付失败,请检查网络连接", 0).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				JSONObject obj = JSONObject.parseObject(arg0.result);
				int code = obj.getIntValue("code");
				if (code == 1) {
					Toast.makeText(activity, "支付成功", 0).show();
					pw.dismiss();
					showMarketEnsurePop(sumCoin, 1,myList);
				}else if (code == 0) {
					Toast.makeText(activity, "支付失败，密码错误", 0).show();
				}else{
					Toast.makeText(activity, "支付失败", 0).show();
				}
			}
		});
	}
	
	/**
	 * 下载商城数据
	 */
	public void loadList() {
		post = new HttpUtils();
		params = new RequestParams();
		dialog = ProgressDialogHandle.getProgressDialog(activity, null);
		String url = UrlUtils.postUrl + UrlUtils.path_goodsList;
		params.addBodyParameter("token", token);
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
				// TODO Auto-generated method stub
				if (dialog != null) {
					dialog.dismiss();
					Toast.makeText(activity, "获取商城数据失败,请检查网络连接", 0).show();
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				if (dialog != null) {
					dialog.dismiss();
				}
				Market obj = JSONObject.parseObject(arg0.result, Market.class);
				int code = obj.getCode();
				if (code == 1) {
					datas = JSONObject.parseObject(arg0.result, Market.class).getData();
					if (datas.size() != 0) {
						for (int i = 0; i < datas.size(); i++) {
							list.add(i,new MarketBuy(datas.get(i).getGoods_id(), 0,
											(int) Double.parseDouble(datas.get(i).getGoods_price()),
											datas.get(i).getGoods_name()));
						}
						adapter = new MarketAdapter();
						adapter.setDatas(datas);
						gv.setAdapter(adapter);
					}
				} else {
					Toast.makeText(activity, "获取商城数据失败,请重新登录", 0).show();
				}
			}
		});
	}
	
	/**
	 * 确认订单的适配器
	 * @author Taylor
	 *
	 */
	public class EnsureAdapter extends BaseAdapter {

		private ArrayList<MarketBuy> list;

		public void setDatas(ArrayList<MarketBuy> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(activity, R.layout.item_market_ensure_list, null);
				holder.tvCount = (TextView) convertView.findViewById(R.id.item_market_ensure_tv_count);
				holder.tvPrice = (TextView) convertView.findViewById(R.id.item_market_ensure_tv_price);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvPrice.setText(list.get(position).getPrice() + "/块地");
			holder.tvCount.setText(list.get(position).getGoods_name() + ":" + list.get(position).getCount());

			return convertView;
		}

		class ViewHolder {
			TextView tvCount, tvPrice;
		}
	}
	/**
	 * 商品列表的适配器
	 * @author Taylor
	 *
	 */
	public class MarketAdapter extends BaseAdapter {

		private ArrayList<MarketData> datas;

		public void setDatas(ArrayList<MarketData> datas) {
			this.datas = datas;
		}

		@Override
		public int getCount() {
			return datas == null ? 0 : datas.size();
		}

		@Override
		public Object getItem(int position) {
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(activity, R.layout.item_market_list, null);
				holder.tvName = (TextView) convertView.findViewById(R.id.item_market_tv_seeds);
				holder.tvCount = (TextView) convertView.findViewById(R.id.item_market_tv_count);
				holder.tvPrice = (TextView) convertView.findViewById(R.id.item_market_tv_price);
				holder.ivAdd = (ImageView) convertView.findViewById(R.id.item_market_iv_add);
				holder.ivDel = (ImageView) convertView.findViewById(R.id.item_market_iv_del);
				holder.ivSeed = (ImageView) convertView.findViewById(R.id.item_market_iv_seeds);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvName.setText(datas.get(position).getGoods_name());
			holder.tvPrice.setText(datas.get(position).getGoods_price() + "/块地");

			holder.tvCount.setText(list.get(position).getCount() + "");

			holder.ivAdd.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int count = Integer.parseInt(holder.tvCount.getText().toString());
					count++;
					holder.tvCount.setText(count + "");
					list.set(position,
							new MarketBuy(datas.get(position).getGoods_id(), count,
									(int) Double.parseDouble(datas.get(position).getGoods_price()),
									datas.get(position).getGoods_name()));
					sum = 0;
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).getCount() != 0) {
							sum += list.get(i).getCount() * list.get(i).getPrice();
						}
					}
					tvSum.setText(sum + "");
				}
			});

			holder.ivDel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int count = Integer.parseInt(holder.tvCount.getText().toString());
					if (count <= 0) {
						Toast.makeText(activity, "种子数量不得小于0", 0).show();
					} else {
						count--;
						holder.tvCount.setText(count + "");
						list.set(position,
								new MarketBuy(datas.get(position).getGoods_id(), count,
										(int) Double.parseDouble(datas.get(position).getGoods_price()),
										datas.get(position).getGoods_name()));
						sum = 0;
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).getCount() != 0) {
								sum += list.get(i).getCount() * list.get(i).getPrice();
							}
						}
						tvSum.setText(sum + "");
					}
				}
			});
			Glide.with(activity).load(datas.get(position).getGoods_img()).placeholder(R.drawable.zhongzibaicai)
					.transform(new GlideRoundTransform(activity, 80)).fitCenter().into(holder.ivSeed);
			return convertView;
		}

		class ViewHolder {
			TextView tvName, tvCount, tvPrice;
			ImageView ivAdd, ivDel, ivSeed;
		}
	}
}
