package com.example.Payment.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.Payment.Repository.PaymentRepository;
import com.example.Payment.Utils.PaymentUtils;
import com.example.Payment.model.Otp;
import com.example.Payment.model.Transaction;
import com.example.Payment.model.User;

@Service
public class PaymentService {
	
	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${create.otp.url}")
	private String createOtpUrl;
	
	@Value("${user.service.getuser.url}")
	private String getUserUrl;
	
	@Value("${confirm.order.url}")
	private String confirmOrderurl;

	//private final String userServiceBaseUrl = "http://localhost:8080/api"; // UserService URL
   	//private final String otpServiceBaseUrl = "http://localhost:8081/api/createOTP"; // OTPService URL
	//private final String orderServiceUrl= "http://localhost:8083/api"; //OrderServiceURL

	public Transaction doTransaction(Transaction transaction) {
		// Make a REST Call
		ResponseEntity<User> userResponse = null;
		String email = null;
		try {
			
			System.out.println("entered doTransaction");
			UriComponentsBuilder uriBuilder1 = UriComponentsBuilder.fromHttpUrl(getUserUrl)
	                .queryParam("userId", transaction.getUserId());
			
			System.out.println("getUserUrl: " + getUserUrl);
			
			userResponse = restTemplate.getForEntity(uriBuilder1.build().toUri(), User.class);
			
			email = userResponse.getBody().getEmail();


			System.out.println("userResponse: " + userResponse);
		} catch (Exception e) {
			
			transaction.setStatus(PaymentUtils.TransactionStatus.FAILED.toString());
			transaction.setDescription("Failed to fetch user info");
			transaction.setAuthenticationId(null);
			
			System.out.println("Transaciton before save in exception: " + transaction);
			transaction = paymentRepository.save(transaction);
			System.out.println("Transaciton after save in exception: " + transaction);

			
			return transaction;
		}
		
		if (userResponse == null || userResponse.getBody() == null || userResponse.getBody().getEmail().isEmpty()) {
			
			transaction.setStatus(PaymentUtils.TransactionStatus.FAILED.toString());
			transaction.setDescription("User not found");
			transaction = paymentRepository.save(transaction);
			return transaction;
		}
		
		// URL prepared
//		String createOtpUrl = otpServiceBaseUrl + "/createOTP" + "?email=" + userResponse.getBody().getEmail()
//				+ "&otpType=" + PaymentUtils.OtpTypes.TRANSACTION.toString();
//		
//		System.out.println("createOtpUrl :" + createOtpUrl);
		
//		
//		// Make a REST call
//		ResponseEntity<Otp> otpResponse = restTemplate.postForEntity(createOtpUrl, null, Otp.class);
		
		
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(createOtpUrl)
                .queryParam("email", email)
                .queryParam("otpType", PaymentUtils.OtpTypes.TRANSACTION.toString());

        
        ResponseEntity<Otp> otpResponse = restTemplate.postForEntity(uriBuilder.build().toUri(), null, Otp.class);
		
		
		if (otpResponse == null || otpResponse.getBody().getId() == null || otpResponse.getBody().getId() == 0) {
			
			transaction.setStatus(PaymentUtils.TransactionStatus.FAILED.toString());
			transaction.setDescription("Unable to create OTP");
			
			return transaction;
		}
		
		transaction.setAuthenticationId(otpResponse.getBody().getId());
		transaction.setDescription("Transaction initiated");
		transaction.setStatus(PaymentUtils.TransactionStatus.PENDING.toString());
		transaction = paymentRepository.save(transaction);
		
		return transaction;
	}

	public String confirmTransaction(long otpId) {
		// TODO Auto-generated method stub
		try {
			
			System.out.println("Entered Try block");
			Transaction transaction = paymentRepository.findByAuthenticationId(otpId);
			
			transaction.setStatus(PaymentUtils.TransactionStatus.SUCCESS.toString());
			transaction.setDescription("Transaction Successful");
			
			System.out.println("Before Save of Transaction : "+ transaction);
			paymentRepository.save(transaction);
			System.out.println("After Save of Transaction : " + transaction);
			
			
			/// Make a call to Order Service to confirmOrder by trasnactionId
			//String getConfirmOrderURL = orderServiceUrl+"/confirmOrder/" + transaction.getTransactionId();
			
			UriComponentsBuilder uriBuilder5 = UriComponentsBuilder.fromHttpUrl(confirmOrderurl)
			        .queryParam("transactionId", transaction.getTransactionId());	
			
			ResponseEntity<String> orderResponse= restTemplate.postForEntity(uriBuilder5.build().toUri(), null, String.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Exception in confirmTransaction: " + e);
			
			return "Exception in confirmtransaction";
		}
		
		
		return "Transaction Successful";
	}
}