package com.cpic.rabbitfarm.popwindow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.cpic.rabbitfarm.R;
import com.cpic.rabbitfarm.adapter.FriendInviteAdapter;
import com.cpic.rabbitfarm.base.MyApplication;
import com.cpic.rabbitfarm.bean.EaseUser;
import com.cpic.rabbitfarm.utils.UrlUtils;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMNotifier;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.db.InviteMessgeDao;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.InviteMessage;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.domain.InviteMessage.InviteMesageStatus;
import com.easemob.util.HanziToPinyin;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

public class FriendRequestPop {

	private PopupWindow pw;
	private int screenWidth;
	private int screenHight;
	private Activity activity;
	private HttpUtils post;
	private String token;
	private RequestParams params;
	private Dialog dialog;
	/**
	 * 环信定义变量
	 */
	private UserDao userDao;
	private InviteMessgeDao inviteMessgeDao;

	/**
	 * 复用的控件
	 */
	private ImageView ivClose;
	private ImageView ivBack;
	/**
	 * 活动列表的控件
	 */
	private ListView lv;
	public FriendRequestPop(PopupWindow pw, int screenWidth, int screenHight, Activity activity, String token) {
		this.pw = pw;
		this.screenWidth = screenWidth;
		this.activity = activity;
		this.token = token;
		this.screenHight = screenHight;
	}

	/**
	 * 好友请求界面
	 */
	public void showFriendRequestMainPop() {
		View view = View.inflate(activity, R.layout.popwin_friend_request_main, null);
		pw = new PopupWindow(view, screenWidth * 4 / 5, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);

		ivClose = (ImageView) view.findViewById(R.id.popwin_friend_request_main_iv_close);
		ivBack = (ImageView) view.findViewById(R.id.popwin_friend_request_main_iv_back);
		lv = (ListView) view.findViewById(R.id.popwin_friend_request_main_lv);

		WindowManager.LayoutParams params = activity.getWindow().getAttributes();
		params.alpha = 0.6f;
		activity.getWindow().setAttributes(params);
		pw.setBackgroundDrawable(new ColorDrawable());
		pw.setOutsideTouchable(false);
		pw.showAtLocation(view, Gravity.CENTER, 0, 0);
		pw.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		new InviteMessgeDao(activity);
		loadFriendInviteList();
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
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pw.dismiss();
				FriendInfoPop pop = new FriendInfoPop(pw, screenWidth, screenHight, activity, token);
				pop.showFriendInfoMainPop();
			}
		});
	}

	public void loadFriendInviteList() {

		InviteMessgeDao dao = new InviteMessgeDao(activity);
		List<InviteMessage> msgs = dao.getMessagesList();
//		Log.e("test", msgs.toString());
		loadInviteInfo(msgs);
		MyApplication.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME).setUnreadMsgCount(0);
	}

	private void loadInviteInfo(final List<InviteMessage> msgs) {
		post = new HttpUtils();
		params = new RequestParams();
		String url = UrlUtils.postUrl + UrlUtils.path_getEaseInfo;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < msgs.size(); i++) {
			sb.append(msgs.get(i).getFrom()).append(",");
		}
		params.addBodyParameter("users", sb.toString());
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
					EaseUser user = JSONObject.parseObject(arg0.result, EaseUser.class);
					if (null != user.getData()) {
						FriendInviteAdapter adapter = new FriendInviteAdapter(msgs, activity, token, user.getData());
						lv.setAdapter(adapter);
						MyApplication.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME).setUnreadMsgCount(0);
					}else{
						Toast.makeText(activity, "身份验证过期，请重新登录", Toast.LENGTH_SHORT).show();
					}

				}
			}
		});
	}




	/**
	 * set head
	 * 
	 * @param username
	 * @return
	 */
	User setUserHead(String username) {
		User user = new User();
		user.setUsername(username);
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
		return user;
	}

	/***
	 * 好友变化listener
	 * 
	 */
	private class MyContactListener implements EMContactListener {

		@Override
		public void onContactAdded(List<String> usernameList) {
			// 保存增加的联系人
			Map<String, User> localUsers = MyApplication.getInstance().getContactList();
			Map<String, User> toAddUsers = new HashMap<String, User>();
			for (String username : usernameList) {
				User user = setUserHead(username);
				// 添加好友时可能会回调added方法两次
				if (!localUsers.containsKey(username)) {
					userDao.saveContact(user);
				}
				toAddUsers.put(username, user);
			}
			localUsers.putAll(toAddUsers);

		}

		@Override
		public void onContactInvited(String username, String reason) {
			// 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不需要重复提醒
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();

			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
					inviteMessgeDao.deleteMessage(username);
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			msg.setReason(reason);
//			Log.d("", username + "请求加你为好友,reason: " + reason);
			// 设置相应status
			msg.setStatus(InviteMesageStatus.BEINVITEED);
			loadFriendInviteList();

			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactAgreed(String username) {
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
//			Log.d("test", username + "同意了你的好友请求");
			msg.setStatus(InviteMesageStatus.BEAGREED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactRefused(String username) {
			// 参考同意，被邀请实现此功能,demo未实现
//			Log.d(username, username + "拒绝了你的好友请求");
		}

		@Override
		public void onContactDeleted(List<String> arg0) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * 保存提示新消息
	 * 
	 * @param msg
	 */
	private void notifyNewIviteMessage(InviteMessage msg) {
		saveInviteMsg(msg);
		// 提示有新消息
		EMNotifier.getInstance(activity.getApplicationContext()).notifyOnNewMsg();

		// 刷新bottom bar消息未读数
		updateUnreadAddressLable();
		// 刷新好友页面ui
		/*
		 * if (currentTabIndex == 1) contactListFragment.refresh();
		 */
	}

	/**
	 * 保存邀请等msg
	 * 
	 * @param msg
	 */
	private void saveInviteMsg(InviteMessage msg) {
		// 保存msg
		inviteMessgeDao.saveMessage(msg);
		// 未读数加1
		User user = MyApplication.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME);
		if (user.getUnreadMsgCount() == 0)
			user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
	}

	/**
	 * 刷新申请与通知消息数
	 */
	public void updateUnreadAddressLable() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				int count = getUnreadAddressCountTotal();
				if (count > 0) {
				} else {
				}
			}
		});

	}

	/**
	 * 获取未读申请与通知消息
	 * 
	 * @return
	 */
	public int getUnreadAddressCountTotal() {
		int unreadAddressCountTotal = 0;
		if (MyApplication.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME) != null)
			unreadAddressCountTotal = MyApplication.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME).getUnreadMsgCount();
		return unreadAddressCountTotal;
	}

	/**
	 * 获取未读消息数
	 * 
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		return unreadMsgCountTotal;
	}

}
