package com.example.kafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserKafkaConsumer {
	

	private static final Logger logger = LoggerFactory.getLogger(UserKafkaConsumer.class);
	
	@KafkaListener(topics = "#{'${user.kafka.topic}'}")
	 public void consume(String message) {
		 
		logger.info(String.format("Message received from user kafka topic: %s", message));
	 }
}
