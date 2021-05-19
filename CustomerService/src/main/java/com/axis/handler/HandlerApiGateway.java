package com.axis.handler;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.services.lambda.model.ServiceException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.axis.CustomerServiceApplication;
import com.axis.model.Cart;
import com.axis.model.CartItem;
import com.axis.model.Customer;
import com.axis.model.CustomerAddress;
import com.axis.model.Order;
import com.axis.repository.CartRepository;
import com.axis.repository.CustomerRepository;
import com.axis.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

public class HandlerApiGateway implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static ApplicationContext applicationContext = SpringApplication.run(CustomerServiceApplication.class);

	private static String User_Creation_Topic = "arn:aws:sns:us-east-2:423814357905:UserCreation";

	private static String UpdateInventoryFunctionName = "check-inventory-service";

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		CustomerRepository customerRepository = applicationContext.getBean(CustomerRepository.class);
		CartRepository cartRepository = applicationContext.getBean(CartRepository.class);
		OrderRepository orderRepository = applicationContext.getBean(OrderRepository.class);
		ObjectMapper objectMapper = new ObjectMapper();
		String method = input.getHttpMethod();
		String resource = input.getResource();
		System.out.println("===PATH=======  " + input.getPath() + "  ===========");
		System.out.println("=====METHOD=====  " + method + "  ===========");
		System.out.println("=======RESOURCe===  " + resource + "  ===========");
		Map<String, String> queryParams = input.getQueryStringParameters();
		if (resource.equals("/customer")) {
			if (method.equals("GET") && queryParams.containsKey("userId")) {
				String userId = queryParams.get("userId");
				String str = makeJSONStringFromObject(objectMapper, customerRepository.getCustomerByUserId(userId));
				return buildResponse(200, str);
			} else if (method.equals("POST")) {
				try {
					Customer customer = objectMapper.readValue(input.getBody(), Customer.class);
					String str = makeJSONStringFromObject(objectMapper, customerRepository.addCustomer(customer));
					return buildResponse(200, str);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return buildResponse(500, "Error Occured:- " + e.getMessage());
				}
			} else if (method.equals("PUT")) {
				try {
					Customer customer = objectMapper.readValue(input.getBody(), Customer.class);
					String str = makeJSONStringFromObject(objectMapper, customerRepository.editCustomer(customer));
					return buildResponse(200, str);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return buildResponse(500, "Error Occured:- " + e.getMessage());
				}
			}
		} else if (resource.equals("/customer-addresses")) {
			if (method.equals("GET") && queryParams.containsKey("userId")) {
				String userId = queryParams.get("userId");
				String str = makeJSONStringFromObject(objectMapper,
						customerRepository.getAllAddressesOfCustomer(userId));
				return buildResponse(200, str);
			}
		} else if (resource.equals("/customer-address")) {
			if (method.equals("POST") && queryParams.containsKey("userId")) {
				String userId = queryParams.get("userId");
				try {
					CustomerAddress customerAddress = objectMapper.readValue(input.getBody(), CustomerAddress.class);
					customerRepository.addAddressToCustomer(userId, customerAddress);
					return buildResponse(200, "Customer address added");
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return buildResponse(500, "Error Occured:- " + e.getMessage());
				}
			} else if (method.equals("DELETE") && queryParams.containsKey("userId")) {
				String userId = queryParams.get("userId");
				String addressId = queryParams.get("addressId");
				customerRepository.deleteAddressOfCustomer(userId, addressId);
				return buildResponse(200, "Customer address deleted");
			}
		} else if (resource.equals("/cart-item")) {
			if (method.equals("POST") && queryParams.containsKey("userId")) {
				String userId = queryParams.get("userId");
				try {
					CartItem cartItem = objectMapper.readValue(input.getBody(), CartItem.class);
					String str = makeJSONStringFromObject(objectMapper, cartRepository.addItemToCart(userId, cartItem));
					return buildResponse(200, str);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return buildResponse(500, "Error Occured:- " + e.getMessage());
				}
			} else if (method.equals("PUT") && queryParams.containsKey("userId")) {
				String userId = queryParams.get("userId");
				int newQuantity = Integer.parseInt(queryParams.get("newQuantity"));
				String cartItemId = queryParams.get("cartItemId");
				cartRepository.updateItemQuantityToCart(newQuantity, userId, cartItemId);
				return buildResponse(200, "Cart updated");
			} else if (method.equals("DELETE") && queryParams.containsKey("userId")) {
				String userId = queryParams.get("userId");
				String cartItemId = queryParams.get("cartItemId");
				cartRepository.removeCartItemFromCart(userId, cartItemId);
				return buildResponse(200, "Cart updated");
			}
		} else if (resource.equals("/cart")) {
			if (method.equals("GET") && queryParams.containsKey("userId")) {
				String userId = queryParams.get("userId");
				Cart cart = cartRepository.getTheCart(userId);
				return buildResponse(200, makeJSONStringFromObject(objectMapper, cart));
			} else if (method.equals("DELETE") && queryParams.containsKey("userId")) {
				String userId = queryParams.get("userId");
				cartRepository.clearTheCart(userId);
				return buildResponse(200, "Cart updated");
			}
		} else if (resource.equals("/order") && queryParams.containsKey("userId")) {
			String userId = queryParams.get("userId");
			if (method.equals("POST")) {
				Cart cart = cartRepository.getTheCart(userId);
				String str = makeJSONStringFromObject(objectMapper, cart.getCartItems());
				InvokeRequest invokeRequest = new InvokeRequest().withFunctionName(UpdateInventoryFunctionName)
						.withPayload(str);
				InvokeResult invokeResult = null;
				try {
					AWSLambda awsLambda = AWSLambdaClientBuilder.standard()
							.withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
							.withRegion(Regions.US_EAST_2).build();

					invokeResult = awsLambda.invoke(invokeRequest);

					String ans = new String(invokeResult.getPayload().array(), StandardCharsets.UTF_8);
					// write out the return value
					System.out.println(ans);

					if (ans.equals("null")) {
						System.out.println("Decreased the quantity in database");
						String addressId = queryParams.get("addressId");
						Order order = orderRepository.placeOrder(userId, addressId);
						if (order != null) {
							SnsClient snsClient = SnsClient.builder().region(Region.US_EAST_2).build();
							PublishRequest request = PublishRequest.builder().topicArn(User_Creation_Topic)
									.subject("OrderPlaced").message(makeJSONStringFromObject(objectMapper, order))
									.build();
							PublishResponse result = snsClient.publish(request);
							System.out.println(result.messageId() + " Message sent. Status was "
									+ result.sdkHttpResponse().statusCode());
							snsClient.close();
						}
						return buildResponse(200, "Order Placed Successfully");
					} else {
						System.out.println(
								"Cannot decrease the quantity in database, insufficient quantity in inventory");
						return buildResponse(202, makeJSONStringFromObject(objectMapper, ans));
					}
				} catch (ServiceException e) {
					System.out.println("Error while updating inventory");
					System.out.println(e);
				}

				System.out.println(invokeResult.getStatusCode());

				return buildResponse(200, "Order Placed");
			}
		} else if (resource.equals("/orders") && queryParams.containsKey("userId")) {
			if (method.equals("GET")) {
				String userId = queryParams.get("userId");
				List<Order> orders = orderRepository.getAllOrdersForUser(userId);
				return buildResponse(200, makeJSONStringFromObject(objectMapper, orders));

			}
		}
		return buildResponse(500, "");
	}

	public static APIGatewayProxyResponseEvent buildResponse(int status, String obj) {
		APIGatewayProxyResponseEvent res = new APIGatewayProxyResponseEvent();
		res.setStatusCode(status);
		Map<String, String> map = new HashMap<String, String>();
		map.put("Content-Type", "application/json");
		map.put("Access-Control-Allow-Headers", "*");
		map.put("Access-Control-Allow-Origin", "*");
		map.put("Access-Control-Allow-Methods", "*");
		res.withHeaders(map);
//		res.setHeaders(map);
		res.setBody(obj);
		return res;
	}

	public static String makeJSONStringFromObject(ObjectMapper objectMapper, Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
			return "";
		}
	}

}