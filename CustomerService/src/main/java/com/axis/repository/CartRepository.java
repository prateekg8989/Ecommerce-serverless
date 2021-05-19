package com.axis.repository;

import com.axis.model.Cart;
import com.axis.model.CartItem;

public interface CartRepository {
	public Cart addItemToCart(String userId, CartItem cartItem);
	public void updateItemQuantityToCart(int newQuantity, String userId, String cartItemId);
	public void removeCartItemFromCart(String userId, String cartItemId);
	public void clearTheCart(String userId);
	Cart getTheCart(String userId);
}
