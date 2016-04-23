package com.cpic.rabbitfarm.bean;

import java.util.ArrayList;

public class EaseUser {

	private int code;
	private String msg;
	private ArrayList<EaseUserInfo> data;
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
	public ArrayList<EaseUserInfo> getData() {
		return data;
	}
	public void setData(ArrayList<EaseUserInfo> data) {
		this.data = data;
	}
	public EaseUser() {
		super();
		// TODO Auto-generated constructor stub
	}
	public EaseUser(int code, String msg, ArrayList<EaseUserInfo> data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	@Override
	public String toString() {
		return "EaseUser [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}
	
}
