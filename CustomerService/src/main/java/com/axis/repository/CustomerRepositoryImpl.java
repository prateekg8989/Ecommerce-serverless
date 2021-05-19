package com.axis.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDeleteExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.axis.model.Cart;
import com.axis.model.CartItem;
import com.axis.model.Customer;
import com.axis.model.CustomerAddress;

@Component
public class CustomerRepositoryImpl implements CustomerRepository {

	@Autowired
	private DynamoDBMapper mapper;

	@Override
	public Customer getCustomerByUserId(String userId) {
		Customer savedCustomer = mapper.load(Customer.class, userId);
		return savedCustomer;
	}

	@Override
	public Customer addCustomer(Customer customer) {
		List<CartItem> l = new ArrayList<CartItem>();
		Cart cart = new Cart(customer.getUserId(), l);
		mapper.save(cart);
		mapper.save(customer);
		return customer;
	}

	@Override
	public Customer editCustomer(Customer customer) {
		Customer savedCustomer = mapper.load(Customer.class, customer.getUserId());
		String gender = customer.getGender();
		if (gender != null) {
			savedCustomer.setGender(gender);
		}

		String name = customer.getName();
		if (name != null) {
			savedCustomer.setName(name);
		}
		mapper.save(savedCustomer);
		return savedCustomer;
	}

	@Override
	public List<CustomerAddress> getAllAddressesOfCustomer(String userId) {
		return getCustomerByUserId(userId).getCustomerAddresses();
	}

	@Override
	public void addAddressToCustomer(String userId, CustomerAddress customerAddress) {
		Customer customer = getCustomerByUserId(userId);
		List<CustomerAddress> custAddresses = customer.getCustomerAddresses();
		if (custAddresses == null) {
			custAddresses = new ArrayList<CustomerAddress>();
		}
		customerAddress.setCustAddrId(userId + "-" + custAddresses.size());
		custAddresses.add(customerAddress);
		customer.setCustomerAddresses(custAddresses);
		mapper.save(customer);
	}

	@Override
	public void deleteAddressOfCustomer(String userId, String addressId) {
		Customer customer = getCustomerByUserId(userId);
		List<CustomerAddress> custAddresses = customer.getCustomerAddresses();
		if (custAddresses != null && custAddresses.size() > 0) {
			custAddresses.remove(new CustomerAddress(userId, addressId, null, null, null));
		}
		mapper.save(customer);
	}
}
