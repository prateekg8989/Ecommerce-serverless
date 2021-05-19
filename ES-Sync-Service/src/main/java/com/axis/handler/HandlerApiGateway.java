package com.axis.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.client.RestHighLevelClient;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.StreamRecord;
import com.axis.config.ESConfig;
import com.axis.model.Product;
import com.axis.util.ElasticSearchUtilities;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HandlerApiGateway implements RequestHandler<DynamodbEvent, String> {

	@Override
	public String handleRequest(DynamodbEvent input, Context context) {
		RestHighLevelClient client = null;
		try {
			client = ESConfig.getInstance();
			ObjectMapper objectMapper = new ObjectMapper();
			ElasticSearchUtilities esUtils = new ElasticSearchUtilities(client);
			for (DynamodbStreamRecord record : input.getRecords()) {
				String eventName = record.getEventName();
				StreamRecord streamRecord = record.getDynamodb();
				if (eventName.equals("INSERT")) {
					System.out.println("******** INSERT *********");
					esUtils.saveProduct(makeProductFromStream(streamRecord), objectMapper);
				} else if (eventName.equals("MODIFY")) {
					System.out.println("******** MODIFY *********");
					Map<String, AttributeValue> mapKey = streamRecord.getKeys();
					String id = getStringFromAttribute(mapKey.get("productId"));
					esUtils.updateProductById(id, makeProductFromStream(streamRecord), objectMapper);
				} else if (eventName.equals("REMOVE")) {
					System.out.println("******** REMOVE *********");
					Map<String, AttributeValue> map = streamRecord.getKeys();
					String productId = getStringFromAttribute(map.get("productId"));
					esUtils.deleteProductById(productId);
				}
				System.out.println(record);
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

	static Product makeProductFromStream(StreamRecord streamRecord) {
		Map<String, AttributeValue> map = streamRecord.getNewImage();
		Product product = new Product();
		product.setId(getStringFromAttribute(map.get("productId")));
		product.setProductId(getStringFromAttribute(map.get("productId")));
		product.setProductName(getStringFromAttribute(map.get("productName")));
		product.setProductSummary(getStringFromAttribute(map.get("productSummary")));
		product.setProductDesc(getStringFromAttribute(map.get("productDesc")));
		product.setMainCategoryName(getStringFromAttribute(map.get("mainCategoryName")));
		product.setSubCategoryName(getStringFromAttribute(map.get("subCategoryName")));
		product.setSubCategoryId(getStringFromAttribute(map.get("subCategoryId")));
		product.setMainCategoryId(getStringFromAttribute(map.get("mainCategoryId")));
		product.setViews(getIntFromAttribute(map.get("views")));
		product.setSellingPrice(getIntFromAttribute(map.get("sellingPrice")));
		product.setCostPrice(getIntFromAttribute(map.get("costPrice")));
		product.setQuantities(getIntFromAttribute(map.get("quantities")));
		product.setImageUrls1(getSetStringFromAttribute(map.get("imageUrls1")));
		return product;
	}

	static String getStringFromAttribute(AttributeValue value) {
		if (value == null) {
			return null;
		} else {
			return value.getS();
		}
	}

	static int getIntFromAttribute(AttributeValue value) {
		if (value == null) {
			return 0;
		} else {
			return Integer.parseInt(value.getN());
		}
	}

	static Set<String> getSetStringFromAttribute(AttributeValue value) {
		if (value == null) {
			return new HashSet<String>();
		} else {
			Set<String> set = new HashSet<String>();
			set.addAll(value.getSS());
			return set;
		}
	}

}
