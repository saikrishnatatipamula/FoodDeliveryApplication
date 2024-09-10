package com.example.Payment.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Payment.Service.PaymentService;
import com.example.Payment.model.Transaction;

@RestController
@RequestMapping("/api")
public class PaymentController
{
	@Autowired
	private PaymentService paymentservice;
	
	@PostMapping("/doTransaction")
	public Transaction doTransaction(@RequestBody Transaction transaction)
	{
		System.out.println("Entered doTransactionapi");
		return paymentservice.processTransaction(transaction);
	}
	
	  @PostMapping("/confirmTransaction")
	    public ResponseEntity<String> confirmTransaction(@RequestParam long otpId)
	  {
	        String confirmTransaction = paymentservice.confirmTransaction(otpId);
	        return ResponseEntity.ok(confirmTransaction);
	    }
    
	
	
	
}
