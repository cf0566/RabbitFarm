package com.cpic.rabbitfarm.popwindow;

import java.util.ArrayList;

import com.alibaba.fastjson.JSONObject;
import com.cpic.rabbitfarm.R;
import com.cpic.rabbitfarm.activity.MainActivity.SeedAdapter;
import com.cpic.rabbitfarm.bean.Seed;
import com.cpic.rabbitfarm.bean.SeedInfo;
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
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.AdapterViewAnimator;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class StoreRoomPop {

	private PopupWindow pw;
	private int screenWidth;
	private int screenHight;
	private Activity activity;
	private String token;
	private RadioGroup rGroup;
	private ImageView ivClose;
	private GridView gv;
	private ImageView ivNoBuy,ivNoZhuan;

	private ArrayList<SeedInfo> datas;
	private HttpUtils post;
	private RequestParams params;
	private Dialog dialog;
	private StoreAdapter adapter;

	public StoreRoomPop(PopupWindow pw, int screenWidth, int screenHight, Activity activity, String token) {
		this.pw = pw;
		this.screenWidth = screenWidth;
		this.activity = activity;
		this.token = token;
		this.screenHight = screenHight;
	}

	public void showStoreMainPop() {
		View view = View.inflate(activity, R.layout.popwin_storeroom, null);
		pw = new PopupWindow(view, screenWidth, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		WindowManager.LayoutParams params1 = activity.getWindow().getAttributes();
		params1.alpha = 0.6f;
		activity.getWindow().setAttributes(params1);
		rGroup = (RadioGroup) view.findViewById(R.id.popwin_storeroom_rg);
		gv = (GridView) view.findViewById(R.id.popwin_storeroom_gv);
		ivNoBuy  = (ImageView) view.findViewById(R.id.popwin_storeroom_iv_nobuy);
		ivNoZhuan = (ImageView) view.findViewById(R.id.popwin_storeroom_iv_nozhuanjia);
		ivClose = (ImageView) view.findViewById(R.id.popwin_storeroom_iv_close);
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
		
		rGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.popwin_storeroom_rb_buy:
					if (datas.size() != 0) {
						gv.setVisibility(View.VISIBLE);
						ivNoBuy.setVisibility(View.GONE);
						ivNoZhuan.setVisibility(View.GONE);
						adapter = new StoreAdapter();
						adapter.setDatas(datas);
						gv.setAdapter(adapter);
					}else{
						gv.setVisibility(View.GONE);
						ivNoBuy.setVisibility(View.VISIBLE);
						ivNoZhuan.setVisibility(View.GONE);
					}
					
					break;
				case R.id.popwin_storeroom_rb_zhuanjia:
					gv.setVisibility(View.GONE);
					ivNoBuy.setVisibility(View.GONE);
					ivNoZhuan.setVisibility(View.VISIBLE);
					break;

				default:
					break;
				}
			}
		});

	}

	public void loadList() {
		post = new HttpUtils();
		params = new RequestParams();
		dialog = ProgressDialogHandle.getProgressDialog(activity, null);
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
					Toast.makeText(activity, "获取储藏室数据失败,请检查网络连接", 0).show();
				}
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
					if (datas.size() != 0) {
						gv.setVisibility(View.VISIBLE);
						ivNoBuy.setVisibility(View.GONE);
						ivNoZhuan.setVisibility(View.GONE);
						adapter = new StoreAdapter();
						adapter.setDatas(datas);
						gv.setAdapter(adapter);
					}else{
						gv.setVisibility(View.GONE);
						ivNoBuy.setVisibility(View.VISIBLE);
						ivNoZhuan.setVisibility(View.GONE);
					}
				} else {
					Toast.makeText(activity, "获取储藏室数据失败,请重新登录", 0).show();
				}
			}
		});
	}

	public class StoreAdapter extends BaseAdapter {

		private ArrayList<SeedInfo> datas;

		public void setDatas(ArrayList<SeedInfo> datas) {
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
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(activity, R.layout.item_store_list, null);
				holder.tvName = (TextView) convertView.findViewById(R.id.item_store_tv_seeds);
				holder.tvCount = (TextView) convertView.findViewById(R.id.item_store_tv_count);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvName.setText(datas.get(position).getGoods_name());
			holder.tvCount.setText(datas.get(position).getGoods_number()+"块地的量");

			return convertView;
		}

		class ViewHolder {
			TextView tvName, tvCount;
		}
	}

}
