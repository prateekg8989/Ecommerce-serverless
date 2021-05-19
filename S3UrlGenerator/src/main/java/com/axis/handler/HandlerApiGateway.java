package com.axis.handler;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

public class HandlerApiGateway implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		Regions clientRegion = Regions.US_EAST_2;
		String bucketName = "";
		Map<String, String> mapOfQueryParams = input.getQueryStringParameters();
		String objectKey = mapOfQueryParams.get("name");
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
				.withCredentials(DefaultAWSCredentialsProviderChain.getInstance()).build();
//		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(new ProfileCredentialsProvider())
//				.withRegion(clientRegion).build();

		// Set the pre-signed URL to expire after one hour.
		java.util.Date expiration = new java.util.Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += 1000 * 60 * 5;
		expiration.setTime(expTimeMillis);

		// Generate the pre-signed URL.
		System.out.println("Generating pre-signed URL.");

		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey)
				.withMethod(HttpMethod.PUT).withExpiration(expiration);
//		generatePresignedUrlRequest.setContentType("image/*");
//		generatePresignedUrlRequest.addRequestParameter("x-amz-acl", "public-read");
		URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
		APIGatewayProxyResponseEvent res = new APIGatewayProxyResponseEvent();
		res.setStatusCode(200);
		Map<String, String> map = new HashMap<String, String>();
		map.put("Content-Type", "text/plain");
		map.put("Access-Control-Allow-Headers", "*");
		map.put("Access-Control-Allow-Origin", "*");
		map.put("Access-Control-Allow-Methods", "*");
		res.withHeaders(map);
		res.setBody(url.toString());
		return res;
	}

}
