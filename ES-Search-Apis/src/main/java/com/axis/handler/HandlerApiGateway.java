package com.axis.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.client.RestHighLevelClient;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.axis.config.ESConfig;
import com.axis.model.Product;
import com.axis.util.ElasticSearchUtilities;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HandlerApiGateway implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		ObjectMapper objectMapper = new ObjectMapper();
		String method = input.getHttpMethod();
		String resource = input.getResource();
		Map<String, String> queryParams = input.getQueryStringParameters();

		System.out.println("===PATH=======  " + input.getPath() + "  ===========");
		System.out.println("=====METHOD=====  " + method + "  ===========");
		System.out.println("=======RESOURCe===  " + resource + "  ===========");

		RestHighLevelClient client = null;
		try {
			client = ESConfig.getInstance();
			ElasticSearchUtilities esUtils = new ElasticSearchUtilities(client);
			if (method.equals("GET") && resource.equals("/products")) {
				System.out.println("******* Products Resource Called *****");

				if (queryParams != null && queryParams.containsKey("subCategoryId")) {
					System.out.println("******* Products Resource GET with subCategoryId Called *****");
					String subCatgoryId = queryParams.get("subCategoryId");
					String result = makeJSONStringFromObject(objectMapper,
							esUtils.getAllProductsBySubCategoryId(subCatgoryId, objectMapper));
					return buildResponse(200, result);
				} else if (queryParams != null && queryParams.containsKey("mainCategoryId")) {
					System.out.println("******* Products Resource GET with mainCategoryId Called *****");
					String mainCatgoryId = queryParams.get("mainCategoryId");
					String result = makeJSONStringFromObject(objectMapper,
							esUtils.getAllProductsByMainCategoryId(mainCatgoryId, objectMapper));
					return buildResponse(200, result);
				} else {
					System.out.println("******* Products Resource GET Called *****");
					String result = makeJSONStringFromObject(objectMapper, esUtils.getAllProducts(objectMapper));
					return buildResponse(200, result);
				}
			} else if (method.equals("POST") && resource.equals("/product-search")) {
				System.out.println("******* Products Resource Called *****");
				Map<String, String> mapBody = null;
				try {
					mapBody = objectMapper.readValue(input.getBody(), new TypeReference<Map<String, String>>() {
					});
					if (mapBody != null) {
						String keyword = mapBody.get("keyword");
						String result = "[]";
						result = objectMapper.writeValueAsString(esUtils.searchProducts(keyword, objectMapper));
						return buildResponse(200, result);
					}
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			} else if (method.equals("POST") && resource.equals("/es-products")) {
				System.out.println("******* es Resource Called *****");
				Product prod = null;
				try {
					prod = objectMapper.readValue(input.getBody(), Product.class);
					if (prod != null) {
						String result = "";
						esUtils.saveProduct(prod, objectMapper);
						result = objectMapper.writeValueAsString("Product saved");
						return buildResponse(200, result);
					}
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			} else if (method.equals("DELETE") && resource.equals("/es-products")) {
				System.out.println("******* es Resource Called *****");
				Product prod = null;
				try {
					prod = objectMapper.readValue(input.getBody(), Product.class);
					if (prod != null) {
						String result = "";
						esUtils.deleteProductById(queryParams.get("productId"));
						result = objectMapper.writeValueAsString("Product deleted");
						return buildResponse(200, result);
					}
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			} else if (method.equals("DELETE") && resource.equals("/products")) {
				System.out.println("******* products Resource DEELETE Called *****");
				try {

					String result = "";
					esUtils.deleteAllProduct();
					result = objectMapper.writeValueAsString("Product deleted");
					return buildResponse(200, result);

				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			} else if (method.equals("GET") && resource.equals("/es-update-structure")) {
				System.out.println("******* products /es-update-structure called *****");
				try {

					String result = "";
					esUtils.updateMapping();
					result = objectMapper.writeValueAsString("Mappings updated");
					return buildResponse(200, result);

				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}
			if (client != null) {
				client.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		return null;
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
		if (obj.equals("null")) {
			res.setBody("[]");
		} else {
			res.setBody(obj);
		}
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
