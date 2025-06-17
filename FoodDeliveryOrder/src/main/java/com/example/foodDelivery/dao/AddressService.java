package com.example.foodDelivery.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.foodDelivery.Kafka.KafkaProducer;
import com.example.foodDelivery.controller.request.AddAddressRequest;
import com.example.foodDelivery.model.Address;
import com.example.foodDelivery.repository.AddressRepository;

@Service
public class AddressService {

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private KafkaProducer kafkaProducer;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${user.service.isUserConfirmed.url}")
	public String isUserConfirmedUrl;

	public String addAddress(AddAddressRequest request) {
		try {
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(isUserConfirmedUrl).queryParam("userId",
					request.getUserId());
			ResponseEntity<Boolean> userResponse = restTemplate.getForEntity(builder.toUriString(), Boolean.class);
			Boolean isUserConfirmed = userResponse.getBody();
			if (!isUserConfirmed) {

				return "User is not confirmed  : " + request.getUserId();

			} else {
				Address address = new Address();
				address.setUserId(request.getUserId());
				address.setAddress(request.getAddress());
				address.setState(request.getState());
				address.setPincode(request.getPincode());
				kafkaProducer.sendMessage("Address Added successfully for the User : " + request.getUserId());
				addressRepository.save(address);
				return "Address details are saved successfully for the user " + request.getUserId();
			}

		}

		catch (Exception e) {
			
			System.out.println( "Exception Found in AddAddress Service" + e);
			return "User not found with ID: " + request.getUserId();

		}

	}
}
