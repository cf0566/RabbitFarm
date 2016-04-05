package com.cpic.rabbitfarm.bean;

public class SeedInfo {

	private String store_id;
	private String store_type;
	private String goods_id;
	private String goods_name;
	private String goods_img;
	private String goods_number;
	
	public String getStore_id() {
		return store_id;
	}
	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}
	public String getStore_type() {
		return store_type;
	}
	public void setStore_type(String store_type) {
		this.store_type = store_type;
	}
	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public String getGoods_img() {
		return goods_img;
	}
	public void setGoods_img(String goods_img) {
		this.goods_img = goods_img;
	}
	public String getGoods_number() {
		return goods_number;
	}
	public void setGoods_number(String goods_number) {
		this.goods_number = goods_number;
	}
	public SeedInfo(String store_id, String store_type, String goods_id, String goods_name, String goods_img,
			String goods_number) {
		super();
		this.store_id = store_id;
		this.store_type = store_type;
		this.goods_id = goods_id;
		this.goods_name = goods_name;
		this.goods_img = goods_img;
		this.goods_number = goods_number;
	}
	@Override
	public String toString() {
		return "SeedInfo [store_id=" + store_id + ", store_type=" + store_type + ", goods_id=" + goods_id
				+ ", goods_name=" + goods_name + ", goods_img=" + goods_img + ", goods_number=" + goods_number + "]";
	}
	public SeedInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
