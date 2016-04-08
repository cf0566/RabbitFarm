package com.cpic.rabbitfarm.activity;

import java.util.ArrayList;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.cpic.rabbitfarm.R;
import com.cpic.rabbitfarm.base.BaseActivity;
import com.cpic.rabbitfarm.bean.LandList;
import com.cpic.rabbitfarm.bean.LandListInfo;
import com.cpic.rabbitfarm.bean.Seed;
import com.cpic.rabbitfarm.bean.SeedInfo;
import com.cpic.rabbitfarm.fonts.CarttonTextView;
import com.cpic.rabbitfarm.fonts.CatTextView;
import com.cpic.rabbitfarm.popwindow.CameraPop;
import com.cpic.rabbitfarm.popwindow.ChuChongPopwindow;
import com.cpic.rabbitfarm.popwindow.MessageMainPop;
import com.cpic.rabbitfarm.popwindow.ShiFeiPopwindow;
import com.cpic.rabbitfarm.utils.GlideRoundTransform;
import com.cpic.rabbitfarm.utils.RoundImageView;
import com.cpic.rabbitfarm.utils.UrlUtils;
import com.cpic.rabbitfarm.view.ProgressDialogHandle;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.socialize.utils.Log;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

	private RoundImageView ivUser;
	private long lastTime;
	private TextView tvMoney;
	private SeekBar sbLevel;
	private TextView tvLevel, tvName;
	private SharedPreferences sp;
	/**
	 * 除虫播种施肥储藏室
	 */
	private ImageView ivBozhong, ivChuchong, ivShifei, ivChucang;
	private PopupWindow pwBozhong, pwChuchong, pwShifei, pwChucang;

	/**
	 * 监控消息商城
	 */
	private ImageView ivCamera, ivMessage, ivMarket;
	private PopupWindow pwCamera, pwMessage, pwMarket;
	private int screenWidth, screenHight;

	private ArrayList<LandListInfo> landDatas;

	/**
	 * 播种控件
	 */
	private ImageView ivClose, ivBuy;
	private PopupWindow pwChooseSeed;
	private ArrayList<SeedInfo> datas;
	private ArrayList<Integer> itemCount;
	private SeedAdapter adapter;
	private Button btnEnsure;
	private ListView lvSeed;
	String goodsId = "", landsId = "";

	private HttpUtils post;
	private RequestParams params;
	private Dialog dialog;
	private String token;

	private int ChuChongCount = 0;

	/**
	 * 消息未读提示以及数量
	 */
	private ImageView ivTis;
	private int messageUnread = 0;
	private int activityUnread = 0;

	@Override
	protected void getIntentData(Bundle savedInstanceState) {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		screenHight = metrics.heightPixels;
	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void initView() {
		ivUser = (RoundImageView) findViewById(R.id.activity_main_iv_icon);
		tvMoney = (TextView) findViewById(R.id.activity_main_tv_money);
		tvName = (TextView) findViewById(R.id.activity_main_tv_user_name);
		tvLevel = (TextView) findViewById(R.id.activity_main_tv_level);
		sbLevel = (SeekBar) findViewById(R.id.activity_main_seekbar);
		ivBozhong = (ImageView) findViewById(R.id.activity_main_iv_bozhong);
		ivChuchong = (ImageView) findViewById(R.id.activity_main_iv_chuchong);
		ivShifei = (ImageView) findViewById(R.id.activity_main_iv_shifei);
		ivChucang = (ImageView) findViewById(R.id.activity_main_iv_chucang);
		ivCamera = (ImageView) findViewById(R.id.activity_main_iv_video);
		ivMessage = (ImageView) findViewById(R.id.activity_main_iv_message);
		ivMarket = (ImageView) findViewById(R.id.activity_main_iv_shop);
		ivTis = (ImageView) findViewById(R.id.activity_main_message_iv_tis);

		dialog = ProgressDialogHandle.getProgressDialog(MainActivity.this, null);
	}

	@Override
	protected void initData() {
		sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
		token = sp.getString("token", "");
		sbLevel.setEnabled(false);
		/**
		 * 获取土地状态
		 */
		loadLandList();
		
		/**
		 * 加载种子
		 */
		loadSeeds();
		
		/**
		 * 加载个人信息
		 */
		loadDatas();
		/**
		 * 获取Message的未读消息
		 */
		loadUnreadMsg();

	}

	private void loadDatas() {
		sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
		String user_img = sp.getString("user_img", "");
		String farm_name = sp.getString("farm_name", "");
		String user_name = sp.getString("user_name", "");
		String level = sp.getString("level", "");
		String balance = sp.getString("balance", "");
		tvName.setText(user_name);
		if ("".equals(level)) {
			level = "0";
		}
		tvMoney.setText((int) Double.parseDouble(balance) + "");
		tvLevel.setText((int) Double.parseDouble(level) + "级");
		sbLevel.setProgress((int) Double.parseDouble(level));
		Glide.with(MainActivity.this).load(user_img).transform(new GlideRoundTransform(MainActivity.this, 80))
				.fitCenter().into(ivUser);

	}

	@Override
	protected void registerListener() {
		ivBozhong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (datas.size() == 0) {
					showNoSeedsPop();
				} else {
					
					showHaveseedPopup();
					
					adapter = new SeedAdapter();
					adapter.setDatas(datas);
					lvSeed.setAdapter(adapter);
				}
			}
		});

		ivChuchong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadSeeds();
				ChuChongPopwindow pop = new ChuChongPopwindow(pwChuchong, screenWidth, MainActivity.this, ChuChongCount,token);
				if (ChuChongCount == 0) {
					pop.showNoBanPop();
				} else {
					pop.showChooseBanPop();
				}
			}
		});
		ivShifei.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ShiFeiPopwindow pop = new ShiFeiPopwindow(pwShifei, screenWidth, MainActivity.this, token);
				pop.showGiveFeiPop();
			}
		});

		/**
		 * 摄像头点击事件
		 */
		ivCamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CameraPop pop = new CameraPop(pwCamera, screenWidth, MainActivity.this, token);
				pop.showLookCameraPop();
			}
		});
		/**
		 * 消息点击事件
		 */
		ivMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MessageMainPop pop = new MessageMainPop(pwMessage, screenWidth, MainActivity.this, token,
						activityUnread, messageUnread);
				pop.showMessageMainPop();
			}
		});
	}

	@Override
	public void onBackPressed() {
		// 获取本次点击的时间
		long currentTime = System.currentTimeMillis();
		long dTime = currentTime - lastTime;
		if (dTime < 2000) {
			finish();
		} else {
			showShortToast("再按一次退出程序");
			lastTime = currentTime;
		}
	}

	private void loadUnreadMsg() {
		post = new HttpUtils();
		params = new RequestParams();
		String url = UrlUtils.postUrl + UrlUtils.path_getMessageCount;
		params.addBodyParameter("token", token);
		post.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				showShortToast("获取未读消息失败，请检查网络连接");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				JSONObject obj = JSONObject.parseObject(arg0.result);
				int code = obj.getIntValue("code");
				if (code == 1) {
					JSONObject data = obj.getJSONObject("data");
					messageUnread = data.getIntValue("message");
					activityUnread = data.getIntValue("activity");
					if (messageUnread != 0 || activityUnread != 0) {
						ivTis.setVisibility(View.VISIBLE);
					} else {
						ivTis.setVisibility(View.INVISIBLE);
					}
				} else {
					showShortToast("获取未读消息失败");
				}
			}
		});
	}

	/**
	 * 获取土地的状态
	 */
	private void loadLandList() {
		post = new HttpUtils();
		params = new RequestParams();
		String url = UrlUtils.postUrl + UrlUtils.path_landList;
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
				if (dialog != null) {
					dialog.dismiss();
				}
				showShortToast("获取土地状态失败，请检查网络连接");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				LandList land = JSONObject.parseObject(arg0.result, LandList.class);
				int code = land.getCode();
				if (code == 1) {
					landDatas = land.getData();
				} else {
					showShortToast("获取土地状态失败");
				}
			}
		});
	}
	
	
	
	
	
	
	
	
	
	
	

	/*********************************************************************************************
	 * 以下是播种弹出框，由于第一次做功能性弹出框，将第一类弹出框写在了主界面里进行测试，之后的功能模块封装成类放在popwin文件夹下
	 */

	/**
	 * 播种没有种子弹出框
	 */
	private void showNoSeedsPop() {
		View view = View.inflate(MainActivity.this, R.layout.popwin_noseed, null);
		pwBozhong = new PopupWindow(view, screenWidth / 2, LayoutParams.WRAP_CONTENT);
		pwBozhong.setFocusable(true);
		ivClose = (ImageView) view.findViewById(R.id.popwin_noseed_iv_close);
		ivBuy = (ImageView) view.findViewById(R.id.popwin_noseed_iv_buy);

		WindowManager.LayoutParams params = MainActivity.this.getWindow().getAttributes();
		params.alpha = 1f;
		MainActivity.this.getWindow().setAttributes(params);
		pwBozhong.setBackgroundDrawable(new ColorDrawable());
		pwBozhong.setOutsideTouchable(false);
		pwBozhong.showAtLocation(view, Gravity.CENTER, 0, 0);
		pwBozhong.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

		pwBozhong.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				WindowManager.LayoutParams params = MainActivity.this.getWindow().getAttributes();
				params.alpha = 1f;
				getWindow().setAttributes(params);
			}
		});

		ivClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pwBozhong.dismiss();
			}
		});
	}

	/**
	 * 确认播种弹出框
	 */
	private void showHaveseedPopup() {
		View view = View.inflate(MainActivity.this, R.layout.popwin_bozhong, null);
		pwChooseSeed = new PopupWindow(view, screenWidth / 2, LayoutParams.WRAP_CONTENT);
		pwChooseSeed.setFocusable(true);
		lvSeed = (ListView) view.findViewById(R.id.popwin_bozhong_lv);
		btnEnsure = (Button) view.findViewById(R.id.popwin_bozhong_btn_ensure);
		ivClose = (ImageView) view.findViewById(R.id.popwin_bozhong_iv_close);

		WindowManager.LayoutParams params = MainActivity.this.getWindow().getAttributes();
		params.alpha = 1f;
		MainActivity.this.getWindow().setAttributes(params);
		pwChooseSeed.setBackgroundDrawable(new ColorDrawable());
		pwChooseSeed.setOutsideTouchable(false);
		pwChooseSeed.showAtLocation(view, Gravity.CENTER, 0, 0);
		pwChooseSeed.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

		pwChooseSeed.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				WindowManager.LayoutParams params = MainActivity.this.getWindow().getAttributes();
				params.alpha = 1f;
				getWindow().setAttributes(params);
			}
		});
		ivClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pwChooseSeed.dismiss();
			}
		});
		btnEnsure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pwChooseSeed.dismiss();
				int count = 0;
				if (itemCount.size() != 0) {
					for (int i = 0; i < itemCount.size() - 1; i++) {
						goodsId += datas.get(i).getGoods_id() + ",";
						landsId += itemCount.get(i).toString() + ",";
					}
					goodsId += datas.get(itemCount.size() - 1).getGoods_id();
					landsId += itemCount.get(itemCount.size() - 1);
					for (int j = 0; j < itemCount.size(); j++) {
						count += itemCount.get(j);
					}
				}
				if (count == 0) {
					showFaluirePop();
				} else {
					if (6 - landDatas.size() < count) {
						showFaluirePop();
					} else {
						plantSeeds(goodsId, landsId);
					}
				}
			}
		});

	}

	/**
	 * 确认播种
	 */
	private void plantSeeds(String goodsId, String landsId) {
		post = new HttpUtils();
		params = new RequestParams();
		String url = UrlUtils.postUrl + UrlUtils.path_plant;
		params.addBodyParameter("token", token);
		params.addBodyParameter("goods", goodsId);
		params.addBodyParameter("land", landsId);
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
				JSONObject obj = JSONObject.parseObject(arg0.result);
				int code = obj.getIntValue("code");
				if (code == 1) {
					pwChooseSeed.dismiss();
					showSuccessPop();
				}
			}
		});
	}

	/**
	 * 播种失败弹出框
	 */
	private void showFaluirePop() {
		View view = View.inflate(MainActivity.this, R.layout.popwin_seed_faliure, null);
		pwChooseSeed = new PopupWindow(view, screenWidth / 2, LayoutParams.WRAP_CONTENT);
		pwChooseSeed.setFocusable(true);
		ImageView ivBack = (ImageView) view.findViewById(R.id.popwin_seed_faliure_iv_back);
		ivClose = (ImageView) view.findViewById(R.id.popwin_seed_faliure_iv_close);
		WindowManager.LayoutParams params = MainActivity.this.getWindow().getAttributes();
		params.alpha = 1f;
		MainActivity.this.getWindow().setAttributes(params);
		pwChooseSeed.setBackgroundDrawable(new ColorDrawable());
		pwChooseSeed.setOutsideTouchable(false);
		pwChooseSeed.showAtLocation(view, Gravity.CENTER, 0, 0);
		pwChooseSeed.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		pwChooseSeed.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				WindowManager.LayoutParams params = MainActivity.this.getWindow().getAttributes();
				params.alpha = 1f;
				getWindow().setAttributes(params);
			}
		});
		ivClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pwChooseSeed.dismiss();
			}
		});
		ivBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pwChooseSeed.dismiss();
				showHaveseedPopup();
			}
		});
	}

	/**
	 * 播种成功弹出框
	 */
	private void showSuccessPop() {
		View view = View.inflate(MainActivity.this, R.layout.popwin_seed_success, null);
		pwChooseSeed = new PopupWindow(view, screenWidth / 2, LayoutParams.WRAP_CONTENT);
		pwChooseSeed.setFocusable(true);
		ivClose = (ImageView) view.findViewById(R.id.popwin_seed_success_iv_close);
		WindowManager.LayoutParams params = MainActivity.this.getWindow().getAttributes();
		params.alpha = 1f;
		MainActivity.this.getWindow().setAttributes(params);
		pwChooseSeed.setBackgroundDrawable(new ColorDrawable());
		pwChooseSeed.setOutsideTouchable(false);
		pwChooseSeed.showAtLocation(view, Gravity.CENTER, 0, 0);
		pwChooseSeed.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		pwChooseSeed.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				WindowManager.LayoutParams params = MainActivity.this.getWindow().getAttributes();
				params.alpha = 1f;
				getWindow().setAttributes(params);
			}
		});
		ivClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pwChooseSeed.dismiss();
			}
		});
	}

	private void loadSeeds() {
		post = new HttpUtils();
		params = new RequestParams();
		String url = UrlUtils.postUrl + UrlUtils.path_storeroomList;
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
				}
				showShortToast("无法获取数据，请检查网络连接");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				if (dialog != null) {
					dialog.dismiss();
				}
				Seed obj = JSONObject.parseObject(arg0.result, Seed.class);
				int code = obj.getCode();
				if (code == 1) {
					datas = JSONObject.parseObject(arg0.result, Seed.class).getData();
					itemCount = new ArrayList<Integer>();
					
					for (int i = 0; i < datas.size(); i++) {

						if ("2".equals(datas.get(i).getStore_type())) {
							ChuChongCount = Integer.parseInt(datas.get(i).getGoods_number());
						}
						if (datas.size() != 0 && !"1".equals(datas.get(i).getStore_type())) {
							datas.remove(i);
						}
					}

					for (int j = 0; j < datas.size(); j++) {
						itemCount.add(0);
					}

				} else {
					showShortToast("无法获取数据" + obj.getMsg());
				}
			}
		});
	}

	public class SeedAdapter extends BaseAdapter {

		private ArrayList<SeedInfo> datas;

		public void setDatas(ArrayList<SeedInfo> datas) {
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
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			final ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(MainActivity.this, R.layout.item_popwin_bozhong_list, null);
				holder = new ViewHolder();
				holder.tvCount = (CatTextView) convertView.findViewById(R.id.item_popwin_bozhong_tv_count);
				holder.tvSeeds = (CarttonTextView) convertView.findViewById(R.id.item_popwin_bozhong_tv_seed);
				holder.ivAdd = (ImageView) convertView.findViewById(R.id.item_popwin_bozhong_iv_add);
				holder.ivDel = (ImageView) convertView.findViewById(R.id.item_popwin_bozhong_iv_del);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tvSeeds.setText(datas.get(position).getGoods_name() + ":");
			holder.tvCount.setText(itemCount.get(position) + "");
			holder.ivAdd.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int count = Integer.parseInt(holder.tvCount.getText().toString());
					if (count > 5) {
						showShortToast("已超出土地范围");
					} else {
						count++;
					}
					holder.tvCount.setText(count + "");
					itemCount.set(position, count);
				}
			});

			holder.ivDel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int count = Integer.parseInt(holder.tvCount.getText().toString());

					if (count < 1) {
						showShortToast("土地范围不得小于0");
					} else {
						count--;
					}
					holder.tvCount.setText(count + "");
					itemCount.set(position, count);
				}
			});
			return convertView;
		}

		class ViewHolder {
			CatTextView tvCount;
			CarttonTextView tvSeeds;
			ImageView ivAdd, ivDel;
		}
	}
}
