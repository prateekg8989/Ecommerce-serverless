package com.axis.handler;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

//import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNS;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNSRecord;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HandlerSns implements RequestHandler<SNSEvent, String> {

	// Replace sender@example.com with your "From" address.
	// This address must be verified.
	static final String FROM = "*@gmail.com";
	static final String FROMNAME = "<FROMNAME> ";

	// Replace smtp_username with your Amazon SES SMTP user name.
	static final String SMTP_USERNAME = "";

	// Replace smtp_password with your Amazon SES SMTP password.
	static final String SMTP_PASSWORD = "";

	// Amazon SES SMTP host name. This example uses the US West (Oregon) region.
	static final String HOST = "";

	// The port you will connect to on the Amazon SES SMTP endpoint.
	static final int PORT = 587;

	@Override
	public String handleRequest(SNSEvent input, Context context) {

		List<SNSRecord> records = input.getRecords();
		for (SNSRecord snsRecord : records) {
			SNS sns = snsRecord.getSNS();
			String subject = sns.getSubject();
			String message = sns.getMessage();
			System.out.println("subject:- " + subject);
			if (subject.equals("UserCreation")) {
				ObjectMapper objectMapper = new ObjectMapper();
				try {
					Map<String, String> map = objectMapper.readValue(message, new TypeReference<Map<String, String>>() {
					});
					String email = map.get("email");
					String subjectContent = "Welcome to Store Bazaar";
					String bodyContent = String.format(
							"%n %n <style>h</style><h2 style='text-align: center;'>Store Bazaar</h2>%n%n<p>Thanks for joining the Store Bazaar.</p>",
							null);
					sendEmail(email, subjectContent, bodyContent);
				} catch (JsonProcessingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			} else 	if (subject.equals("OrderPlaced")) {
				ObjectMapper objectMapper = new ObjectMapper();
				try {
					Map<String, Object> map = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {
					});
					String email = (String) map.get("email");
					String subjectContent = "Store Bazaar, Order has been placed";
					String bodyContent = String.format(
							"%n <h2 style='text-align: center;'>Store Bazaar</h2>%n%n<p>Thanks for placing  an order from the Store Bazaar.</p>\"%n %s"
							+ "%n<h3>Total Price:- Rs.%s</h3>",
							makeOrderPlacedItemView(map),map.get("totalAmount"));
					sendEmail(email, subjectContent, bodyContent);
				} catch (JsonProcessingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
			System.out.println("message:- " + sns.getMessage());
		}
		return null;
	}

	
	private String makeOrderPlacedItemView(Map<String, Object> map) {
		List<Map<String, Object>> listOfItems = (List<Map<String, Object>>) map.get("orderItem");
		String str = "";
		for (Map<String, Object> map2 : listOfItems) {
			str = str + "<div style='display: flex;'>"
						+"<div>"
							+ "<img src=\"" + ((ArrayList<String>)map2.get("imageUrls1")).get(0) + "\" width='130px' height='110px'/>"
							+ "</div>"
						+"<div style='margin-left: 30px;'>"
							+ "<h4 style='margin-bottom:0px;'>Price:- " + map2.get("sellingPrice")  +  "</h4>"
							+ "<h4>Quantity:- " + map2.get("quantities") +  "</h4>"
						+ "</div>"
					+ "</div><br/>";
		}
		return str;
	}

	private void sendEmail(String recepientEmail, String subject, String body) {
		try {
			AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
					// Replace US_WEST_2 with the AWS Region you're using for
					// Amazon SES.
					.withRegion(Regions.US_EAST_2).build();
			SendEmailRequest request = new SendEmailRequest()
					.withDestination(new Destination().withToAddresses(recepientEmail))
					.withMessage(new Message()
							.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(body)))
							.withSubject(new Content().withCharset("UTF-8").withData(subject)))
					.withSource(FROM);
			client.sendEmail(request);
			System.out.println("Email sent!");
		} catch (Exception ex) {
			System.out.println("The email was not sent. Error message: " + ex.getMessage());
		}
	}
}
