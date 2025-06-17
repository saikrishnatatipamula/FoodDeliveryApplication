package com.example.foodDelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.foodDelivery.controller.request.AddAddressRequest;
import com.example.foodDelivery.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
	
}
