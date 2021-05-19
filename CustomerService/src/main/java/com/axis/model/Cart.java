package com.axis.model;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "cart")
public class Cart {
	@DynamoDBHashKey
	private String userId;
	@DynamoDBAttribute
	List<CartItem> cartItems = new ArrayList<CartItem>();

	public Cart() {
		super();
	}

	public Cart(String userId, List<CartItem> cartItems) {
		super();
		this.userId = userId;
		this.cartItems = cartItems;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	@Override
	public String toString() {
		return "Cart [userId=" + userId + ", cartItems=" + cartItems + "]";
	}

}
