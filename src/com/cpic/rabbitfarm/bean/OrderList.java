package com.cpic.rabbitfarm.bean;

import java.util.ArrayList;

public class OrderList {

	private int code;
	private String msg;
	private ArrayList<OrderListData> data;
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
	public ArrayList<OrderListData> getData() {
		return data;
	}
	public void setData(ArrayList<OrderListData> data) {
		this.data = data;
	}
	public OrderList() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OrderList(int code, String msg, ArrayList<OrderListData> data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	@Override
	public String toString() {
		return "OrderList [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}
	
}
