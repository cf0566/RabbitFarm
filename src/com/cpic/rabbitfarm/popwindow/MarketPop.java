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
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
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
	
	private ArrayList<MarketData> datas;
	private HttpUtils post;
	private RequestParams params;
	private Dialog dialog;
	private MarketAdapter adapter;

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
					Log.i("oye", list.get(i).getCount()+"-----"+list.get(i).getGoods_id());
				}
			}
		});

	}

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
			holder.tvPrice.setText(datas.get(position).getGoods_price()+"/块地");
			list.add(position, new MarketBuy(datas.get(position).getGoods_id(), 0));
			holder.tvCount.setText(list.get(position).getCount()+"");
			
			holder.ivAdd.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int count = Integer.parseInt(holder.tvCount.getText().toString());
					count ++;
					holder.tvCount.setText(count+"");
					list.set(position, new MarketBuy(datas.get(position).getGoods_id(), count));
				}
			});
			
			holder.ivDel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int count = Integer.parseInt(holder.tvCount.getText().toString());
					if (count <=0) {
						Toast.makeText(activity, "种子数量不得小于0", 0).show();
					}else{
						count --;
						holder.tvCount.setText(count+"");
						list.set(position, new MarketBuy(datas.get(position).getGoods_id(), count));
					}
				}
			});
			Glide.with(activity).load(datas.get(position).getGoods_img()).placeholder(R.drawable.zhongzibaicai).transform(new GlideRoundTransform(activity, 80))
			.fitCenter().into(holder.ivSeed);
			return convertView;
		}

		class ViewHolder {
			TextView tvName, tvCount, tvPrice;
			ImageView ivAdd, ivDel, ivSeed;
		}
	}
}
