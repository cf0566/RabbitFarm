package com.cpic.rabbitfarm.bean;

public class LandListInfo {

	private String land_id;
	private String goods_name;
	private String in_user;
	private String day;
	private String img;
	private String attr_name;
	public String getLand_id() {
		return land_id;
	}
	public void setLand_id(String land_id) {
		this.land_id = land_id;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public String getIn_user() {
		return in_user;
	}
	public void setIn_user(String in_user) {
		this.in_user = in_user;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getAttr_name() {
		return attr_name;
	}
	public void setAttr_name(String attr_name) {
		this.attr_name = attr_name;
	}
	public LandListInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "LandListInfo [land_id=" + land_id + ", goods_name=" + goods_name + ", in_user=" + in_user + ", day="
				+ day + ", img=" + img + ", attr_name=" + attr_name + "]";
	}
	public LandListInfo(String land_id, String goods_name, String in_user, String day, String img, String attr_name) {
		super();
		this.land_id = land_id;
		this.goods_name = goods_name;
		this.in_user = in_user;
		this.day = day;
		this.img = img;
		this.attr_name = attr_name;
	}
	
	
	
}
