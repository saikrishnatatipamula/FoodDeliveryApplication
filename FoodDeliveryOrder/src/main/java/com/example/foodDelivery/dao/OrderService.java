package com.example.foodDelivery.dao;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.foodDelivery.Utils.OrderUtils;
import com.example.foodDelivery.configuration.AppConfig;
import com.example.foodDelivery.model.OrderDetails;
import com.example.foodDelivery.model.Transaction;
import com.example.foodDelivery.repository.OrderRepository;

@Service
public class OrderService
{
	@Autowired
	private OrderRepository orderrepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private AppConfig appConfig;

	
	public OrderDetails placeOrder(OrderDetails orderDetails)
	{
		orderDetails.setStatus(OrderUtils.orderStatus.INITIATED.toString());
		orderDetails.setOrderPlacedTime(LocalDateTime.now());
		orderDetails.setTransactionId(null);
		System.out.println("orderDetails before save: " + orderDetails);
		
		orderDetails = orderrepository.save(orderDetails);          // Saving order initially
		System.out.println("orderDetails after save: " + orderDetails);

		// Calculate amount = (itemAmount * quantity) + charges
		double amount = (orderDetails.getItemAmount()*orderDetails.getItemQuantity()) + orderDetails.getCharges();
		
		System.out.println("orderAmount: " + amount);
		// Make a API call to /doTransaction  - userId, amount     (Returns transactionId)
		
		Transaction request = new Transaction();
		request.setUserId(orderDetails.getUserId());
		request.setAmount(amount);
		ResponseEntity<Transaction> transactionResponse
		= restTemplate.postForEntity(appConfig.doTransactionURL, request, Transaction.class);
		
		System.out.println("Transaction created: " + transactionResponse);
		System.out.println("Url : "+ appConfig.doTransactionURL);
		
		if (transactionResponse.getBody().getStatus().equals(OrderUtils.TransactionStatus.FAILED.toString())) {
			orderDetails.setStatus(OrderUtils.orderStatus.FAILED.toString());
			
		} else if (transactionResponse.getBody().getStatus().equals(OrderUtils.TransactionStatus.SUCCESS.toString())){
			orderDetails.setStatus(OrderUtils.orderStatus.COMPLETED.toString());
			
		} else {
			
		}
		orderDetails.setTransactionId(transactionResponse.getBody().getTransactionId());
		orderDetails.setComments(transactionResponse.getBody().getDescription());
		orderDetails =  orderrepository.save(orderDetails);
		
		return orderDetails;
	}

	public String confirmOrder(String transactionId) {
		OrderDetails orderDetails = orderrepository.findByTransactionId(Long.valueOf(transactionId));
		orderDetails.setStatus(OrderUtils.orderStatus.ORDER_PLACED.toString());
		orderDetails.setComments("Transaction Successfull");
		orderrepository.save(orderDetails);
		return "Order confirmed!";
	}
}
