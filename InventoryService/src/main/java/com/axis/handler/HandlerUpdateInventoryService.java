package com.axis.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

//@Component
public class HandlerUpdateInventoryService implements RequestHandler<List<Product>, String> {

	private static ApplicationContext applicationContext = SpringApplication.run(InventoryServiceApplication.class);

	private ProductRepository productRepository;

	@Override
	public String handleRequest(List<Product> input, Context context) {
		this.productRepository = applicationContext.getBean(ProductRepository.class);
		ObjectMapper objectMapper = new ObjectMapper();
		boolean isRequiredQuantitiesAvailable = true;
		Map<String, Integer> productIdQuantityMap = new HashMap<String, Integer>();
		List<Product> productsWhoseQuantityIsLess = new ArrayList<Product>();
		for (Product cartItem : input) {
			System.out.println(cartItem);
			Product productInDatabase = productRepository.getProductById(cartItem.getProductId());
			if (productInDatabase.getQuantities() < cartItem.getQuantities()) {
				isRequiredQuantitiesAvailable = false;
				productsWhoseQuantityIsLess.add(cartItem);
			} else {
				productIdQuantityMap.put(cartItem.getProductId(),
						productInDatabase.getQuantities() - cartItem.getQuantities());
			}
		}
		if (isRequiredQuantitiesAvailable) {
			Set<Entry<String, Integer>> setEntryMap = productIdQuantityMap.entrySet();
			for (Entry<String, Integer> entry : setEntryMap) {
				productRepository.updateProductById(new Product(entry.getKey(), entry.getValue()));
			}
			return null;
		}
		return makeJSONStringFromObject(objectMapper, productsWhoseQuantityIsLess);
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