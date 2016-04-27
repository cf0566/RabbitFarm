package com.cpic.rabbitfarm.popwindow;

import java.util.ArrayList;

import com.alibaba.fastjson.JSONObject;
import com.cpic.rabbitfarm.R;
import com.cpic.rabbitfarm.activity.MainActivity;
import com.cpic.rabbitfarm.adapter.FriendListAdapter;
import com.cpic.rabbitfarm.adapter.FriendListAdapter.addOnlineFsIf;
import com.cpic.rabbitfarm.bean.Friend;
import com.cpic.rabbitfarm.bean.FriendData;
import com.cpic.rabbitfarm.utils.UrlUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

public class AddFriendPop implements addOnlineFsIf{

	private PopupWindow pw;
	private int screenWidth;
	private MainActivity activity;
	private HttpUtils post;
	private String token;
	private RequestParams params;
	private Dialog dialog;
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
	public AddFriendPop(PopupWindow pw, int screenWidth, int screenHight, Activity activity, String token) {
		this.pw = pw;
		this.screenWidth = screenWidth;
		this.activity = (MainActivity) activity;
		this.token = token;
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

				JSONObject.parseObject(arg0.result, FriendData.class);
				friends = (ArrayList<Friend>) JSONObject.parseObject(arg0.result, FriendData.class).data;
				Log.e("test", "friends" + friends);
				if (null != friends) {
					/**
					 * 1代表在线用户调用适配器标志位
					 */
					FriendListAdapter adapter = new FriendListAdapter(activity, token, friends,1,AddFriendPop.this);

					lvOnlineFriends.setAdapter(adapter);

				}
			}
		});
	}

	
	void showShortToast(String msg){
		Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void addOline(int position) {
		String userId=null;
		if(null!=friends){
			
			userId=friends.get(position).ease_user;
		}
		AddFriendDetailPop pop=new AddFriendDetailPop(addDetailPop, activity.getScreenWidth(), activity.getScreenHight(), activity, token,userId);
		pop.showAddFriendDetailPop();
		pw.dismiss();
		
	}
	

}
