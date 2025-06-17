package com.example.foodDelivery.dao;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.foodDelivery.Kafka.KafkaProducer;
import com.example.foodDelivery.Utils.OrderUtils;
import com.example.foodDelivery.configuration.AppConfig;
import com.example.foodDelivery.controller.request.PlaceOrderRequest;
import com.example.foodDelivery.controller.response.PlaceOrderResponse;
import com.example.foodDelivery.model.Address;
import com.example.foodDelivery.model.OrderDetails;
import com.example.foodDelivery.model.Transaction;
import com.example.foodDelivery.repository.OrderRepository;

@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private AppConfig appConfig;

	@Autowired
	private KafkaProducer kafkaProducer;

	@Value("${user.service.getUser.url}")
	public String getUserUrl;
	
	@Value("${user.service.isUserConfirmed.url}")
	public String isUserConfirmedUrl;

	public PlaceOrderResponse placeOrder(PlaceOrderRequest request) {
		PlaceOrderResponse response = new PlaceOrderResponse();
		response.setUserId(request.getUserId());
//		TODO - Add all required validations
		if (request.getUserId() == 0L) {
			response.setStatus(OrderUtils.orderStatus.DECLINED.toString());
			response.setComments("Unables to place order because user is not valid");
			kafkaProducer.sendMessage("Unable to place Order because UserId is invalid");
			return response;
		}
//		TODO - Add all required validations
		
		
		OrderDetails orderDetails = new OrderDetails();
		orderDetails.setUserId(request.getUserId());
		
		Address address = new Address();
		address.setId(request.getAddressId());
		orderDetails.setAddress(address);
		orderDetails.setItemName(request.getItemName());
		orderDetails.setItemAmount(request.getItemAmount());
		orderDetails.setItemQuantity(request.getItemQuantity());
		orderDetails.setCharges(request.getCharges());
		orderDetails.setStatus(OrderUtils.orderStatus.REQUESTED.toString());
		
		System.out.println("Order Details Before Save : "+ orderDetails);
		
		orderRepository.save(orderDetails);
		
		System.out.println("Order Details after Save : "+ orderDetails);
		
		response.setOrderId(orderDetails.getOrderId());
		
		
//		TODO - Expose a NEW API in user service to get if isUserEligibleToPlaceOrder instead complete User data
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(isUserConfirmedUrl).queryParam("userId",orderDetails.getUserId());
		ResponseEntity<Boolean> userResponse = restTemplate.getForEntity(builder.toUriString(), Boolean.class);
		Boolean isUserConfirmed = userResponse.getBody();
		System.out.println("User confirmed status : " + isUserConfirmed);
		if (!isUserConfirmed) {
			orderDetails.setStatus(OrderUtils.orderStatus.FAILED.toString());
			orderDetails.setComments("User Is Not Confirmed");
			orderRepository.save(orderDetails);
			
			response.setStatus(OrderUtils.orderStatus.FAILED.toString());
			response.setComments("User Is Not Confirmed");
			kafkaProducer.sendMessage("Order Failed because User is Not Verified");

			return response;
		}
//		TODO - Expose a NEW API in user service to get only confirmed status instead complete User data
	
		
		

// 		Make a API call to /doTransaction - userId, amount (Returns transactionId with status)
		
		double amount = (orderDetails.getItemAmount() * orderDetails.getItemQuantity()) + orderDetails.getCharges();

		Transaction transactionRequest = new Transaction();
		transactionRequest.setUserId(orderDetails.getUserId());
		transactionRequest.setAmount(amount);

		ResponseEntity<Transaction> transactionResponse = restTemplate.postForEntity(appConfig.doTransactionURL,
				transactionRequest, Transaction.class);
		System.out.println("Transaction response: " + transactionResponse);
		
		Transaction transaction = transactionResponse.getBody();
		
		if (transaction.getStatus().equals(OrderUtils.TransactionStatus.PENDING.toString())) {
			orderDetails.setStatus(OrderUtils.orderStatus.PAYMENT_INITIATED.toString());
			response.setStatus(orderDetails.getStatus());
			response.setComments("Order Initiated");
		} else if (transaction.getStatus().equals(OrderUtils.TransactionStatus.SUCCESS.toString())) {
			orderDetails.setStatus(OrderUtils.orderStatus.ORDER_PLACED.toString());
			response.setStatus(orderDetails.getStatus());
			response.setComments("Order Placed SuccessFully");
			
		} else {
			orderDetails.setStatus(OrderUtils.orderStatus.FAILED.toString());
		}
		orderDetails.setTransactionId(transaction.getTransactionId());
		orderDetails.setComments(transaction.getDescription());
		orderRepository.save(orderDetails);
		
		response.setAmount(amount);
		response.setTransactionId(transaction.getTransactionId());
		
		System.out.println("Transaction Response  before Return: " + response);
		return response;
	}

	public String confirmOrder(String transactionId) {
		System.out.println("Enterd Confirm Order Service");
		try {
			OrderDetails orderDetails = orderRepository.findByTransactionId(Long.valueOf(transactionId));
			
			orderDetails.setStatus(OrderUtils.orderStatus.ORDER_PLACED.toString());
			orderDetails.setOrderPlacedTime(LocalDateTime.now());
			orderDetails.setComments("Transaction Successfull");
			orderRepository.save(orderDetails);
			
			System.out.println("After Save of order Details : " + orderDetails);

			kafkaProducer.sendMessage("Your Order is Confirmed Successfully : ");
		} catch (Exception e) {
			
			System.out.println("Exception in Confirm Order");
		}

		return "Order confirmed!";
	}

	public ResponseEntity<OrderDetails> getOrderDetailsByOrderId(long orderId) {
		Optional<OrderDetails> optionalResponse = orderRepository.findById(orderId);
		if (optionalResponse.isPresent()) {
			return new ResponseEntity<OrderDetails>(optionalResponse.get(), HttpStatus.OK);

		}
		return new ResponseEntity<OrderDetails>(HttpStatus.NOT_FOUND);
	}

	
}
