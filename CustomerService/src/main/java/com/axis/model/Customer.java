package com.axis.model;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "customer")
public class Customer {
	@DynamoDBHashKey
	private String userId;
	@DynamoDBAttribute
	private String username;
	@DynamoDBAttribute
	private String name;
	@DynamoDBAttribute
	private String gender;
	@DynamoDBAttribute
	private String email;
	@DynamoDBAttribute
	private List<CustomerAddress> customerAddresses = new ArrayList<CustomerAddress>();
	

	public Customer() {
		super();
	}

	public Customer(String userId) {
		super();
		this.userId = userId;
	}
	
	public Customer(String userId, String username, String name, String gender, String email) {
		super();
		this.userId = userId;
		this.username = username;
		this.name = name;
		this.gender = gender;
		this.email = email;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public List<CustomerAddress> getCustomerAddresses() {
		return customerAddresses;
	}
	
	public void setCustomerAddresses(List<CustomerAddress> customerAddresses) {
		this.customerAddresses = customerAddresses;
	}

	@Override
	public String toString() {
		return "Customer [userId=" + userId + ", username=" + username + ", name=" + name + ", gender=" + gender
				+ ", email=" + email + "]";
	}

}