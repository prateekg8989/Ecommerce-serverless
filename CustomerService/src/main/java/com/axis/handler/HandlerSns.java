package com.axis.handler;

import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNS;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNSRecord;
import com.axis.CustomerServiceApplication;
import com.axis.model.Customer;
import com.axis.repository.CustomerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HandlerSns implements RequestHandler<SNSEvent, String> {

	private static ApplicationContext applicationContext = SpringApplication.run(CustomerServiceApplication.class);

	@Override
	public String handleRequest(SNSEvent input, Context context) {
		CustomerRepository customerRepository = applicationContext.getBean(CustomerRepository.class);
		ObjectMapper objectMapper = new ObjectMapper();
		List<SNSRecord> records = input.getRecords();
		for (SNSRecord snsRecord : records) {
			SNS sns = snsRecord.getSNS();
			String subject = sns.getSubject();
			String message = sns.getMessage();
			System.out.println("subject:- " + subject);
			if (subject.equals("UserCreation")) {
				try {
					Map<String, String> customerDetails = objectMapper.readValue(message,
							new TypeReference<Map<String, String>>() {
							});
					customerRepository
							.addCustomer(new Customer(customerDetails.get("userId"), customerDetails.get("username"),
									customerDetails.get("name"), null, customerDetails.get("email")));
					return null;
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}

			System.out.println("message:- " + sns.getMessage());
		}
		return null;
	}

}
