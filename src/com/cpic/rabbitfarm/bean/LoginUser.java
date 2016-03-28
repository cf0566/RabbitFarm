package com.cpic.rabbitfarm.bean;

public class LoginUser {
	
	private int code;
	private String msg;
	private LoginUserInfo data;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public LoginUserInfo getData() {
		return data;
	}
	public void setData(LoginUserInfo data) {
		this.data = data;
	}
	public LoginUser() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LoginUser(int code, String msg, LoginUserInfo data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	@Override
	public String toString() {
		return "LoginUser [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}
	
}
