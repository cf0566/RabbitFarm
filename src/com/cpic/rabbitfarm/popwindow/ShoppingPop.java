package com.cpic.rabbitfarm.popwindow;

import java.util.ArrayList;

import com.alibaba.fastjson.JSONObject;
import com.cpic.rabbitfarm.R;
import com.cpic.rabbitfarm.bean.MarketBuy;
import com.cpic.rabbitfarm.bean.ShopGoods;
import com.cpic.rabbitfarm.bean.ShopGoodsData;
import com.cpic.rabbitfarm.bean.ShopGoodsInfo;
import com.cpic.rabbitfarm.popwindow.MarketPop.EnsureAdapter.ViewHolder;
import com.cpic.rabbitfarm.utils.UrlUtils;
import com.cpic.rabbitfarm.view.MyGridView;
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
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

public class ShoppingPop {

	private PopupWindow pw;
	private int screenWidth;
	private int screenHight;
	private Activity activity;
	private String token;
	private ImageView ivClose;
	private ListView lv;
	
	private HttpUtils post;
	private RequestParams params;
	private Dialog dialog;
	private ArrayList<ShopGoodsData> datas;
	
	private ShopAdapter adapter;
	
	public ShoppingPop(PopupWindow pw, int screenWidth, int screenHight, Activity activity, String token) {
		this.pw = pw;
		this.screenWidth = screenWidth;
		this.activity = activity;
		this.token = token;
		this.screenHight = screenHight;
	}
	
	
	public void showShoppingMainPop() {
		View view = View.inflate(activity, R.layout.popwin_shopping_goods, null);
		pw = new PopupWindow(view, screenWidth, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		WindowManager.LayoutParams params1 = activity.getWindow().getAttributes();
		params1.alpha = 0.6f;
		activity.getWindow().setAttributes(params1);
		ivClose = (ImageView) view.findViewById(R.id.popwin_shopping_goods_iv_close);
		lv = (ListView) view.findViewById(R.id.popwin_shopping_goods_lv);
		
		pw.setBackgroundDrawable(new ColorDrawable());
		pw.setOutsideTouchable(false);
		pw.showAtLocation(view, Gravity.CENTER, 0, 0);
		loadList();
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
	 * 我的购买列表
	 */
	private void loadList() {
		post  = new HttpUtils();
		params = new RequestParams();
		String url = UrlUtils.postUrl+UrlUtils.path_orderList;
		dialog = ProgressDialogHandle.getProgressDialog(activity, null);
		params.addBodyParameter("token", token);
		params.addBodyParameter("type", 1+"");
		post.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				if (dialog!=null) {
					dialog.show();
				}
				super.onStart();
			}
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				if (dialog!=null) {
					dialog.dismiss();
				}
				Toast.makeText(activity, "获取商品订单失败，请检查网络连接", 0).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog!=null) {
					dialog.dismiss();
				}
				ShopGoods good = JSONObject.parseObject(arg0.result, ShopGoods.class);
				int code = good.getCode();
				if (code == 1) {
					datas = good.getData();
					adapter = new ShopAdapter();
					adapter.setDatas(datas);
					lv.setAdapter(adapter);
				}else{
					Toast.makeText(activity, "获取商品订单失败", 0).show();
				}
			}
		});
	}

	/**
	 * 我的购买列表适配器
	 * @author Taylor
	 */
	public class ShopAdapter extends BaseAdapter {

		private ArrayList<ShopGoodsData> datas;

		public void setDatas(ArrayList<ShopGoodsData> datas) {
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
				convertView = View.inflate(activity, R.layout.item_shop_list, null);
				holder.tvOrdernum = (TextView) convertView.findViewById(R.id.item_shop_list_tv_ordernum);
				holder.tvType = (TextView) convertView.findViewById(R.id.item_shop_list_tv_ordertype);
				holder.tvTime = (TextView) convertView.findViewById(R.id.item_shop_list_tv_ordertime);
				holder.tvCount = (TextView) convertView.findViewById(R.id.item_shop_list_tv_count);
				holder.tvMoney = (TextView) convertView.findViewById(R.id.item_shop_list_tv_money);
				holder.btnPay = (Button) convertView.findViewById(R.id.item_shop_list_btn_pay);
				holder.btnDel = (Button) convertView.findViewById(R.id.item_shop_list_btn_del);
				holder.gv = (MyGridView) convertView.findViewById(R.id.item_shop_list_gv);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvOrdernum.setText("订单编号： "+datas.get(position).getOrder_id());
			holder.tvTime.setText("下单时间： "+datas.get(position).getCreate_time());
			holder.tvCount.setText(datas.get(position).getTotal());
			holder.tvMoney.setText(datas.get(position).getOrder_amount());
			if ("0".equals(datas.get(position).getPay_status())) {
				holder.tvType.setText("待支付");
				holder.btnPay.setVisibility(View.VISIBLE);
			}else if ("1".equals(datas.get(position).getPay_status())) {
				holder.tvType.setText("交易成功");
				holder.btnPay.setVisibility(View.GONE);
			}
			GridAdapter adapter = new GridAdapter();
			adapter.setDatas(datas.get(position).getGoods());
			holder.gv.setAdapter(adapter);
			
			holder.btnPay.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					pw.dismiss();
					ArrayList<MarketBuy> list = new ArrayList<MarketBuy>();
					for (int i = 0; i < datas.get(position).getGoods().size(); i++) {
						list.add(new MarketBuy(datas.get(position).getGoods().get(i).getGoods_id(), Integer.parseInt(datas.get(position).getGoods().get(i).getGoods_number()),
								(int)Double.parseDouble(datas.get(position).getGoods().get(i).getGoods_price()), datas.get(position).getGoods().get(i).getGoods_name()));
					}
					MarketPop pop = new MarketPop(pw, screenWidth, screenHight, activity, token);
					pop.showInputPwdPop(datas.get(position).getOrder_id(), datas.get(position).getOrder_amount(),list);
				}
			});
			
			holder.btnDel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					delOrderList(datas.get(position).getOrder_id());
				}

			});
			
			return convertView;
		}
		class ViewHolder {
			TextView tvOrdernum, tvType, tvTime,tvCount,tvMoney;
			Button btnPay,btnDel;
			MyGridView gv;
		}
	}
	
	private void delOrderList(String order_id) {
		post  = new HttpUtils();
		params = new RequestParams();
		String url = UrlUtils.postUrl+UrlUtils.path_orderRemove;
		dialog = ProgressDialogHandle.getProgressDialog(activity, null);
		params.addBodyParameter("token", token);
		params.addBodyParameter("order_id", order_id);
		post.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				if (dialog!=null) {
					dialog.show();
				}
				super.onStart();
			}
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				if (dialog!=null) {
					dialog.dismiss();
				}
				Toast.makeText(activity, "删除商品订单失败，请检查网络连接", 0).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog!=null) {
					dialog.dismiss();
				}
				JSONObject obj = JSONObject.parseObject(arg0.result);
				int code = obj.getIntValue("code");
				if (code == 1) {
					Toast.makeText(activity, "删除成功", 0).show();
					loadList();
				}else{
					Toast.makeText(activity, "删除商品订单失败", 0).show();
				}
			}
		});
	}
	
	
	/**
	 * 确认订单的适配器
	 * @author Taylor
	 *
	 */
	public class GridAdapter extends BaseAdapter {

		private ArrayList<ShopGoodsInfo> list;

		public void setDatas(ArrayList<ShopGoodsInfo> list) {
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
			holder.tvPrice.setText(list.get(position).getGoods_price() + "/块地");
			holder.tvCount.setText(list.get(position).getGoods_name() + ":" + list.get(position).getGoods_number());

			return convertView;
		}

		class ViewHolder {
			TextView tvCount, tvPrice;
		}
	}
	
}
