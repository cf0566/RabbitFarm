package com.cpic.rabbitfarm.bean;

import java.util.ArrayList;

public class ActivityInfo {

	private int code;
	private String msg;
	private ArrayList<ActivityData> data;
	public ActivityInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ActivityInfo(int code, String msg, ArrayList<ActivityData> data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	@Override
	public String toString() {
		return "Activity [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}
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
	public ArrayList<ActivityData> getData() {
		return data;
	}
	public void setData(ArrayList<ActivityData> data) {
		this.data = data;
	}
	
}
