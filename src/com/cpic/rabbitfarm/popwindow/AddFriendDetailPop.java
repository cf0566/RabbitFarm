package com.cpic.rabbitfarm.popwindow;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
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
import com.easemob.chat.EMContactManager;
import com.easemob.chatuidemo.activity.AlertDialog;
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
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

public class AddFriendDetailPop {

	private PopupWindow pw;
	private int screenWidth;
	private MainActivity activity;
	/**
	 * 环信Id
	 */
	private String userId;
	
	/**
	 * 发送验证的Button等
	 */
	
	private Button sendBt;
	private EditText editInfo;
	
	/**
	 * 复用的控件
	 */
	private ImageView ivClose;
	public AddFriendDetailPop(PopupWindow pw, int screenWidth, int screenHight, Activity activity, String token,String userId) {
		this.pw = pw;
		this.screenWidth = screenWidth;
		this.activity = (MainActivity) activity;
		this.userId=userId;
	}

	/**
	 * 弹出添加好友界面
	 */
	public void showAddFriendDetailPop() {
		View view = View.inflate(activity, R.layout.send_info_add_friend, null);
		pw = new PopupWindow(view, screenWidth * 4 / 5, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);

		ivClose = (ImageView) view.findViewById(R.id.popwin_send_add_friend_close);
		sendBt=(Button) view.findViewById(R.id.bt_send);
		editInfo=(EditText) view.findViewById(R.id.et_info);
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
		sendBt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				addContact(userId,editInfo.getText().toString());
				pw.dismiss();
				
			}
		});
	}

	void showShortToast(String msg) {
		Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 *  添加contact
	 * @param view
	 */
	public void addContact(final String userId,final String info){
		if(MyApplication.getInstance().getUserName().equals(userId)){
			String str =activity.getResources().getString(R.string.not_add_myself);
			activity.startActivity(new Intent(activity, AlertDialog.class).putExtra("msg", str));
			return;
		}
		
		if(MyApplication.getInstance().getContactList().containsKey(userId)){
		    //提示已在好友列表中，无需添加
		    if(EMContactManager.getInstance().getBlackListUsernames().contains(userId)){
		    	activity.startActivity(new Intent(activity, AlertDialog.class).putExtra("msg", "此用户已是你好友(被拉黑状态)，从黑名单列表中移出即可"));
		        return;
		    }
			String strin = activity.getResources().getString(R.string.This_user_is_already_your_friend);
			activity.startActivity(new Intent(activity, AlertDialog.class).putExtra("msg", strin));
			return;
		}
		
		final ProgressDialog progressDialog = new ProgressDialog(activity);
		String stri = activity.getResources().getString(R.string.Is_sending_a_request);
		progressDialog.setMessage(stri);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		
		new Thread(new Runnable() {
			public void run() {
				
				try {
					activity. getResources().getString(R.string.Add_a_friend);
					EMContactManager.getInstance().addContact(userId, info);
					((Activity) activity).runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							String s1 = activity.getResources().getString(R.string.send_successful);
							Toast.makeText(activity.getApplicationContext(), s1, 1).show();
						}
					});
				} catch (final Exception e) {
					((Activity) activity).runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							String s2 = activity.getResources().getString(R.string.Request_add_buddy_failure);
							Toast.makeText(activity.getApplicationContext(), s2 + e.getMessage(), 1).show();
						}
					});
				}
			}
		}).start();
	}

}
