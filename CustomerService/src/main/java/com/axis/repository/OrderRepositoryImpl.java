package com.axis.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.axis.model.Cart;
import com.axis.model.CartItem;
import com.axis.model.Customer;
import com.axis.model.CustomerAddress;
import com.axis.model.Order;
import com.axis.model.OrderItem;

@Component
public class OrderRepositoryImpl implements OrderRepository {
	@Autowired
	private DynamoDBMapper mapper;

	@Override
	public List<Order> getAllOrdersForUser(String userId) {

		Condition rangeKeyCondition = new Condition();
		rangeKeyCondition.withComparisonOperator(ComparisonOperator.EQ)
				.withAttributeValueList(new AttributeValue().withS(userId));
		DynamoDBScanExpression scanExpr = new DynamoDBScanExpression();
		scanExpr.withFilterConditionEntry("userId", rangeKeyCondition);
		return mapper.scan(Order.class, scanExpr);
	}

	@Override
	public Order placeOrder(String userId, String addressId) {
		Cart cart = mapper.load(Cart.class, userId);

		List<CartItem> cartItems = cart.getCartItems();
		if (cartItems != null && cartItems.size() != 0) {
			Order order = new Order();
			order.setUserId(userId);
			List<OrderItem> orderItems = new ArrayList<OrderItem>();
			int totalAmount = 0;
			for (CartItem cartItem : cartItems) {
				OrderItem orderItem = new OrderItem(cartItem.getProductId(), cartItem.getProductName(),
						cartItem.getSellingPrice(), cartItem.getCostPrice(), cartItem.getImageUrls1(),
						cartItem.getQuantities());
				orderItems.add(orderItem);
				totalAmount += (orderItem.getQuantities() * orderItem.getSellingPrice());
			}
			order.setOrderItem(orderItems);
			Customer customer = mapper.load(Customer.class, userId);
			List<CustomerAddress> addresses = customer.getCustomerAddresses();
			CustomerAddress customerAddress = null;
			for (CustomerAddress ele : addresses) {
				if (ele.getCustAddrId().equals(addressId)) {
					customerAddress = ele;
				}
			}
			order.setEmail(customer.getEmail());
			order.setOrderDate(new Date());
			order.setCustAddrId(customerAddress.getCustAddrId());
			order.setAddress(customerAddress.getAddress());
			order.setPinCode(customerAddress.getPinCode());
			order.setRecepientName(customerAddress.getRecepientName());
			order.setStatus("PLACED");
			order.setTotalAmount(totalAmount);
			mapper.save(order);

			cart.setCartItems(new ArrayList<CartItem>());
			mapper.save(cart);
			return order;
		}
		return null;
	}
}
