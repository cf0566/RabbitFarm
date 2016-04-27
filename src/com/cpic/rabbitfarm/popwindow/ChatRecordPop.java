package com.cpic.rabbitfarm.popwindow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alipay.a.a.a;
import com.bumptech.glide.Glide;
import com.cpic.rabbitfarm.R;
import com.cpic.rabbitfarm.activity.MainActivity;
import com.cpic.rabbitfarm.adapter.FriendInviteAdapter;
import com.cpic.rabbitfarm.adapter.FriendListAdapter;
import com.cpic.rabbitfarm.base.MyApplication;
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
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chatuidemo.activity.ChatActivity;
import com.easemob.chatuidemo.adapter.ChatAllHistoryAdapter;
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
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.util.Pair;
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

public class ChatRecordPop {

	private PopupWindow pw;
	private int screenWidth;
	private MainActivity activity;
	private HttpUtils post;
	private String token;
	private RequestParams params;
	private Dialog dialog;
	/**
	 * 定义friends以及控件
	 */
	private ArrayList<Friend> friends;
	
	private ListView lvChat;
	
	/**
	 * 定义环信内容
	 */
	private List<EMConversation> conversationList = new ArrayList<EMConversation>();
	private ChatAllHistoryAdapter adapter;
	
	/**
	 * 复用的控件
	 */
	private ImageView ivClose;
	public ChatRecordPop(PopupWindow pw, int screenWidth, int screenHight, Activity activity, String token) {
		this.pw = pw;
		this.screenWidth = screenWidth;
		this.activity = (MainActivity) activity;
		this.token = token;
	}

	/**
	 * 弹出添加好友界面
	 */
	public void showChatRecordPop() {
		View view = View.inflate(activity, R.layout.chat_record_pop, null);
		pw = new PopupWindow(view, screenWidth * 4 / 5, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);

		ivClose = (ImageView) view.findViewById(R.id.popwin_cht_record_close);
		lvChat = (ListView) view.findViewById(R.id.chat_record_lv);

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
		
		conversationList.addAll(loadConversationsWithRecentChat());
		adapter = new ChatAllHistoryAdapter(activity, 1, conversationList);
		// 设置adapter
		lvChat.setAdapter(adapter);
		ivClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pw.dismiss();
			}
		});
		/*lvChat.setOnItemClickListener(new OnItemClickListener() {

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
		});*/
		final String st2 = activity.getResources().getString(R.string.Cant_chat_with_yourself);
		lvChat.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				EMConversation conversation = adapter.getItem(position);
				String username = conversation.getUserName();
				if (username.equals(MyApplication.getInstance().getUserName()))
					Toast.makeText(activity, st2, 0).show();
				else {
				    // 进入聊天页面
				    Intent intent = new Intent(activity, ChatActivity.class);
				    if(conversation.isGroup()){
				        // it is group chat
                        intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                        intent.putExtra("groupId", username);
				    }else{
				        // it is single chat
                        intent.putExtra("userId", username);
				    }
				    activity.startActivity(intent);
				    pw.dismiss();
				}
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
					new FriendListAdapter(activity, token, friends,1);


				}
			}
		});
	}
	
	
	
	void showShortToast(String msg){
		Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
	}
	/**
	 * 获取所有会话
	 * 
	 * @param context
	 * @return
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        +	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
		// 过滤掉messages size为0的conversation
		/**
		 * 如果在排序过程中有新消息收到，lastMsgTime会发生变化
		 * 影响排序过程，Collection.sort会产生异常
		 * 保证Conversation在Sort过程中最后一条消息的时间不变 
		 * 避免并发问题
		 */
		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
		synchronized (conversations) {
			for (EMConversation conversation : conversations.values()) {
				if (conversation.getAllMessages().size() != 0) {
					sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
				}
			}
		}
		try {
			// Internal is TimSort algorithm, has bug
			sortConversationByLastChatTime(sortList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<EMConversation> list = new ArrayList<EMConversation>();
		for (Pair<Long, EMConversation> sortItem : sortList) {
			list.add(sortItem.second);
		}
		return list;
	}
	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
		Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
			@Override
			public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

				if (con1.first == con2.first) {
					return 0;
				} else if (con2.first > con1.first) {
					return 1;
				} else {
					return -1;
				}
			}

		});
	}

}
