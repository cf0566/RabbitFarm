package com.cpic.rabbitfarm.popwindow;

import java.util.ArrayList;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.cpic.rabbitfarm.R;
import com.cpic.rabbitfarm.bean.ActivityData;
import com.cpic.rabbitfarm.bean.ActivityInfo;
import com.cpic.rabbitfarm.bean.ZhuanjiaData;
import com.cpic.rabbitfarm.bean.ZhuanjiaInfo;
import com.cpic.rabbitfarm.utils.DensityUtil;
import com.cpic.rabbitfarm.utils.GlideRoundTransform;
import com.cpic.rabbitfarm.utils.UrlUtils;
import com.cpic.rabbitfarm.view.ProgressDialogHandle;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.editorpage.ShareActivity;
import com.umeng.socialize.media.UMImage;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

public class ZhuanjiaPop {

	private PopupWindow pw;
	private PopupWindow pwShare;
	private int screenWidth;
	private int screenHight ;
	private Activity activity;
	private HttpUtils post;
	private String token;
	private RequestParams params;
	private Dialog dialog;
	private ArrayList<ZhuanjiaData> datas;
	/**
	 * 复用的控件
	 */
	private ImageView ivClose;
	private ImageView ivBack;
	/**
	 * 活动列表的控件
	 */
	private ListView lv;
	private ZhuanjiaAdapter adapter;
	private int messageUnread;
	private int activityUnread;
	
	/**
	 * 活动详情的控件
	 */
	private ImageView ivIcon;
	private TextView tvTitle,tvContent;
	

	
	
	public ZhuanjiaPop(PopupWindow pw, int screenWidth,int screenHight ,Activity activity,String token) {
		this.pw = pw;
		this.screenWidth = screenWidth;
		this.activity = activity;
		this.token = token;
		this.screenHight = screenHight;
	}

	/**
	 * 弹出庄家主界面
	 */
	public void showZhuanjiaMainPop() {
		View view = View.inflate(activity, R.layout.popwin_zhuanjia_main, null);
		pw = new PopupWindow(view, screenWidth*4/5, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		
		ivClose = (ImageView) view.findViewById(R.id.popwin_zhuanjia_main_iv_close);
		ivBack = (ImageView) view.findViewById(R.id.popwin_zhuanjia_main_iv_back);
		lv = (ListView) view.findViewById(R.id.popwin_zhuanjia_main_lv);
		
		WindowManager.LayoutParams params =	activity.getWindow().getAttributes();
		params.alpha = 1f;
		activity.getWindow().setAttributes(params);
		pw.setBackgroundDrawable(new ColorDrawable());
		pw.setOutsideTouchable(false);
		pw.showAtLocation(view, Gravity.CENTER, 0, 0);
		pw.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		loadActivityList();
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
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				pw.dismiss();
				showZhuanjiaDetailsPop(datas.get(position).getMessage_id(),
						datas.get(position).getMessage_img(),
						datas.get(position).getMessage_content()
						);
				
			}
		});
		ivBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadUnreadMsg();
			}
		});
	}
	/**
	 * 获取活动详情的pop
	 */
	public void showZhuanjiaDetailsPop(final String title,final String img_url,final String content) {
		View view = View.inflate(activity, R.layout.popwin_zhuanjia_details, null);
		pw = new PopupWindow(view, screenWidth, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		
		ivClose = (ImageView) view.findViewById(R.id.popwin_activity_details_iv_close);
		ivBack = (ImageView) view.findViewById(R.id.popwin_activity_details_iv_back);
		ivIcon = (ImageView) view.findViewById(R.id.pop_activity_details_iv_icon);
		tvTitle = (TextView) view.findViewById(R.id.pop_activity_details_tv_title);
		tvContent = (TextView) view.findViewById(R.id.pop_activity_details_tv_content);
		
		WindowManager.LayoutParams params =	activity.getWindow().getAttributes();
		params.alpha = 1f;
		activity.getWindow().setAttributes(params);
		pw.setBackgroundDrawable(new ColorDrawable());
		pw.setOutsideTouchable(false);
		pw.showAtLocation(view, Gravity.CENTER, 0, 0);
		pw.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		pw.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				WindowManager.LayoutParams params = activity.getWindow().getAttributes();
				params.alpha = 1f;
				activity.getWindow().setAttributes(params);
			}
		});
		tvTitle.setText(title);
		tvContent.setText(content);
		Glide.with(activity).load(img_url).placeholder(R.drawable.h_xiqtp).transform(new GlideRoundTransform(activity, 80))
		.fitCenter().into(ivIcon);
		
		ivClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pw.dismiss();
			}
		});
		ivBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pw.dismiss();
				showZhuanjiaMainPop();
			}
		});
	
	}
	
	private void loadUnreadMsg() {
		post = new HttpUtils();
		params = new RequestParams();
		String url = UrlUtils.postUrl + UrlUtils.path_getMessageCount;
		params.addBodyParameter("token", token);
		post.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
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
					pw.dismiss();
					MessageMainPop pop = new MessageMainPop(pw, screenWidth,screenHight,activity, token, activityUnread, messageUnread);
					pop.showMessageMainPop();
				} 
			}
		});
	}
	
	
	/**
	 * 加载活动列表
	 */
	public void loadActivityList(){
		post = new HttpUtils();
		params = new RequestParams();
		dialog = ProgressDialogHandle.getProgressDialog(activity, null);
		String url = UrlUtils.postUrl+UrlUtils.path_messageList;
		params.addBodyParameter("token", token);
		params.addBodyParameter("type", "1");
		
		post.send(HttpMethod.POST, url , params, new RequestCallBack<String>() {
			
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
				Toast.makeText(activity, "获取活动列表失败，请检查网络连接", 0).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				ZhuanjiaInfo info = JSONObject.parseObject(arg0.result, ZhuanjiaInfo.class);
				if (info.getCode() == 1) {
					datas = info.getData();
					adapter = new ZhuanjiaAdapter();
					adapter.setDatas(datas);
					lv.setAdapter(adapter);
				}else{
					Toast.makeText(activity, "获取活动列表失败", 0).show();
				}
			}
		});
	}
	public class ZhuanjiaAdapter extends BaseAdapter {
		
		private ArrayList<ZhuanjiaData> datas;

		public void setDatas(ArrayList<ZhuanjiaData> datas) {
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
				convertView = View.inflate(activity, R.layout.item_popwin_activity_list, null);
				holder = new ViewHolder();
				holder.tvTitle = (TextView) convertView.findViewById(R.id.item_activity_main_tv_title);
				holder.tvContent = (TextView) convertView.findViewById(R.id.item_activity_main_tv_content);
				holder.ivIcon = (ImageView) convertView.findViewById(R.id.item_activity_main_iv);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if ("0".equals(datas.get(position))) {
				holder.tvTitle.setTextColor(Color.parseColor("#77FF0000"));
				holder.tvContent.setTextColor(Color.parseColor("#77FF0000"));
			}else if ("1".equals(datas.get(position))) {
				holder.tvTitle.setTextColor(Color.parseColor("#5b3509"));
				holder.tvContent.setTextColor(Color.parseColor("#593607"));
			}
			
			holder.tvTitle.setText(datas.get(position).getMessage_id());
			holder.tvContent.setText(datas.get(position).getMessage_content());
			holder.ivIcon.setImageResource(R.drawable.icon2);
			return convertView;
		}
		class ViewHolder {
			TextView tvTitle,tvContent;
			ImageView ivIcon;
		}
	}
	
}
