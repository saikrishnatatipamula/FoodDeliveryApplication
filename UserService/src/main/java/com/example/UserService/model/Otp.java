package com.example.UserService.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;



public class Otp {

    private Long id;

    private String otpCode;
    private String email;
    private LocalDateTime expirationTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOtpCode() {
		return otpCode;
	}
	public void setOtpCode(String otpCode) {
		this.otpCode = otpCode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public LocalDateTime getExpirationTime() {
		return expirationTime;
	}
	public void setExpirationTime(LocalDateTime expirationTime) {
		this.expirationTime = expirationTime;
	}
	@Override
	public String toString() {
		return "Otp [id=" + id + ", otpCode=" + otpCode + ", email=" + email + ", expirationTime=" + expirationTime
				+ "]";
	}
    
    
    
}
