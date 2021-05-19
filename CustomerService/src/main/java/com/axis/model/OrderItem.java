package com.axis.model;

import java.util.Set;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

@DynamoDBDocument
public class OrderItem {
	@DynamoDBAttribute
	private String productId;
	@DynamoDBAttribute
	private String productName;
	@DynamoDBAttribute
	private int sellingPrice;
	@DynamoDBAttribute
	private int costPrice;
	@DynamoDBAttribute
	private Set<String> imageUrls1;
	@DynamoDBAttribute
	private int quantities;

	public OrderItem() {
		super();
	}

	public OrderItem(String productId, String productName, int sellingPrice, int costPrice, Set<String> imageUrls1,
			int quantities) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.sellingPrice = sellingPrice;
		this.costPrice = costPrice;
		this.imageUrls1 = imageUrls1;
		this.quantities = quantities;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(int sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public int getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(int costPrice) {
		this.costPrice = costPrice;
	}

	public Set<String> getImageUrls1() {
		return imageUrls1;
	}

	public void setImageUrls1(Set<String> imageUrls1) {
		this.imageUrls1 = imageUrls1;
	}

	public int getQuantities() {
		return quantities;
	}

	public void setQuantities(int quantities) {
		this.quantities = quantities;
	}

	@Override
	public String toString() {
		return "OrderItem [productId=" + productId + ", productName=" + productName + ", sellingPrice=" + sellingPrice
				+ ", costPrice=" + costPrice + ", imageUrls1=" + imageUrls1 + ", quantities=" + quantities + "]";
	}

}
