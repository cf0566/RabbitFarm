package com.cpic.rabbitfarm.bean;

import java.util.ArrayList;

public class Market {

	private int code;
	private String msg;
	private ArrayList<MarketData> data;
	public Market() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Market(int code, String msg, ArrayList<MarketData> data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	@Override
	public String toString() {
		return "Market [code=" + code + ", msg=" + msg + ", data=" + data + "]";
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
	public ArrayList<MarketData> getData() {
		return data;
	}
	public void setData(ArrayList<MarketData> data) {
		this.data = data;
	}
	
}
