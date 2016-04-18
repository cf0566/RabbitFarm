package com.cpic.rabbitfarm.bean;

public class MarketBuy {

	private String goods_id;
	private int count;
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
		return "MarketBuy [goods_id=" + goods_id + ", count=" + count + "]";
	}
	
	
}
