package com.cpic.rabbitfarm.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.cpic.rabbitfarm.R;
import com.cpic.rabbitfarm.utils.UrlUtils;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.db.InviteMessgeDao;
import com.easemob.chatuidemo.domain.InviteMessage;
import com.easemob.chatuidemo.domain.InviteMessage.InviteMesageStatus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.socialize.utils.Log;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FriendInviteAdapter extends BaseAdapter {

	private Context mcontext;
	private InviteMessgeDao messgeDao;
	private List<InviteMessage> objects;
	private String token;

	public FriendInviteAdapter(List<InviteMessage> objects, Context mcontext,String token) {
		this.mcontext = mcontext;
		this.objects = objects;
		messgeDao = new InviteMessgeDao(mcontext);
		this.token=token;
	}

	@Override
	public int getCount() {

		return objects.size();
	}

	@Override
	public Object getItem(int position) {
		return objects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final InviteMessage msg = (InviteMessage) getItem(position);
		final ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(mcontext, R.layout.item_friend_invite, null);
			holder = new ViewHolder();
			holder.friend_invite_user_logo = (ImageView) convertView.findViewById(R.id.friend_invite_user_logo);
			holder.friend_invite_user_name = (TextView) convertView.findViewById(R.id.friend_invite_user_name);
			holder.friend_invite_info = (TextView) convertView.findViewById(R.id.friend_invite_info);
			holder.friend_invite_comment = (TextView) convertView.findViewById(R.id.friend_invite_comment);
			holder.friend_invite_button_layout = (LinearLayout) convertView.findViewById(R.id.friend_invite_button_layout);
			holder.button_yes = (Button) convertView.findViewById(R.id.button_yes);
			holder.button_no = (Button) convertView.findViewById(R.id.button_no);
			holder.friend_invite_state = (TextView) convertView.findViewById(R.id.friend_invite_state);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.button_yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				acceptInvitation(holder.button_yes, msg);
				accepOrRefuseInvitationOur(String.valueOf(msg.getId()),"1");
			}
		});

		holder.button_no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				refusenvitation(holder.button_no, msg);
				accepOrRefuseInvitationOur(String.valueOf(msg.getId()),"2");
				
			}
		});
		holder.friend_invite_user_name.setText(msg.getFrom());
		holder.friend_invite_comment.setText("备注：" + msg.getReason());
		if (msg.getStatus() == InviteMesageStatus.AGREED) {
			holder.friend_invite_button_layout.setVisibility(View.GONE);
			holder.friend_invite_state.setVisibility(View.VISIBLE);
			holder.friend_invite_state.setText("已同意");
			Log.e("test", "已同" + holder.friend_invite_state.getText()); 
		} else if (msg.getStatus() == InviteMesageStatus.REFUSED) {
			holder.friend_invite_button_layout.setVisibility(View.GONE);
			holder.friend_invite_state.setVisibility(View.VISIBLE);
			holder.friend_invite_state.setText("已拒绝");
			Log.e("test", "" + token);
		}
		notifyDataSetChanged();

		return convertView;
	}

	class ViewHolder {

		ImageView friend_invite_user_logo;
		TextView friend_invite_user_name;
		TextView friend_invite_info;
		TextView friend_invite_comment;
		LinearLayout friend_invite_button_layout;
		Button button_yes;
		Button button_no;
		TextView friend_invite_state;
	}

	/**
	 * 同意好友请求或者群申请
	 * 
	 * @param button
	 * @param username
	 */
	private void acceptInvitation(final Button button, final InviteMessage msg) {
		final ProgressDialog pd = new ProgressDialog(mcontext);
		String str1 = mcontext.getResources().getString(R.string.Are_agree_with);
		final String str2 = mcontext.getResources().getString(R.string.Has_agreed_to);
		final String str3 = mcontext.getResources().getString(R.string.Agree_with_failure);
		pd.setMessage(str1);
		pd.setCanceledOnTouchOutside(false);
		pd.show();

		new Thread(new Runnable() {
			public void run() {
				// 调用sdk的同意方法
				try {
					if (msg.getGroupId() == null) // 同意好友请求
						EMChatManager.getInstance().acceptInvitation(msg.getFrom());
					else
						// 同意加群申请
						EMGroupManager.getInstance().acceptApplication(msg.getFrom(), msg.getGroupId());
					((Activity) mcontext).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							button.setText(str2);
							msg.setStatus(InviteMesageStatus.AGREED);
							// 更新db
							ContentValues values = new ContentValues();
							values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
							messgeDao.updateMessage(msg.getId(), values);
							button.setBackgroundDrawable(null);
							button.setEnabled(false);

						}
					});
				} catch (final Exception e) {
					((Activity) mcontext).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							Toast.makeText(mcontext, str3 + e.getMessage(), 1).show();
						}
					});

				}
			}
		}).start();
	}

	private void accepOrRefuseInvitationOur(String message_id, String action) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		String url = UrlUtils.postUrl + UrlUtils.path_friendAction;
		params.addBodyParameter("token", token);
		params.addBodyParameter("message_id", message_id);
		params.addBodyParameter("action", action);
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				Log.e("test", "result"+responseInfo.result);
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(result);
					Gson gson = new Gson();
					java.lang.reflect.Type type = new TypeToken<Object>() {
					}.getType();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
					} else {

					}
					Log.e("hello", "+++++" + jsonObj.getString("msg"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 拒绝好友请求或者群申请
	 * 
	 * @param button
	 * @param username
	 */
	private void refusenvitation(final Button button, final InviteMessage msg) {
		final ProgressDialog pd = new ProgressDialog(mcontext);
		String str1 = mcontext.getResources().getString(R.string.Are_refuse_with);
		final String str2 = mcontext.getResources().getString(R.string.Has_refused_to);
		final String str3 = mcontext.getResources().getString(R.string.Refuse_with_failure);
		pd.setMessage(str1);
		pd.setCanceledOnTouchOutside(false);
		pd.show();

		new Thread(new Runnable() {
			public void run() {
				// 调用sdk的同意方法
				try {
					if (msg.getGroupId() == null) // 同意好友请求
						EMChatManager.getInstance().refuseInvitation(msg.getFrom());
					else
						// 同意加群申请
						EMGroupManager.getInstance().acceptApplication(msg.getFrom(), msg.getGroupId());
					((Activity) mcontext).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							button.setText(str2);
							msg.setStatus(InviteMesageStatus.REFUSED);
							// 更新db
							ContentValues values = new ContentValues();
							values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
							messgeDao.updateMessage(msg.getId(), values);
							button.setBackgroundDrawable(null);
							button.setEnabled(false);

						}
					});
				} catch (final Exception e) {
					((Activity) mcontext).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							Toast.makeText(mcontext, str3 + e.getMessage(), 1).show();
						}
					});

				}
			}
		}).start();
	}
}
