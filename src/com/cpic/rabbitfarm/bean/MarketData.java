package com.cpic.rabbitfarm.bean;

public class MarketData {

	private String goods_id;
	private String goods_no;
	private String category_id;
	private String goods_name;
	private String goods_price;
	private String goods_img;
	private String is_online;
	private String unit;
	private String inventory;
	private String sales;
	private String is_promote;
	private String promete_start;
	private String promote_end;
	private String promote_price;
	private String create_time;
	private String is_del;
	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}
	public String getGoods_no() {
		return goods_no;
	}
	public void setGoods_no(String goods_no) {
		this.goods_no = goods_no;
	}
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
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
	public String getGoods_img() {
		return goods_img;
	}
	public void setGoods_img(String goods_img) {
		this.goods_img = goods_img;
	}
	public String getIs_online() {
		return is_online;
	}
	public void setIs_online(String is_online) {
		this.is_online = is_online;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getInventory() {
		return inventory;
	}
	public void setInventory(String inventory) {
		this.inventory = inventory;
	}
	public String getSales() {
		return sales;
	}
	public void setSales(String sales) {
		this.sales = sales;
	}
	public String getIs_promote() {
		return is_promote;
	}
	public void setIs_promote(String is_promote) {
		this.is_promote = is_promote;
	}
	public String getPromete_start() {
		return promete_start;
	}
	public void setPromete_start(String promete_start) {
		this.promete_start = promete_start;
	}
	public String getPromote_end() {
		return promote_end;
	}
	public void setPromote_end(String promote_end) {
		this.promote_end = promote_end;
	}
	public String getPromote_price() {
		return promote_price;
	}
	public void setPromote_price(String promote_price) {
		this.promote_price = promote_price;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getIs_del() {
		return is_del;
	}
	public void setIs_del(String is_del) {
		this.is_del = is_del;
	}
	public MarketData() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MarketData(String goods_id, String goods_no, String category_id, String goods_name, String goods_price,
			String goods_img, String is_online, String unit, String inventory, String sales, String is_promote,
			String promete_start, String promote_end, String promote_price, String create_time, String is_del) {
		super();
		this.goods_id = goods_id;
		this.goods_no = goods_no;
		this.category_id = category_id;
		this.goods_name = goods_name;
		this.goods_price = goods_price;
		this.goods_img = goods_img;
		this.is_online = is_online;
		this.unit = unit;
		this.inventory = inventory;
		this.sales = sales;
		this.is_promote = is_promote;
		this.promete_start = promete_start;
		this.promote_end = promote_end;
		this.promote_price = promote_price;
		this.create_time = create_time;
		this.is_del = is_del;
	}
	@Override
	public String toString() {
		return "MarketData [goods_id=" + goods_id + ", goods_no=" + goods_no + ", category_id=" + category_id
				+ ", goods_name=" + goods_name + ", goods_price=" + goods_price + ", goods_img=" + goods_img
				+ ", is_online=" + is_online + ", unit=" + unit + ", inventory=" + inventory + ", sales=" + sales
				+ ", is_promote=" + is_promote + ", promete_start=" + promete_start + ", promote_end=" + promote_end
				+ ", promote_price=" + promote_price + ", create_time=" + create_time + ", is_del=" + is_del + "]";
	}
	
}
