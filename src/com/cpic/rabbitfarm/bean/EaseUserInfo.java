package com.cpic.rabbitfarm.bean;


public class EaseUserInfo {
	
	private String user_id;
	private String user_no;
	private String alias_name;
	private String ease_user;
	private String level;
	private String user_img;
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_no() {
		return user_no;
	}
	public void setUser_no(String user_no) {
		this.user_no = user_no;
	}
	public String getAlias_name() {
		return alias_name;
	}
	public void setAlias_name(String alias_name) {
		this.alias_name = alias_name;
	}
	public String getEase_user() {
		return ease_user;
	}
	public void setEase_user(String ease_user) {
		this.ease_user = ease_user;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getUser_img() {
		return user_img;
	}
	public void setUser_img(String user_img) {
		this.user_img = user_img;
	}
	public EaseUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public EaseUserInfo(String user_id, String user_no, String alias_name, String ease_user, String level,
			String user_img) {
		super();
		this.user_id = user_id;
		this.user_no = user_no;
		this.alias_name = alias_name;
		this.ease_user = ease_user;
		this.level = level;
		this.user_img = user_img;
	}
	@Override
	public String toString() {
		return "EaseUserInfo [user_id=" + user_id + ", user_no=" + user_no + ", alias_name=" + alias_name
				+ ", ease_user=" + ease_user + ", level=" + level + ", user_img=" + user_img + "]";
	}
	
}
