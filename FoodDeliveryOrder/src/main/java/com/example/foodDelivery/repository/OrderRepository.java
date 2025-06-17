package com.example.foodDelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.foodDelivery.model.Address;
import com.example.foodDelivery.model.OrderDetails;

public interface OrderRepository extends JpaRepository<OrderDetails, Long>
{
	OrderDetails findByTransactionId(long transactionID);
	
	@Query("SELECT SUM(o.itemAmount * o.itemQuantity) FROM OrderDetails o WHERE o.status = 'INITIATED'")
	Double getTotalInitiatedAmount();

}
