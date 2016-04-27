package com.cpic.rabbitfarm.utils;

import com.alibaba.fastjson.JSONObject;
import com.cpic.rabbitfarm.bean.EaseUser;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class GetUserInfo {

	private HttpUtils post;
	private RequestParams params;
	public String user_id;

	/**
	 * 根据环信ID获取用户信息
	 * 
	 * @param msg
	 */

	public String loadUserInfo(String ease_user) {
		post = new HttpUtils();
		params = new RequestParams();
		String url = UrlUtils.postUrl + UrlUtils.path_getEaseInfo;
		params.addBodyParameter("users", ease_user);
		post.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				EaseUser user = JSONObject.parseObject(arg0.result, EaseUser.class);
				int code = user.getCode();
				if (code == 1) {
					user_id = user.getData().get(0).getUser_id();
				} else {
				}
			}
		});
		return user_id;
	}

}
