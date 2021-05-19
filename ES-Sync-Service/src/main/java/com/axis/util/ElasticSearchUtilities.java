package com.axis.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.Build.Type;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
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

	private RestHighLevelClient client;
	
	private static String IndexName = "products2";

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
		DeleteRequest request = new DeleteRequest(IndexName, prodId);
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
		searchSourceBuilder.query(QueryBuilders.matchQuery("mainCategoryId", mainCategoryId));
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
		searchSourceBuilder.query(QueryBuilders.matchQuery("subCategoryId", subCategoryId));
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

}
