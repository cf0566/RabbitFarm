package com.cpic.rabbitfarm.bean;

import java.util.ArrayList;

public class ZhuanjiaInfo {

	private int code;
	private String msg;
	private ArrayList<ZhuanjiaData> data;
	public ZhuanjiaInfo() {
		super();
		// TODO Auto-generated constructor stub
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
	public ArrayList<ZhuanjiaData> getData() {
		return data;
	}
	public void setData(ArrayList<ZhuanjiaData> data) {
		this.data = data;
	}
	public ZhuanjiaInfo(int code, String msg, ArrayList<ZhuanjiaData> data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	@Override
	public String toString() {
		return "ZhuanjiaInfo [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}
	
}
