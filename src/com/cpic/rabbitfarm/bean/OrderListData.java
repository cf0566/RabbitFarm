package com.cpic.rabbitfarm.bean;

import java.util.ArrayList;

public class OrderListData {
	
	private String order_id;
	private String pay_id;
	private String pay_status;
	private String order_amount;
	private String total;
	private String create_time;
	private ArrayList<OrderListGoods> goods;
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getPay_id() {
		return pay_id;
	}
	public void setPay_id(String pay_id) {
		this.pay_id = pay_id;
	}
	public String getPay_status() {
		return pay_status;
	}
	public void setPay_status(String pay_status) {
		this.pay_status = pay_status;
	}
	public String getOrder_amount() {
		return order_amount;
	}
	public void setOrder_amount(String order_amount) {
		this.order_amount = order_amount;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public ArrayList<OrderListGoods> getGoods() {
		return goods;
	}
	public void setGoods(ArrayList<OrderListGoods> goods) {
		this.goods = goods;
	}
	public OrderListData() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OrderListData(String order_id, String pay_id, String pay_status, String order_amount, String total,
			String create_time, ArrayList<OrderListGoods> goods) {
		super();
		this.order_id = order_id;
		this.pay_id = pay_id;
		this.pay_status = pay_status;
		this.order_amount = order_amount;
		this.total = total;
		this.create_time = create_time;
		this.goods = goods;
	}
	@Override
	public String toString() {
		return "OrderListData [order_id=" + order_id + ", pay_id=" + pay_id + ", pay_status=" + pay_status
				+ ", order_amount=" + order_amount + ", total=" + total + ", create_time=" + create_time + ", goods="
				+ goods + "]";
	}
	

}
