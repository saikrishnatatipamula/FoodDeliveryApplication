package com.example.foodDelivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.foodDelivery.controller.request.PlaceOrderRequest;
import com.example.foodDelivery.controller.response.PlaceOrderResponse;
import com.example.foodDelivery.dao.OrderService;
import com.example.foodDelivery.model.OrderDetails;

@RestController
@RequestMapping("/api")
public class OrderController {
	@Autowired
	private OrderService orderService;

	@PostMapping("/placeOrder")
	public PlaceOrderResponse placeOrder(@RequestBody PlaceOrderRequest request) {
		System.out.println("Incoming request: " + request);
		return orderService.placeOrder(request);
	}

	@PostMapping("/confirmOrder")
	public ResponseEntity<String> confirmOrder(@RequestParam String transactionId) {
		String confirmOrderResponse = null;
		try {
			System.out.println("Entered controller Confirm Order Method");
			confirmOrderResponse = orderService.confirmOrder(transactionId);
		} catch (Exception e) {
			System.out.println("Exception in ConfirmOrder Controller");
		}
		return ResponseEntity.ok(confirmOrderResponse);
	}

	@GetMapping("/getOrderDetailsByOrderId")
	public ResponseEntity<OrderDetails> getOrderDetailsByOrderId(@RequestParam long orderId) {
		ResponseEntity<OrderDetails> response = orderService.getOrderDetailsByOrderId(orderId);
		return response;
	}

}
