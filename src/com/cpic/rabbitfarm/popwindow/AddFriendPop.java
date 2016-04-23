package com.cpic.rabbitfarm.popwindow;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.cpic.rabbitfarm.R;
import com.cpic.rabbitfarm.activity.MainActivity;
import com.cpic.rabbitfarm.adapter.FriendInviteAdapter;
import com.cpic.rabbitfarm.adapter.FriendListAdapter;
import com.cpic.rabbitfarm.bean.ActivityData;
import com.cpic.rabbitfarm.bean.ActivityInfo;
import com.cpic.rabbitfarm.bean.Friend;
import com.cpic.rabbitfarm.bean.FriendData;
import com.cpic.rabbitfarm.bean.ZhuanjiaData;
import com.cpic.rabbitfarm.bean.ZhuanjiaInfo;
import com.cpic.rabbitfarm.utils.DensityUtil;
import com.cpic.rabbitfarm.utils.GlideRoundTransform;
import com.cpic.rabbitfarm.utils.UrlUtils;
import com.cpic.rabbitfarm.view.ProgressDialogHandle;
import com.easemob.chatuidemo.db.InviteMessgeDao;
import com.easemob.chatuidemo.domain.InviteMessage;
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
import android.util.Log;
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

public class AddFriendPop {

	private PopupWindow pw;
	private PopupWindow pwShare;
	private int screenWidth;
	private int screenHight;
	private MainActivity activity;
	private HttpUtils post;
	private String token;
	private RequestParams params;
	private Dialog dialog;
	private ArrayList<ZhuanjiaData> datas;
	private PopupWindow addDetailPop;
	
	/**
	 * 定义friends以及控件
	 */
	private ArrayList<Friend> friends;
	private ListView lvOnlineFriends;
	
	/**
	 * 复用的控件
	 */
	private ImageView ivClose;
	private ImageView ivBack;
	/**
	 * 活动列表的控件
	 */
	private ListView lv;
	private int messageUnread;
	private int activityUnread;

	/**
	 * 活动详情的控件
	 */
	private ImageView ivIcon;
	private TextView tvTitle, tvContent;

	public AddFriendPop(PopupWindow pw, int screenWidth, int screenHight, Activity activity, String token) {
		this.pw = pw;
		this.screenWidth = screenWidth;
		this.activity = (MainActivity) activity;
		this.token = token;
		this.screenHight = screenHight;
	}

	/**
	 * 弹出添加好友界面
	 */
	public void showAddFriendPop() {
		View view = View.inflate(activity, R.layout.add_friend, null);
		pw = new PopupWindow(view, screenWidth * 4 / 5, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);

		ivClose = (ImageView) view.findViewById(R.id.popwin_add_friend_close);
		lvOnlineFriends = (ListView) view.findViewById(R.id.activity_add_friend_lv);

		WindowManager.LayoutParams params = activity.getWindow().getAttributes();
		params.alpha = 0.6f;
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
		loadOnLineUsers();
		ivClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pw.dismiss();
			}
		});
		lvOnlineFriends.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				// showZhuanjiaDetailsPop(datas.get(position).getMessage_id(),
				// datas.get(position).getMessage_img(),
				// datas.get(position).getMessage_content());
				String userId=null;
				if(null!=friends){
					
					userId=friends.get(position).ease_user;
				}
				AddFriendDetailPop pop=new AddFriendDetailPop(addDetailPop, activity.getScreenWidth(), activity.getScreenHight(), activity, token,userId);
				pop.showAddFriendDetailPop();
				pw.dismiss();
			}
		});
	}


	/**
	 * 加载在线好友
	 */
	private void loadOnLineUsers() {

		post = new HttpUtils();
		params = new RequestParams();
		String url = UrlUtils.postUrl + UrlUtils.path_onlineList;
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
				showShortToast("提交失败，请检查网络连接");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}

				FriendData obj = JSONObject.parseObject(arg0.result, FriendData.class);
				friends = (ArrayList<Friend>) JSONObject.parseObject(arg0.result, FriendData.class).data;
				Log.e("test", "friends" + friends);
				if (null != friends) {
					/**
					 * 1代表在线用户调用适配器标志位
					 */
					FriendListAdapter adapter = new FriendListAdapter(activity, token, friends,1);

					lvOnlineFriends.setAdapter(adapter);

				}
			}
		});
	}

	
	void showShortToast(String msg){
		Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
	}
	

}
