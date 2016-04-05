package com.cpic.rabbitfarm.bean;

import java.util.ArrayList;

public class Seed {
	
	private int code;
	private String msg;
	private ArrayList<SeedInfo> data;
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
	public Seed() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ArrayList<SeedInfo> getData() {
		return data;
	}
	public void setData(ArrayList<SeedInfo> data) {
		this.data = data;
	}
	public Seed(int code, String msg, ArrayList<SeedInfo> data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	@Override
	public String toString() {
		return "Seed [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}
}
