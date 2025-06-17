package com.example.foodDelivery.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Address {
 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private int userId;
	private String address;
	private String state;
	private Long pincode;
	 @OneToMany(mappedBy = "address")
	    private List<OrderDetails> orders;
	 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Long getPincode() {
		return pincode;
	}
	public void setPincode(Long pincode) {
		this.pincode = pincode;
	}
	public List<OrderDetails> getOrders() {
		return orders;
	}
	public void setOrders(List<OrderDetails> orders) {
		this.orders = orders;
	}
	@Override
	public String toString() {
		return "Address [id=" + id + ", userId=" + userId + ", address=" + address + ", state=" + state + ", pincode="
				+ pincode + ", orders=" + orders + "]";
	}
	
	
	 
	
}