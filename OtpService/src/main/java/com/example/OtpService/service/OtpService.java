package com.example.OtpService.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.OtpService.Kafka.KafkaProducer;
import com.example.OtpService.Repository.OtpRepository;
import com.example.OtpService.Utils.OtpUtils;
import com.example.OtpService.Utils.OtpUtils.OtpTypes;
import com.example.OtpService.model.Otp;

@Service
public class OtpService {
	
	@Autowired
	private KafkaProducer kafkaProducer;

    @Autowired
    private OtpRepository otpRepository;
    
    @Autowired
    private RestTemplate restTemplate;
    
    //private final String userServiceBaseUrl = "http://localhost:8080/api"; // UserService URL
    //private final String paymentServiceBaseUrl = "http://localhost:8082/api"; // Payment Service URL
    
    @Value("${confirm.user.service.url}")
    private String confirmUserServiceUrl;
    
    @Value("${confirmTransaction.service.url}")
    private String confirmTransactionUrl;

    public Otp createOTP(String email, OtpTypes otpType) {
        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setOtpCode(OtpUtils.generateOtp());
        otp.setType(otpType);
        otp.setExpirationTime(OtpUtils.generateExpiry(OtpUtils.FIVE_MINUTES));
        Otp otpCreated = otpRepository.save(otp);
        kafkaProducer.sendMessage("OTP created for email: " + email + " OTP: " + otpCreated.getOtpCode());
        
        return otpCreated;
    }

    public String validateOTP(long otpId, String otpCode) {
        System.out.println("Entered validateOtp Method");
    	Optional<Otp> otpOptional = otpRepository.findById(otpId);
    	Otp otpDetails = otpOptional.get();
    	
    	System.out.println(otpOptional);
        if (otpDetails == null) {
            return "Otp Not found for Id : "+ otpId;
        }
        
        else if (otpDetails.getOtpCode().equals(otpCode) && LocalDateTime.now().isBefore(otpDetails.getExpirationTime()))
        {
        	if (otpDetails.getType().toString().equalsIgnoreCase(OtpUtils.OtpTypes.REGISTRATION.toString())) {
        		//String confirmedUserUrl = userServiceBaseUrl +"/confirmUser/"+otp.getEmail();
        		
        		 UriComponentsBuilder uriBuilder3 = UriComponentsBuilder.fromHttpUrl(confirmUserServiceUrl)
        	                .queryParam("email", otpDetails.getEmail());
  
        	//	ResponseEntity<String> confirmUserResponse = restTemplate.postForEntity(confirmedUserUrl, null, String.class);
        		 
        		 ResponseEntity<String> confirmUserResponse=restTemplate.postForEntity(uriBuilder3.build().toUri(), null, String.class);
        		return "Otp verified Successfully For Registration: "+ otpDetails.getEmail();
        		
        	} else if (otpDetails.getType().toString().equalsIgnoreCase(OtpUtils.OtpTypes.TRANSACTION.toString())){
        		
        		//String confirmedUserUrl = paymentServiceBaseUrl +"/confirmTransaction/"+otpResponse.getId();
        		
        		UriComponentsBuilder uriBuilder4 = UriComponentsBuilder.fromHttpUrl(confirmTransactionUrl)
                        .queryParam("otpId", otpDetails.getId());

        		  
        		//ResponseEntity<String> confirmUserResponse = restTemplate.postForEntity(confirmedUserUrl, null, String.class);
        		ResponseEntity<String> confirmUserResponse = restTemplate.postForEntity(uriBuilder4.build().toUri(),null,String.class);
        		return "Otp verified Successfully For Transaction";
				
			} else {
				return "Invalid OtpType";
			}
        		
        }
        else if (!otpDetails.getOtpCode().equals(otpCode))
        {
        	return "Otp Doesnot Match";
        }
        else if(!otpDetails.getExpirationTime().isAfter(LocalDateTime.now()))
        {
        	kafkaProducer.sendMessage("User OTP expired, please regenerateOTP");
        	return " Otp Expired";
        }
        else
        {
        	return "Otp Not Found";
        }
		
    }

	public String regenarateOtp(String email, int expiryInMinutes)
	{
		Otp fetchedOtp = otpRepository.findByEmail(email);
		fetchedOtp.setOtpCode(OtpUtils.generateOtp());
		fetchedOtp.setExpirationTime(OtpUtils.generateExpiry(expiryInMinutes));
		
		Otp regeneratedOtp = otpRepository.save(fetchedOtp);
		kafkaProducer.sendMessage("OTP regenerated for email: " + email + " OTP: " + regeneratedOtp.getOtpCode());
		return "OTP regenerated successfully. OTP: " + regeneratedOtp.getOtpCode();
	}
	
}
