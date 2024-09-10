package com.example.OtpService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.OtpService.model.Otp;

public interface OtpRepository extends JpaRepository<Otp, Long> {

	Otp findByEmail(String email);
}
