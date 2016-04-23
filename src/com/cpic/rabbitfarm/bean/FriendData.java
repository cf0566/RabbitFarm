package com.cpic.rabbitfarm.bean;

import java.util.List;

public class FriendData {
	
	public String msg;
	public int code;
	public List<Friend> data;
	@Override
	public String toString() {
		return "FriendData [msg=" + msg + ", code=" + code + ", data=" + data + "]";
	}
	

}
