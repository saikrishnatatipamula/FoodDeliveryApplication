package com.example.OtpService.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.OtpService.Repository.OtpRepository;
import com.example.OtpService.Utils.OtpUtils;
import com.example.OtpService.Utils.OtpUtils.OtpTypes;
import com.example.OtpService.model.Otp;

@Service
public class OtpService {

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
        return otpRepository.save(otp);
    }

    public String validateOTP(long otpId, String otpCode) {
        
    	Optional<Otp> otpOptional = otpRepository.findById(otpId);
    	Otp otpResponse = otpOptional.get();
    	
    	System.out.println("Otp :"+otpResponse);
        
        if (otpResponse == null)
        {
            return "Otp Not found for Id : "+ otpId;
        }
        else if (otpResponse.getOtpCode().equals(otpCode)&& otpResponse.getExpirationTime().isAfter(LocalDateTime.now()))
        {
        	if (otpResponse.getType().toString().equalsIgnoreCase(OtpUtils.OtpTypes.REGISTRATION.toString())) {
        		//String confirmedUserUrl = userServiceBaseUrl +"/confirmUser/"+otp.getEmail();
        		
        		 UriComponentsBuilder uriBuilder3 = UriComponentsBuilder.fromHttpUrl(confirmUserServiceUrl)
        	                .queryParam("email", otpResponse.getEmail());
  
        	//	ResponseEntity<String> confirmUserResponse = restTemplate.postForEntity(confirmedUserUrl, null, String.class);
        		 
        		 ResponseEntity<String> confirmUserResponse=restTemplate.postForEntity(uriBuilder3.build().toUri(), null, String.class);
        		return "Otp verified Successfully For Registration: "+ otpResponse.getEmail();
        		
        	} else if (otpResponse.getType().toString().equalsIgnoreCase(OtpUtils.OtpTypes.TRANSACTION.toString())){
        		
        		//String confirmedUserUrl = paymentServiceBaseUrl +"/confirmTransaction/"+otpResponse.getId();
        		
        		UriComponentsBuilder uriBuilder4 = UriComponentsBuilder.fromHttpUrl(confirmTransactionUrl)
                        .queryParam("otpId", otpResponse.getId());

        		  
        		//ResponseEntity<String> confirmUserResponse = restTemplate.postForEntity(confirmedUserUrl, null, String.class);
        		ResponseEntity<String> confirmUserResponse =restTemplate.postForEntity(uriBuilder4.build().toUri(),null,String.class);
        		return "Otp verified Successfully For Transaction";
        		
        		
				
			} else {
				return "Invalid OtpType";
			}
        		
        }
        else if (!otpResponse.getOtpCode().equals(otpCode))
        {
        	return "Otp Doesnot Match";
        }
        else if(!otpResponse.getExpirationTime().isAfter(LocalDateTime.now()))
        {
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
		return "OTP regenerated successfully. OTP: " + regeneratedOtp.getOtpCode();
	}
	
}
