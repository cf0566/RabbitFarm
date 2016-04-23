package com.cpic.rabbitfarm.popwindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.cpic.rabbitfarm.R;
import com.cpic.rabbitfarm.base.MyApplication;
import com.cpic.rabbitfarm.bean.ZhuanjiaData;
import com.cpic.rabbitfarm.bean.ZhuanjiaInfo;
import com.cpic.rabbitfarm.utils.UrlUtils;
import com.cpic.rabbitfarm.view.ProgressDialogHandle;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMNotifier;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.activity.ChatActivity;
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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

public class FriendInfoPop {

	private PopupWindow pw;
	private PopupWindow chatRecordPop;
	private PopupWindow pwShare;
	private int screenWidth;
	private int screenHight;
	private Activity activity;
	private HttpUtils post;
	private String token;
	private RequestParams params;
	private Dialog dialog;
	private ArrayList<ZhuanjiaData> datas;

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
	private LinearLayout friendReqquestll;
	private LinearLayout chatRecordll;
	private int messageUnread;
	private int activityUnread;

	/**
	 * 活动详情的控件
	 */
	private ImageView ivIcon;
	private TextView tvTitle, tvContent;

	public FriendInfoPop(PopupWindow pw, int screenWidth, int screenHight, Activity activity, String token) {
		this.pw = pw;
		this.screenWidth = screenWidth;
		this.activity = activity;
		this.token = token;
		this.screenHight = screenHight;
	}

	/**
	 * 弹出好友信息主界面
	 */
	public void showFriendInfoMainPop() {
		View view = View.inflate(activity, R.layout.popwin_friend_info_main, null);
		pw = new PopupWindow(view, screenWidth * 4 / 5, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);

		friendReqquestll = (LinearLayout) view.findViewById(R.id.popwin_message_main_ll_friend_request);
		chatRecordll = (LinearLayout) view.findViewById(R.id.popwin_message_main_ll_chat_record);

		ivClose = (ImageView) view.findViewById(R.id.popwin_friend_info_main_iv_close);
		ivBack = (ImageView) view.findViewById(R.id.popwin_friend_info_main_iv_back);

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

		ivClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pw.dismiss();
			}
		});
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadUnreadMsg();
			}
		});

		friendReqquestll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				pw.dismiss();
				FriendRequestPop pop = new FriendRequestPop(pw, screenWidth, screenHight, activity, token);
				pop.showFriendRequestMainPop();
				inviteMessgeDao = new InviteMessgeDao(activity);
				userDao = new UserDao(activity);
				EMContactManager.getInstance().setContactListener(new MyContactListener());
				EMChat.getInstance().setAppInited();
				InviteMessgeDao dao = new InviteMessgeDao(activity);
				List<InviteMessage> msgs = dao.getMessagesList();

			}
		});

		chatRecordll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ChatRecordPop pop = new ChatRecordPop(chatRecordPop, screenWidth, screenHight, activity, token);
				pop.showChatRecordPop();
				pw.dismiss();
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
					MessageMainPop pop = new MessageMainPop(pw, screenWidth, screenHight, activity, token, activityUnread, messageUnread);
					pop.showMessageMainPop();
				}
			}
		});
	}

	/**
	 * 加载活动列表
	 */
	public void loadActivityList() {
		post = new HttpUtils();
		params = new RequestParams();
		dialog = ProgressDialogHandle.getProgressDialog(activity, null);
		String url = UrlUtils.postUrl + UrlUtils.path_messageList;
		params.addBodyParameter("token", token);
		params.addBodyParameter("type", "1");

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
				Toast.makeText(activity, "获取活动列表失败，请检查网络连接", 0).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				ZhuanjiaInfo info = JSONObject.parseObject(arg0.result, ZhuanjiaInfo.class);
				if (info.getCode() == 1) {
				} else {
					Toast.makeText(activity, "获取活动列表失败", 0).show();
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
			Log.d("", username + "请求加你为好友,reason: " + reason);
			// 设置相应status
			msg.setStatus(InviteMesageStatus.BEINVITEED);
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
			Log.d("test", username + "同意了你的好友请求");
			msg.setStatus(InviteMesageStatus.BEAGREED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactRefused(String username) {
			// 参考同意，被邀请实现此功能,demo未实现
			Log.d(username, username + "拒绝了你的好友请求");
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
