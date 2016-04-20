package com.cpic.rabbitfarm.bean;

import java.util.ArrayList;

public class ShopGoods {

	private int code;
	private String msg;
	private ArrayList<ShopGoodsData> data;
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
	public ArrayList<ShopGoodsData> getData() {
		return data;
	}
	public void setData(ArrayList<ShopGoodsData> data) {
		this.data = data;
	}
	public ShopGoods() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ShopGoods(int code, String msg, ArrayList<ShopGoodsData> data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	@Override
	public String toString() {
		return "ShopGoods [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}
	
}
