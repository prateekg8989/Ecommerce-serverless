package com.axis.repository;

import java.util.List;

import com.axis.model.Order;

public interface OrderRepository {
	public List<Order> getAllOrdersForUser(String userId);

	public Order placeOrder(String userId, String addressId);
}
