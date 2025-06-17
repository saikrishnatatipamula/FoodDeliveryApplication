package com.example.foodDelivery.controller.response;

import lombok.Data;

@Data
public class PlaceOrderResponse {

	private long userId;
	private long orderId;
	private Long transactionId;
	private Double amount;
	private String status;
	private String comments;
}
