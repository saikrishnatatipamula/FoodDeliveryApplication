package com.example.foodDelivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.foodDelivery.dao.OrderService;
import com.example.foodDelivery.model.OrderDetails;

@RestController
@RequestMapping("/api")
public class OrderController
{
	@Autowired
	private OrderService orderService;
	
	@PostMapping("/placeOrder")
	public OrderDetails placeOrder(@RequestBody OrderDetails orderDetails)
	{
		System.out.println("Incoming request: " + orderDetails);
		return orderService.placeOrder(orderDetails);
	}
	
    @PostMapping("/confirmOrder")
    public ResponseEntity<String> confirmOrder(@RequestParam String transactionId) {
        String confirmOrderResponse = orderService.confirmOrder(transactionId);
        return ResponseEntity.ok(confirmOrderResponse);
    }
    
	
}
