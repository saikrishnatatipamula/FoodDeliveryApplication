package com.example.OtpService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.OtpService.Utils.OtpUtils;
import com.example.OtpService.model.Otp;
import com.example.OtpService.service.OtpService;

@RestController
@RequestMapping("/api")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @PostMapping("/createOTP")
    public ResponseEntity<Otp> createOTP(@RequestParam String email, @RequestParam OtpUtils.OtpTypes otpType) 
    {
    	System.out.println("Entered inside api");
        Otp createdOTP = otpService.createOTP(email, otpType);
        System.out.println("created Otp  :" + createdOTP);
       
        return ResponseEntity.ok(createdOTP);
    }

    @PostMapping("/validateOTP")
    public ResponseEntity<String> validateOTP(@RequestParam long otpId, @RequestParam String otpCode) {
    	
    	System.out.println("Entered Controller");
        String isValid = otpService.validateOTP(otpId, otpCode);
        return ResponseEntity.ok(isValid);
    }
    
    @PostMapping("/regenarateOtp")
    public ResponseEntity<String> regenarateOtp(@RequestParam String email, @RequestParam int expiryInMinutes) {
        String confirmedUser = otpService.regenarateOtp(email, expiryInMinutes);
        return ResponseEntity.ok(confirmedUser);
    }
}
