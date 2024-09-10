package com.example.OtpService.model;

import java.time.LocalDateTime;

import com.example.OtpService.Utils.OtpUtils;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String otpCode;
    private String email;
    private OtpUtils.OtpTypes type;
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
	public OtpUtils.OtpTypes getType() {
		return type;
	}
	public void setType(OtpUtils.OtpTypes type) {
		this.type = type;
	}
	public LocalDateTime getExpirationTime() {
		return expirationTime;
	}
	public void setExpirationTime(LocalDateTime expirationTime) {
		this.expirationTime = expirationTime;
	}
	@Override
	public String toString() {
		return "Otp [id=" + id + ", otpCode=" + otpCode + ", email=" + email + ", type=" + type + ", expirationTime="
				+ expirationTime + "]";
	}
}
