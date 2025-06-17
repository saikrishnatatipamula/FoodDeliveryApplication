package com.example.foodDelivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.foodDelivery.controller.request.AddAddressRequest;
import com.example.foodDelivery.dao.AddressService;

@RestController
@RequestMapping("/api")
public class AddressController {
	@Autowired
	private AddressService addressService;

	@PostMapping("/addAddress")
	public ResponseEntity<String> addAddress(@RequestBody AddAddressRequest request) {

		String result = addressService.addAddress(request);
		
		return ResponseEntity.ok(result);
	}
}
