package com.cpic.rabbitfarm.bean;

public class ActivityData {
	
	private String activity_id;
	private String activity_title;
	private String activity_img;
	private String activity_content;
	private String create_time;
	private String is_online;
	private String from_time;
	private String to_time;
	private String url;
	private int is_read;
	public String getActivity_id() {
		return activity_id;
	}
	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}
	public String getActivity_title() {
		return activity_title;
	}
	public void setActivity_title(String activity_title) {
		this.activity_title = activity_title;
	}
	public String getActivity_img() {
		return activity_img;
	}
	public void setActivity_img(String activity_img) {
		this.activity_img = activity_img;
	}
	public String getActivity_content() {
		return activity_content;
	}
	public void setActivity_content(String activity_content) {
		this.activity_content = activity_content;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getIs_online() {
		return is_online;
	}
	public void setIs_online(String is_online) {
		this.is_online = is_online;
	}
	public String getFrom_time() {
		return from_time;
	}
	public void setFrom_time(String from_time) {
		this.from_time = from_time;
	}
	public String getTo_time() {
		return to_time;
	}
	public void setTo_time(String to_time) {
		this.to_time = to_time;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getIs_read() {
		return is_read;
	}
	public void setIs_read(int is_read) {
		this.is_read = is_read;
	}
	public ActivityData() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "ActivityData [activity_id=" + activity_id + ", activity_title=" + activity_title + ", activity_img="
				+ activity_img + ", activity_content=" + activity_content + ", create_time=" + create_time
				+ ", is_online=" + is_online + ", from_time=" + from_time + ", to_time=" + to_time + ", url=" + url
				+ ", is_read=" + is_read + "]";
	}
	public ActivityData(String activity_id, String activity_title, String activity_img, String activity_content,
			String create_time, String is_online, String from_time, String to_time, String url, int is_read) {
		super();
		this.activity_id = activity_id;
		this.activity_title = activity_title;
		this.activity_img = activity_img;
		this.activity_content = activity_content;
		this.create_time = create_time;
		this.is_online = is_online;
		this.from_time = from_time;
		this.to_time = to_time;
		this.url = url;
		this.is_read = is_read;
	}
	
}
