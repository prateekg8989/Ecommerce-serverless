package com.axis.repository;

import java.util.List;

import com.axis.model.Customer;
import com.axis.model.CustomerAddress;

public interface CustomerRepository {
	public Customer getCustomerByUserId(String userId);

	public Customer addCustomer(Customer customer);

	public Customer editCustomer(Customer customer);

	public List<CustomerAddress> getAllAddressesOfCustomer(String userId);

	public void addAddressToCustomer(String userId, CustomerAddress customerAddress);

	public void deleteAddressOfCustomer(String userId, String addressId);
}
