package com.cpic.rabbitfarm.adapter;

/**
 * 好友列表适配器
 */
import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.cpic.rabbitfarm.R;
import com.cpic.rabbitfarm.activity.MainActivity;
import com.cpic.rabbitfarm.base.MyApplication;
import com.cpic.rabbitfarm.bean.Friend;
import com.cpic.rabbitfarm.utils.GlideRoundTransform;
import com.cpic.rabbitfarm.utils.RoundImageView;
import com.easemob.chat.EMContactManager;
import com.easemob.chatuidemo.activity.AlertDialog;
import com.easemob.chatuidemo.db.InviteMessgeDao;
import com.easemob.chatuidemo.domain.InviteMessage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FriendListAdapter extends BaseAdapter {

	private Context mcontext;
	private InviteMessgeDao messgeDao;
	private List<InviteMessage> objects;
	private String token;
	private ArrayList<Friend> friends;
	private int actionId;
	private PopupWindow addDetailPop;
	private MainActivity activity;

	public FriendListAdapter(Context mcontext, String token, ArrayList<Friend> friends, int actionId) {

		this.mcontext = mcontext;
		this.token = token;
		this.friends = friends;
		this.actionId = actionId;
		activity=(MainActivity) mcontext;
	}

	@Override
	public int getCount() {
		return friends.size();
	}

	@Override
	public Object getItem(int position) {
		return friends.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Friend friend = (Friend) getItem(position);
		final ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(mcontext, R.layout.friend_list_item, null);
			holder = new ViewHolder();
			holder.friend_logo = (RoundImageView) convertView.findViewById(R.id.friend_logo);
			holder.friend_list_user_name = (TextView) convertView.findViewById(R.id.friend_list_user_name);
			holder.friend_info = (TextView) convertView.findViewById(R.id.friend_info);
			holder.add_friend_bt = (Button) convertView.findViewById(R.id.add_friend_bt);
			holder.friend_list_id = (RelativeLayout) convertView.findViewById(R.id.friend_list_id);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.friend_list_user_name.setText(friend.alias_name);
		holder.friend_info.setText(friend.level);
		if (actionId == 1) {
			holder.add_friend_bt.setVisibility(View.VISIBLE);
			holder.friend_list_id.setBackgroundResource(R.drawable.x_bg2);
		}
		
		Glide.with(mcontext).load(friend.user_img).placeholder(R.drawable.m_tx).fitCenter().into(holder.friend_logo);
		/*holder.add_friend_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				//addContact(friend.ease_user);
				AddFriendDetailPop pop=new AddFriendDetailPop(addDetailPop, activity.getScreenWidth(), activity.getScreenHight(), activity, token);
			}
		});*/

		return convertView;
	}

	class ViewHolder {

		RoundImageView friend_logo;
		TextView friend_list_user_name;
		TextView friend_info;
		LinearLayout friend_invite_button_layout;
		TextView friend_invite_state;
		Button add_friend_bt;
		RelativeLayout friend_list_id;
	}
	
	/**
	 *  添加contact
	 * @param view
	 */
	public void addContact(final String userId){
		if(MyApplication.getInstance().getUserName().equals(userId)){
			String str =mcontext.getResources().getString(R.string.not_add_myself);
			mcontext.startActivity(new Intent(mcontext, AlertDialog.class).putExtra("msg", str));
			return;
		}
		
		if(MyApplication.getInstance().getContactList().containsKey(userId)){
		    //提示已在好友列表中，无需添加
		    if(EMContactManager.getInstance().getBlackListUsernames().contains(userId)){
		    	mcontext.startActivity(new Intent(mcontext, AlertDialog.class).putExtra("msg", "此用户已是你好友(被拉黑状态)，从黑名单列表中移出即可"));
		        return;
		    }
			String strin = mcontext.getResources().getString(R.string.This_user_is_already_your_friend);
			mcontext.startActivity(new Intent(mcontext, AlertDialog.class).putExtra("msg", strin));
			return;
		}
		
		final ProgressDialog progressDialog = new ProgressDialog(mcontext);
		String stri = mcontext.getResources().getString(R.string.Is_sending_a_request);
		progressDialog.setMessage(stri);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		
		new Thread(new Runnable() {
			public void run() {
				
				try {
					//demo写死了个reason，实际应该让用户手动填入
					String s =mcontext. getResources().getString(R.string.Add_a_friend);
					EMContactManager.getInstance().addContact(userId, s);
					((Activity) mcontext).runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							String s1 = mcontext.getResources().getString(R.string.send_successful);
							Toast.makeText(mcontext.getApplicationContext(), s1, 1).show();
						}
					});
				} catch (final Exception e) {
					((Activity) mcontext).runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							String s2 = mcontext.getResources().getString(R.string.Request_add_buddy_failure);
							Toast.makeText(mcontext.getApplicationContext(), s2 + e.getMessage(), 1).show();
						}
					});
				}
			}
		}).start();
	}

}
