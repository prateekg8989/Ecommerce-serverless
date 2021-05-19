package com.axis.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.Build.Type;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import com.axis.model.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ElasticSearchUtilities {

	private static String IndexName = "products2";

	private RestHighLevelClient client;

	public ElasticSearchUtilities(RestHighLevelClient client) {
		super();
		this.client = client;
	}

	public void saveProduct(Product product, ObjectMapper objectMapper) {
		IndexRequest request = new IndexRequest(IndexName);
		try {
			String prodString = objectMapper.writeValueAsString(product);
			request.id(product.getProductId());
			request.source(prodString, XContentType.JSON);
			IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
			String index = indexResponse.getIndex();
			String id = indexResponse.getId();
		} catch (IOException | ElasticsearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Product> getAllProducts(ObjectMapper objectMapper) {

		List<Product> products = new ArrayList<Product>();
		SearchRequest searchRequest = new SearchRequest(IndexName);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchRequest.source(searchSourceBuilder);
		try {
			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			SearchHits hits = searchResponse.getHits();
			SearchHit[] searchHits = hits.getHits();
			for (SearchHit hit : searchHits) {
				String sourceAsString = hit.getSourceAsString();
				Product product = objectMapper.readValue(sourceAsString, Product.class);
				products.add(product);
			}
			return products;

		} catch (IOException | ElasticsearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public Product getProductById(String id, ObjectMapper objectMapper) {
		GetRequest getRequest = new GetRequest(IndexName, id);
		try {
			GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
			if (getResponse.isExists()) {
				String sourceAsString = getResponse.getSourceAsString();
				Product product = objectMapper.readValue(sourceAsString, Product.class);
				return product;
			} else {
				return null;
			}

		} catch (IOException | ElasticsearchException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Product updateProductById(String id, Product product, ObjectMapper objectMapper) {
		try {
			UpdateRequest request = new UpdateRequest(IndexName, id);
			String productStr = objectMapper.writeValueAsString(product);
			request.docAsUpsert(true);
			request.fetchSource(true);
			request.doc(productStr, XContentType.JSON);
			UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);
			GetResult result = updateResponse.getGetResult();
			if (result.isExists()) {
				String sourceAsString = result.sourceAsString();
				Product productUpdated = objectMapper.readValue(sourceAsString, Product.class);
				return productUpdated;
			} else {
				return null;
			}
		} catch (IOException | ElasticsearchException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void deleteProductById(String prodId) {
		DeleteRequest request = new DeleteRequest(IndexName, "1");
		try {
			DeleteResponse deleteResponse = client.delete(request, RequestOptions.DEFAULT);
		} catch (IOException | ElasticsearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Product> getAllProductsByMainCategoryId(String mainCategoryId, ObjectMapper objectMapper) {
		List<Product> products = new ArrayList<Product>();
		SearchRequest searchRequest = new SearchRequest(IndexName);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.termQuery("mainCategoryId", mainCategoryId));
//		sourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
		searchSourceBuilder.sort(new FieldSortBuilder("views").order(SortOrder.ASC));
		searchRequest.source(searchSourceBuilder);
		try {
			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			SearchHits hits = searchResponse.getHits();
			SearchHit[] searchHits = hits.getHits();
			for (SearchHit hit : searchHits) {
				String sourceAsString = hit.getSourceAsString();
				Product product = objectMapper.readValue(sourceAsString, Product.class);
				products.add(product);
			}
			return products;

		} catch (IOException | ElasticsearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Product> getAllProductsBySubCategoryId(String subCategoryId, ObjectMapper objectMapper) {
		List<Product> products = new ArrayList<Product>();
		SearchRequest searchRequest = new SearchRequest(IndexName);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.termQuery("subCategoryId", subCategoryId));
//		sourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
		searchSourceBuilder.sort(new FieldSortBuilder("views").order(SortOrder.ASC));
		searchRequest.source(searchSourceBuilder);
		try {
			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			SearchHits hits = searchResponse.getHits();
			SearchHit[] searchHits = hits.getHits();
			for (SearchHit hit : searchHits) {
				String sourceAsString = hit.getSourceAsString();
				Product product = objectMapper.readValue(sourceAsString, Product.class);
				products.add(product);
			}
			return products;

		} catch (IOException | ElasticsearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Product> searchProducts(String keyword, ObjectMapper objectMapper) {
		List<Product> products = new ArrayList<Product>();
		SearchRequest searchRequest = new SearchRequest(IndexName);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.multiMatchQuery(keyword, "productName", "productSummary",
				"mainCategoryName", "subCategoryName"));
		searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
		searchRequest.source(searchSourceBuilder);
		try {
			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			SearchHits hits = searchResponse.getHits();
			SearchHit[] searchHits = hits.getHits();
			for (SearchHit hit : searchHits) {
				String sourceAsString = hit.getSourceAsString();
				Product product = objectMapper.readValue(sourceAsString, Product.class);
				products.add(product);
			}
			return products;
		} catch (IOException | ElasticsearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public void deleteAllProduct() {
		DeleteByQueryRequest request = new DeleteByQueryRequest(IndexName);
		request.setQuery(QueryBuilders.matchAllQuery());
		request.setConflicts("proceed");
		try {
			BulkByScrollResponse bulkResponse = client.deleteByQuery(request, RequestOptions.DEFAULT);
		} catch (IOException | ElasticsearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void deleteIndices() {
		DeleteIndexRequest request = new DeleteIndexRequest("products", "products1");
		AcknowledgedResponse deleteIndexResponse;
		try {
			deleteIndexResponse = client.indices().delete(request, RequestOptions.DEFAULT);
			boolean acknowledged = deleteIndexResponse.isAcknowledged();
			System.out.println("Indices deletion response:- " + acknowledged);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateMapping() {
		CreateIndexRequest request = new CreateIndexRequest(IndexName);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		Map<String, Object> propertiesMap = new HashMap<String, Object>();

		Map<String, Object> productIdMap = new HashMap<String, Object>();
		productIdMap.put("type", "keyword");
		propertiesMap.put("productId", productIdMap);

		Map<String, Object> productNameMap = new HashMap<String, Object>();
		productNameMap.put("type", "text");
		propertiesMap.put("productName", productNameMap);

		Map<String, Object> productSummaryMap = new HashMap<String, Object>();
		productSummaryMap.put("type", "text");
		propertiesMap.put("productSummary", productSummaryMap);

		Map<String, Object> productDescMap = new HashMap<String, Object>();
		productDescMap.put("type", "text");
		propertiesMap.put("productDesc", productDescMap);

		Map<String, Object> viewsMap = new HashMap<String, Object>();
		viewsMap.put("type", "keyword");
		propertiesMap.put("views", viewsMap);

		Map<String, Object> sellingPriceMap = new HashMap<String, Object>();
		sellingPriceMap.put("type", "keyword");
		propertiesMap.put("sellingPrice", sellingPriceMap);

		Map<String, Object> costPriceMap = new HashMap<String, Object>();
		costPriceMap.put("type", "keyword");
		propertiesMap.put("costPrice", costPriceMap);

		Map<String, Object> quantitiesMap = new HashMap<String, Object>();
		quantitiesMap.put("type", "keyword");
		propertiesMap.put("quantities", quantitiesMap);

		Map<String, Object> mainCategoryNameMap = new HashMap<String, Object>();
		mainCategoryNameMap.put("type", "text");
		propertiesMap.put("mainCategoryName", mainCategoryNameMap);

		Map<String, Object> subCategoryNameMap = new HashMap<String, Object>();
		subCategoryNameMap.put("type", "text");
		propertiesMap.put("subCategoryName", subCategoryNameMap);

		Map<String, Object> mainCategoryIdMap = new HashMap<String, Object>();
		mainCategoryIdMap.put("type", "keyword");
		propertiesMap.put("mainCategoryId", mainCategoryIdMap);

		Map<String, Object> subCategoryIdMap = new HashMap<String, Object>();
		subCategoryIdMap.put("type", "keyword");
		propertiesMap.put("subCategoryId", subCategoryIdMap);

		jsonMap.put("properties", propertiesMap);

		request.mapping(jsonMap);

		try {
			CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
			boolean acknowledged = createIndexResponse.isAcknowledged();
			System.out.println("Mapping for product Indices:- " + acknowledged);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
