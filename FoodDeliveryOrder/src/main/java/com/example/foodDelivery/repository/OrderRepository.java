package com.example.foodDelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.foodDelivery.model.OrderDetails;

public interface OrderRepository extends JpaRepository<OrderDetails, Long>
{
	OrderDetails findByTransactionId(long transactionID);

}
