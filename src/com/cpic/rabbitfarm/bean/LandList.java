package com.cpic.rabbitfarm.bean;

import java.util.ArrayList;

public class LandList {

	private int code;
	private String msg;
	private ArrayList<LandListInfo> data;
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
	public ArrayList<LandListInfo> getData() {
		return data;
	}
	public void setData(ArrayList<LandListInfo> data) {
		this.data = data;
	}
	public LandList(int code, String msg, ArrayList<LandListInfo> data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	public LandList() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "LandList [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}
}
