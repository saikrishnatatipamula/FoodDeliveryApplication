package com.example.foodDelivery.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig
{
	@Value("${transaction.service.doTransaction.url}")
	public String doTransactionURL;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}