package com.cpic.rabbitfarm.bean;

public class MarketBuy {

	private String goods_id;
	private int count;
	private int price;
	private String goods_name;
	public MarketBuy(String goods_id, int count, int price, String goods_name) {
		super();
		this.goods_id = goods_id;
		this.count = count;
		this.price = price;
		this.goods_name = goods_name;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public MarketBuy() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MarketBuy(String goods_id, int count) {
		super();
		this.goods_id = goods_id;
		this.count = count;
	}
	@Override
	public String toString() {
		return "MarketBuy [goods_id=" + goods_id + ", count=" + count + ", price=" + price + "]";
	}
	
	
}
