package com.example.foodDelivery.controller.request;

import lombok.Data;

@Data
public class PlaceOrderRequest {

	private long userId;
	private long addressId;
	private String itemName;
	private double itemAmount;
	private int itemQuantity;
	private double charges;
	
}
