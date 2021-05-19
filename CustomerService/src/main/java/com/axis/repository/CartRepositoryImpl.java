package com.axis.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.axis.model.Cart;
import com.axis.model.CartItem;
import com.axis.model.Customer;

@Component
public class CartRepositoryImpl implements CartRepository {
	@Autowired
	private DynamoDBMapper mapper;

	@Override
	public Cart addItemToCart(String userId, CartItem cartItem) {
		Cart cart = mapper.load(Cart.class, userId);
		List<CartItem> cartItems = cart.getCartItems();
		if (cartItems == null || cartItems.size() == 0) {
			cartItems = new ArrayList<CartItem>();
		}
		cartItems.add(cartItem);
		cart.setCartItems(cartItems);
		mapper.save(cart);
		return cart;
	}

	@Override
	public void updateItemQuantityToCart(int newQuantity, String userId, String cartItemId) {

		if (newQuantity == 0) {
			removeCartItemFromCart(userId, cartItemId);
		} else {
			Cart cart = mapper.load(Cart.class, userId);
			List<CartItem> cartItems = cart.getCartItems();
			for (CartItem cartItem : cartItems) {
				if (cartItem.getProductId().equals(cartItemId)) {
					cartItem.setQuantities(newQuantity);
				}
			}
			mapper.save(cart);
		}
	}

	@Override
	public void removeCartItemFromCart(String userId, String cartItemId) {
		Cart cart = mapper.load(Cart.class, userId);
		List<CartItem> cartItems = cart.getCartItems();
		List<CartItem> newCartItems = new ArrayList<CartItem>();
		for (CartItem cartItem : cartItems) {
			if (!cartItem.getProductId().equals(cartItemId)) {
				newCartItems.add(cartItem);
			}
		}
		cart.setCartItems(newCartItems);
		mapper.save(cart);
	}

	@Override
	public void clearTheCart(String userId) {
		Cart cart = mapper.load(Cart.class, userId);
		cart.setCartItems(new ArrayList<CartItem>());
		mapper.save(cart);
	}

	@Override
	public Cart getTheCart(String userId) {
		Cart cart = mapper.load(Cart.class, userId);
		return cart;
	}

}
