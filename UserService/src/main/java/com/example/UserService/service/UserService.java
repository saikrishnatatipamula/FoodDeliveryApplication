package com.example.UserService.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.UserService.model.Otp;
import com.example.UserService.model.User;
import com.example.UserService.repository.UserRepository;
import com.example.UserService.utils.UserUtils;

@Service
public class UserService
{
	@Autowired
	private UserRepository userrepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${otp.service.createOtp.url}")
	private String OtpserviceUrl;
	
    //private final String otpServiceBaseUrl = "http://localhost:8081/api"; // OTPService URL

	
	public String SignUpUser(User user)
	{
		System.out.println("before save: " + user);
		
		User savedUser;
		try {
			savedUser = userrepository.save(user);
		} catch (Exception e) {
			
			String error = "Please check the Email and SignIn Again";
			return error;
		}
		System.out.println("after save: " + user);

		// URL prepared
	//	String createOtpUrl = otpServiceBaseUrl + "/createOTP/" + savedUser.getEmail()  + "?otpType=" + UserUtils.OtpTypes.REGISTRATION.toString();
		System.out.println("createOtpUrl: " + OtpserviceUrl);

		// Make a post call to createOTP API
		//ResponseEntity<Otp> Otpresponse = restTemplate.postForEntity(createOtpUrl, null, Otp.class);
		
		
		ResponseEntity<Otp> Otpresponse =null;
		 UriComponentsBuilder uriBuilder2 = UriComponentsBuilder.fromHttpUrl(OtpserviceUrl)
	                .queryParam("email", savedUser.getEmail())
	                .queryParam("otpType", UserUtils.OtpTypes.REGISTRATION.toString());
		 
		 
		 Otpresponse=restTemplate.postForEntity(uriBuilder2.build().toUri(),null,Otp.class);
		
		System.out.println("otpresponse: "+ Otpresponse);
        
        return "Otp sent to Email successfully :" + savedUser.getEmail() + " : " + "OTP :" + Otpresponse.getBody().getOtpCode();
	}
	
	
	public String confirmUser(String email) {
        User user = userrepository.findByEmail(email);
        if (user != null) {
            user.setConfirmed(true);
            userrepository.save(user);
            return "User Found Sucessfully";
        }
        else
        {
        	return "User Not Found";
        }
        
    }
	
	public ResponseEntity<User> getUser(long userid)
	{
		Optional<User> optionalResponse = userrepository.findById(userid);
		
		if (optionalResponse.isPresent()) {
			return new ResponseEntity<User>(optionalResponse.get(), HttpStatus.OK);
		}
		
		return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
	}
	
}
