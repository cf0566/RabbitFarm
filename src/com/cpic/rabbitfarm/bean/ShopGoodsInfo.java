package com.cpic.rabbitfarm.bean;

public class ShopGoodsInfo {

	private String goods_id;
	private String store_type;
	private String goods_name;
	private String goods_price;
	private String goods_number;
	private String goods_img;
	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}
	public String getStore_type() {
		return store_type;
	}
	public void setStore_type(String store_type) {
		this.store_type = store_type;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public String getGoods_price() {
		return goods_price;
	}
	public void setGoods_price(String goods_price) {
		this.goods_price = goods_price;
	}
	public String getGoods_number() {
		return goods_number;
	}
	public void setGoods_number(String goods_number) {
		this.goods_number = goods_number;
	}
	public String getGoods_img() {
		return goods_img;
	}
	public void setGoods_img(String goods_img) {
		this.goods_img = goods_img;
	}
	public ShopGoodsInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ShopGoodsInfo(String goods_id, String store_type, String goods_name, String goods_price, String goods_number,
			String goods_img) {
		super();
		this.goods_id = goods_id;
		this.store_type = store_type;
		this.goods_name = goods_name;
		this.goods_price = goods_price;
		this.goods_number = goods_number;
		this.goods_img = goods_img;
	}
	@Override
	public String toString() {
		return "ShopGoodsInfo [goods_id=" + goods_id + ", store_type=" + store_type + ", goods_name=" + goods_name
				+ ", goods_price=" + goods_price + ", goods_number=" + goods_number + ", goods_img=" + goods_img + "]";
	}
	
}
