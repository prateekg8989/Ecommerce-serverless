package com.axis.config;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;

public class ESConfig {
	private static String serviceName = "es";
	private static String region = "us-east-2";
	private static String aesEndpoint = ""; // e.g.
																															// https://search-mydomain.us-west-1.es.amazonaws.com
	private static String index = "products";
	private static String type = "_doc";
	private static String id = "1";
	static final AWSCredentialsProvider credentialsProvider = new DefaultAWSCredentialsProviderChain();

	public static RestHighLevelClient getInstance() {

		AWS4Signer signer = new AWS4Signer();
		signer.setServiceName(serviceName);
		signer.setRegionName(region);
		HttpRequestInterceptor interceptor = new AWSRequestSigningApacheInterceptor(serviceName, signer,
				credentialsProvider);
		return new RestHighLevelClient(RestClient.builder(HttpHost.create(aesEndpoint))
				.setHttpClientConfigCallback(hacb -> hacb.addInterceptorLast(interceptor)));

	}
}
