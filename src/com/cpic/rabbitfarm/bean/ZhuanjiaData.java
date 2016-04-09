package com.cpic.rabbitfarm.bean;

public class ZhuanjiaData {
	
	private String message_id;
	private String message_type;
	private String message_content;
	private String message_img;
	private String owner_id;
	private String create_user;
	private String operation;
	private String is_del;
	private String create_time;
	private String update_time;
	public String getMessage_id() {
		return message_id;
	}
	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}
	public String getMessage_type() {
		return message_type;
	}
	public void setMessage_type(String message_type) {
		this.message_type = message_type;
	}
	public String getMessage_content() {
		return message_content;
	}
	public void setMessage_content(String message_content) {
		this.message_content = message_content;
	}
	public String getMessage_img() {
		return message_img;
	}
	public void setMessage_img(String message_img) {
		this.message_img = message_img;
	}
	public String getOwner_id() {
		return owner_id;
	}
	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}
	public String getCreate_user() {
		return create_user;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getIs_del() {
		return is_del;
	}
	public void setIs_del(String is_del) {
		this.is_del = is_del;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public ZhuanjiaData() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ZhuanjiaData(String message_id, String message_type, String message_content, String message_img,
			String owner_id, String create_user, String operation, String is_del, String create_time,
			String update_time) {
		super();
		this.message_id = message_id;
		this.message_type = message_type;
		this.message_content = message_content;
		this.message_img = message_img;
		this.owner_id = owner_id;
		this.create_user = create_user;
		this.operation = operation;
		this.is_del = is_del;
		this.create_time = create_time;
		this.update_time = update_time;
	}
	@Override
	public String toString() {
		return "ZhuanjiaData [message_id=" + message_id + ", message_type=" + message_type + ", message_content="
				+ message_content + ", message_img=" + message_img + ", owner_id=" + owner_id + ", create_user="
				+ create_user + ", operation=" + operation + ", is_del=" + is_del + ", create_time=" + create_time
				+ ", update_time=" + update_time + "]";
	}
	
}
