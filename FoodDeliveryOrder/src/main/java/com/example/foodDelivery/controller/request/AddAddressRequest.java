package com.example.foodDelivery.controller.request;

import lombok.Data;

@Data
public class AddAddressRequest
{
	private int userId;
	private String address;
	private String state;
	private Long pincode;
}
