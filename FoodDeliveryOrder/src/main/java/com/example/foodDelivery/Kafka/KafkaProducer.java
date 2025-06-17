package com.example.foodDelivery.Kafka;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
	
	@Value("${user.kafka.topic}")
	private String USER_KAFKA_TOPIC;
	
	private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
	
	private KafkaTemplate<String, String> kafkaTemplate;

	public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendMessage(String message) {
		kafkaTemplate.send(USER_KAFKA_TOPIC.strip(), message);
		logger.info(String.format("Message: %s sent to Topic: %s", message, USER_KAFKA_TOPIC));
	}
	
}
