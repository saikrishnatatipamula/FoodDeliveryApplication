package com.example.Payment.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Payment.model.Transaction;

public interface PaymentRepository extends JpaRepository<Transaction, Long>
{
	Transaction findByAuthenticationId(long id);	
}
