package com.example.foodDelivery.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Orders")
public class OrderDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	private long userId;

	@Column(unique = true, nullable = true)
	private Long transactionId;

	private String itemName;
	private double itemAmount;
	private int itemQuantity;
	private double charges;
	private String status;
	private String comments;
	private LocalDateTime orderPlacedTime;
	
	@ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public double getItemAmount() {
		return itemAmount;
	}

	public void setItemAmount(double itemAmount) {
		this.itemAmount = itemAmount;
	}

	public int getItemQuantity() {
		return itemQuantity;
	}

	public void setItemQuantity(int itemQuantity) {
		this.itemQuantity = itemQuantity;
	}

	public double getCharges() {
		return charges;
	}

	public void setCharges(double charges) {
		this.charges = charges;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public LocalDateTime getOrderPlacedTime() {
		return orderPlacedTime;
	}

	public void setOrderPlacedTime(LocalDateTime orderPlacedTime) {
		this.orderPlacedTime = orderPlacedTime;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "OrderDetails [orderId=" + orderId + ", userId=" + userId + ", transactionId=" + transactionId
				+ ", itemName=" + itemName + ", itemAmount=" + itemAmount + ", itemQuantity=" + itemQuantity
				+ ", charges=" + charges + ", status=" + status + ", comments=" + comments + ", orderPlacedTime="
				+ orderPlacedTime + ", address=" + address.getAddress() + "]";
	}

	
	
	
	
	

}