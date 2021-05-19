package com.axis.handler;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.axis.AuthServiceMsApplication;
import com.axis.config.PasswordHasher;
import com.axis.message.request.LoginForm;
import com.axis.message.request.SignUpForm;
import com.axis.message.response.JwtResponse;
import com.axis.model.User;
import com.axis.repository.UserRepository;
import com.axis.security.jwt.JwtAuthTokenValidator;
import com.axis.security.jwt.JwtProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.SnsClientBuilder;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;

@Component
public class HandlerApiGateway implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

//	private static ApplicationContext springContext;
	private static String User_Creation_Topic = "arn:aws:sns:us-east-2:423814357905:UserCreation";

	private static ApplicationContext applicationContext = SpringApplication.run(AuthServiceMsApplication.class);

	UserRepository userRepository;

	PasswordHasher passwordHasher;

	JwtProvider jwtProvider;

	JwtAuthTokenValidator authTokenValidator;

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		this.userRepository = applicationContext.getBean(UserRepository.class);
		this.passwordHasher = applicationContext.getBean(PasswordHasher.class);
		this.jwtProvider = applicationContext.getBean(JwtProvider.class);
		this.authTokenValidator = applicationContext.getBean(JwtAuthTokenValidator.class);
		ObjectMapper objectMapper = new ObjectMapper();
		String method = input.getHttpMethod();
		String resource = input.getResource();
		System.out.println("===PATH=======  " + input.getPath() + "  ===========");
		System.out.println("=====METHOD=====  " + method + "  ===========");
		System.out.println("=======RESOURCe===  " + resource + "  ===========");
		Map<String, String> queryParams = input.getQueryStringParameters();
		if (method.equals("POST") && resource.equals("/signup")) {
			System.out.println("************Signup api called*************");
			String body = input.getBody();
			SignUpForm signUpRequest = null;
			try {
				signUpRequest = objectMapper.readValue(body, SignUpForm.class);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				Map<String, String> map = new HashMap<String, String>();
				map.put("message", "Please send required parameters!!");
				return buildResponse(400, makeJSONStringFromObject(objectMapper, map));
			}

			if (userRepository.existsByUsername(signUpRequest.getUsername())) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("message", "Username already exists!!");
				return buildResponse(400, makeJSONStringFromObject(objectMapper, map));
			}

			if (userRepository.existsByEmail(signUpRequest.getUsername(), signUpRequest.getEmail())) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("message", "Email already in use!!");
				return buildResponse(400, makeJSONStringFromObject(objectMapper, map));
			}
			String[] strArr;
			try {
				strArr = passwordHasher.makeHashAndSalt(signUpRequest.getPassword());

				// Creating user's account
				User user = new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(),
						strArr[0], strArr[1]);

				String strRole = signUpRequest.getRole();

				if (strRole.equals("admin")) {
					user.setRole(strRole);
				} else {
					user.setRole(strRole);
				}

				User savedUser = userRepository.addUser(user);
				SnsClient snsClient = SnsClient.builder().region(Region.US_EAST_2).build();
//				SubscribeRequest request = SubscribeRequest.builder().topicArn("").protocol(strRole).endpoint(strRole).build();
				PublishRequest request = PublishRequest.builder().topicArn(User_Creation_Topic).subject("UserCreation")
						.message(makeJSONStringFromObject(objectMapper, savedUser)).build();
				PublishResponse result = snsClient.publish(request);
				System.out.println(
						result.messageId() + " Message sent. Status was " + result.sdkHttpResponse().statusCode());
				snsClient.close();
				return buildResponse(200, "User added successfully");
			} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		} else if (method.equals("POST") && resource.equals("/signin")) {
			System.out.println("************Signin api called*************");
			String body = input.getBody();
			LoginForm loginRequest = null;
			try {
				loginRequest = objectMapper.readValue(body, LoginForm.class);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				Map<String, String> map = new HashMap<String, String>();
				map.put("message", "Please send required parameters!!");
				System.out.println("Please send required parameters!!");
				return buildResponse(400, makeJSONStringFromObject(objectMapper, map));
			}

			User userFound = userRepository.findByUsername(loginRequest.getUsername());
			if (userFound == null) {
				System.out.println("User not found with that username!!");
				return buildResponse(404, "User not found with that username!!");
			} else {
				try {
					boolean isAuthenticated = passwordHasher.validatePassword(loginRequest.getPassword(),
							userFound.getSalt(), userFound.getPassword());
					if (isAuthenticated) {
						JwtResponse jwtResponse = new JwtResponse(jwtProvider.generateJwtToken(userFound),
								userFound.getUsername(), userFound.getRole(), userFound.getUserId());
						String responseBody = objectMapper.writeValueAsString(jwtResponse);
						return buildResponse(200, responseBody);
					} else {
						Map<String, String> map = new HashMap<String, String>();
						System.out.println("Incorrect credentials!!");
						map.put("message", "Incorrect credentials!!");
						return buildResponse(400, makeJSONStringFromObject(objectMapper, map));
					}

				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
					Map<String, String> map = new HashMap<String, String>();
					System.out.println("Incorrect credentials!!");
					map.put("message", "Incorrect credentials!!");
					return buildResponse(400, makeJSONStringFromObject(objectMapper, map));
				} catch (InvalidKeySpecException e) {
					Map<String, String> map = new HashMap<String, String>();
					System.out.println("Incorrect credentials!!");
					map.put("message", "Incorrect credentials!!");
					return buildResponse(400, makeJSONStringFromObject(objectMapper, map));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					Map<String, String> map = new HashMap<String, String>();
					System.out.println("Error Occured!!");
					map.put("message", "Error Occured!!");
					return buildResponse(500, makeJSONStringFromObject(objectMapper, map));
				}
			}

		} else if (method.equals("GET") && resource.equals("/validate")) {
			Map<String, String> headers = input.getHeaders();
			System.out.println("*********/validate api called*********");
			System.out.println("headers => " + headers);
			String authHeader = headers.get("Authorization");
			try {
				User user = authTokenValidator.authenticate(authHeader);
				if (user != null) {
					return buildResponse(200, objectMapper.writeValueAsString(user));
				} else {
					Map<String, String> map = new HashMap<String, String>();
					map.put("message", "Incorrect Token!!");
					return buildResponse(403, makeJSONStringFromObject(objectMapper, map));
				}
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				Map<String, String> map = new HashMap<String, String>();
				map.put("message", "Error Occured!!");
				return buildResponse(500, makeJSONStringFromObject(objectMapper, map));
			}
		} else {
			System.out.println("************nothing called*************");
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
