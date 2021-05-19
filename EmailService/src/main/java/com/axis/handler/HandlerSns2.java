package com.axis.handler;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNS;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNSRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HandlerSns2 implements RequestHandler<SNSEvent, String> {

	// Replace sender@example.com with your "From" address.
	// This address must be verified.
	static final String FROM = "@gmail.com";
	static final String FROMNAME = " ";

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
							"%n %n<h2 class=\"text-align-center\">Store Bazaar</h2>%n%n<p>Thanks for joining the Store Bazaar.</p>",
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
							"%n %n<h2 class=\"text-align-center\">Store Bazaar</h2>%n%n<p>Thanks for placing  an order from the Store Bazaar.</p>\"%n %n %s"
							+ "%n%n%n<h3>Total Price:- Rs.%s</h3>",
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

	private void sendEmail(String recepientEmail, String subject, String body) {

		// Create a Properties object to contain connection configuration information.
		Properties props = System.getProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.port", PORT);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");

		// Create a Session object to represent a mail session with the specified
		// properties.
		Session session = Session.getDefaultInstance(props);

		// Create a message with the specified information.
		Transport transport = null;
		MimeMessage msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(FROM, FROMNAME));

			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recepientEmail));
			msg.setSubject(subject);
			msg.setContent(body, "text/html");

			// Create a transport.
			transport = session.getTransport();

			// Connect to Amazon SES using the SMTP username and password you specified
			// above.
			transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);

			// Send the email.
			transport.sendMessage(msg, msg.getAllRecipients());
			System.out.println("Email sent!");

		} catch (UnsupportedEncodingException | MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("The email was not sent.");
			System.out.println("Error message: " + e.getMessage());
		} finally {
			// Close and terminate the connection.
			try {
				transport.close();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private String makeOrderPlacedItemView(Map<String, Object> map) {
		List<Map<String, Object>> listOfItems = (List<Map<String, Object>>) map.get("orderItem");
		String str = "";
		for (Map<String, Object> map2 : listOfItems) {
			str = "<div style=\"{display: flex;}\">"
						+"<div>"
							+ "<img src=\"" + ((ArrayList<String>)map2.get("imageUrls1")).get(0) + "\" style=\"{width: 150px, height: 110px}\" >"
							+ "</div>%n"
						+"<div>"
							+ "<h4>Price:- " + map2.get("sellingPrice")  +  "</h4>%n"
							+ "<h4>Quantity:- " + map2.get("quantities") +  "</h4>"
						+ "</div>%n"
					+ "</div>";
		}
		return str;
	}

}
