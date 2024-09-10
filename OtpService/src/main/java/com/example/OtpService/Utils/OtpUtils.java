package com.example.OtpService.Utils;

import java.time.LocalDateTime;
import java.util.Random;

public class OtpUtils {

	public static final int FIVE_MINUTES = 5;
	public static final int TEN_MINUTES = 10;
	
	public enum OtpTypes {
		REGISTRATION, TRANSACTION
	}
	    
	public static String generateOtp() {
		return String.valueOf(new Random().nextInt(999999));
	}
	public static LocalDateTime generateExpiry(int expiry) {
	
		if (expiry <= 0) {
			return LocalDateTime.now().plusMinutes(FIVE_MINUTES);
		}
		return LocalDateTime.now().plusMinutes(expiry);
	}
}
