package com.example.Payment.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Transaction
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long transactionId;
	private long userId;
	private double amount;
	private String description;
	private String status;

	
	@Column(unique = true, nullable = true) // Ensures the email column is unique
	private Long authenticationId;

	public long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getAuthenticationId() {
		return authenticationId;
	}
	public void setAuthenticationId(Long authenticationId) {
		this.authenticationId = authenticationId;
	}
	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", userId=" + userId + ", amount=" + amount
				+ ", description=" + description + ", status=" + status + ", authenticationId=" + authenticationId
				+ "]";
	}
}
