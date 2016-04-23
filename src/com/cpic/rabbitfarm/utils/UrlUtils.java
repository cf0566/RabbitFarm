package com.cpic.rabbitfarm.utils;

/**
 * 兔窝农场Url
 * @author MBENBEN
 *
 */
public class UrlUtils {
	//请求头
	public static final String postUrl="http://wx.cpioc.com/rf/index.php?m=Api&c=api&a=";
	//未读消息数量
	public static final String path_getMessageCount="getMessageCount";
	//阅读
	public static final String path_read="read";
	//环信获取用户ID:
	public static final String path_getEaseInfo="getEaseInfo";
	//三方登录
	public static final String path_oauth="oauth";
	//注册接口:
	public static final String path_register="register";
	//登录接口:
	public static final String path_login="login";
	//获取验证码:
	public static final String path_getCode ="getCode";
	//忘记密码:
	public static final String path_forgotPwd ="forgotPwd";
	//初始化接口: 
	public static final String path_init="init";
	//土地状态:
	public static final String path_landList="landList";
	//好友列表:
	public static final String path_friendList="friendList";
	//在线用户:
	public static final String path_onlineList="onlineList";
	//添加好友:
	public static final String path_addFriend="addFriend";
	//同意拒绝好友申请:
	public static final String path_friendAction="friendAction";
	//更改用户信息:
	public static final String path_modifyUserInfo="modifyUserInfo";
	//设置支付密码: 
	public static final String path_setPayCode="setPayCode";
	//更改用户头像: 
	public static final String path_uploadUserImg="uploadUserImg";
	//储藏室:  
	public static final String path_storeroomList="storeroomList";
	//种植: 
	public static final String path_plant="plant";
	//除草接口:
	public static final String path_weeding="weeding";
	//施肥接口:
	public static final String path_fertilization="fertilization";
	//商品列表:
	public static final String path_goodsList="goodsList";
	//购买商品:
	public static final String path_buyGoods="buyGoods";
	//支付商品:
	public static final String path_payGoods="payGoods";
	//购买兔币:
	public static final String path_buyCurrency="buyCurrency";
	//兔币订单支付:
	public static final String path_orderPay="orderPay";
	//订单列表:
	public static final String path_orderList="orderList";
	//活动列表: 
	public static final String path_activityList="activityList";
	//消息列表:
	public static final String path_messageList="messageList";
	//订单删除:
	public static final String path_orderRemove="orderRemove";
}
