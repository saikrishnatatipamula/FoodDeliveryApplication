package com.example.UserService.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;

	private String password;

	@Column(unique = true) // Ensures the email column is unique
	private String email;

	private boolean confirmed;

	@Column(name = "sign_up_time", nullable = false, updatable = false)
	private LocalDateTime signUpTime;

	@PrePersist
	protected void onCreate() {
		this.signUpTime = LocalDateTime.now();
	}

	public LocalDateTime getSignUpTime() {
		return signUpTime;
	}

	public void setSignUpTime(LocalDateTime signUpTime) {
		this.signUpTime = signUpTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email
				+ ", confirmed=" + confirmed + ", signUpTime=" + signUpTime + "]";
	}

}
