package com.axis.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.axis.InventoryServiceApplication;
import com.axis.model.MainCategory;
import com.axis.model.Product;
import com.axis.model.SubCategory;
import com.axis.repository.MainCategoryRepository;
import com.axis.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//@Component
public class HandlerApiGateway implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static ApplicationContext applicationContext = SpringApplication.run(InventoryServiceApplication.class);

	private ProductRepository productRepository;

	private MainCategoryRepository mainCategoryRepository;

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		this.productRepository = applicationContext.getBean(ProductRepository.class);
		this.mainCategoryRepository = applicationContext.getBean(MainCategoryRepository.class);
		ObjectMapper objectMapper = new ObjectMapper();
		String method = input.getHttpMethod();
		String resource = input.getResource();
		System.out.println("===PATH=======  " + input.getPath() + "  ===========");
		System.out.println("=====METHOD=====  " + method + "  ===========");
		System.out.println("=======RESOURCe===  " + resource + "  ===========");
		Map<String, String> queryParams = input.getQueryStringParameters();
		if (resource.equals("/products")) {
			System.out.println("******* Products Resource Called *****");
			if (method.equals("GET") && queryParams != null && queryParams.containsKey("subCategoryId")) {
				System.out.println("******* Products Resource GET with subCategoryId Called *****");
				String subCatgoryId = queryParams.get("subCategoryId");
				String result = makeJSONStringFromObject(objectMapper,
						productRepository.getProductsBySubCategoryId(subCatgoryId));
				return buildResponse(200, result);
			} else if (method.equals("GET") && queryParams != null && queryParams.containsKey("mainCategoryId")) {
				System.out.println("******* Products Resource GET with mainCategoryId Called *****");
				String mainCatgoryId = queryParams.get("mainCategoryId");
				String result = makeJSONStringFromObject(objectMapper,
						productRepository.getProductsByMainCategoryId(mainCatgoryId));
				return buildResponse(200, result);
			} else {
				System.out.println("******* Products Resource GET Called *****");
				String result = makeJSONStringFromObject(objectMapper, productRepository.getAllProducts());
				return buildResponse(200, result);
			}
		} else if (resource.equals("/product")) {
			System.out.println("******* Product Resource Called *****");
			if (method.equals("GET") && queryParams.containsKey("productId")) {
				System.out.println("******* Product Resource GET with productId Called *****");
				String productId = queryParams.get("productId");
				String result = makeJSONStringFromObject(objectMapper, productRepository.getProductById(productId));
				return buildResponse(200, result);
			} else if (method.equals("POST")) {
				System.out.println("******* Product Resource POST Called *****");
				String body = input.getBody();
				Product product = null;
				String result = "[]";
				try {
					product = objectMapper.readValue(body, Product.class);
					if (product != null) {
						result = objectMapper.writeValueAsString(productRepository.saveProduct(product));
						return buildResponse(200, result);
					}
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					return buildResponse(500, "error occured!!");
				}
			} else if (method.equals("PUT")) {
				System.out.println("******* Product Resource PUT Called *****");
				String body = input.getBody();
				Product product = null;
				String result = "[]";
				try {
					product = objectMapper.readValue(body, Product.class);
					if (product != null) {
						result = objectMapper.writeValueAsString(productRepository.updateProductById(product));
						return buildResponse(200, result);
					}
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					return buildResponse(500, "error occured!!");
				}

			} else if (method.equals("DELETE")) {
				System.out.println("******* Product Resource DELETE Called *****");
				String productId = queryParams.get("productId");
				String result = "[]";
				result = productRepository.deleteProductById(productId);
				return buildResponse(200, result);

			}
		} else if (resource.equals("/category")) {
			System.out.println("******* Category Resource Called ***** " + input.getResource());
			if (method.equals("POST")) {
				System.out.println("******* Category POST Called *****");
				String body = input.getBody();
				MainCategory mainCategory = null;
				String result = "[]";
				try {
					mainCategory = objectMapper.readValue(body, MainCategory.class);
					if (mainCategory != null) {
						result = objectMapper.writeValueAsString(mainCategoryRepository.saveMainCategory(mainCategory));
						return buildResponse(200, result);
					}
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					return buildResponse(500, "error occured!!");
				}
			} else if (method.equals("GET")) {
				System.out.println("******* Category GET Called *****");
				String result = makeJSONStringFromObject(objectMapper, mainCategoryRepository.getAllMainCategories());
				return buildResponse(200, result);
			} else if (method.equals("DELETE") && queryParams.containsKey("mainCategoryId")) {
				String mainCategoryId = queryParams.get("mainCategoryId");
				String result = makeJSONStringFromObject(objectMapper,
						mainCategoryRepository.deleteMainCategoryById(mainCategoryId));
				return buildResponse(200, result);
			}
		} else if (resource.equals("/sub-category")) {
			System.out.println("******* Category Resource Called *****");
			String mainCategoryId = queryParams.get("mainCategoryId");
			if (method.equals("POST") && queryParams.containsKey("mainCategoryId")) {
				System.out.println("******* sub-Category POST Called *****");
				String body = input.getBody();
				SubCategory subCategory = null;
				String result = "[]";
				try {
					subCategory = objectMapper.readValue(body, SubCategory.class);
					if (subCategory != null) {
						result = objectMapper.writeValueAsString(
								mainCategoryRepository.saveSubCategoryToMainCategory(subCategory, mainCategoryId));
						return buildResponse(200, result);
					}
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return buildResponse(500, "error occured!!");
				}
			} else if (method.equals("DELETE") && queryParams.containsKey("mainCategoryId")
					&& queryParams.containsKey("subCategoryId")) {
				System.out.println("******* Sub-Category DELETE Called *****");
				String subCategoryId = queryParams.get("subCategoryId");
				String result = makeJSONStringFromObject(objectMapper,
						mainCategoryRepository.deleteSubCategoryById(mainCategoryId, subCategoryId));
				return buildResponse(200, result);
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