package com.example.foodDelivery.Utils;

public class OrderUtils 
{
	public enum orderStatus
	{
		REQUESTED, DECLINED, PAYMENT_INITIATED, ORDER_PLACED, USER_CANCELLED, FAILED
	}
	
	public enum TransactionStatus {
		PENDING, DECLINED, SUCCESS, FAILED
	}
	
}
