package com.axis.repository;

import java.util.List;

import com.axis.model.Product;

public interface ProductRepository {

	List<Product> getAllProducts();

	Product saveProduct(Product product);

	Product getProductById(String productId);

	List<Product> getProductsByMainCategoryId(String mainCategoryId);

	List<Product> getProductsBySubCategoryId(String subCategoryId);

	String deleteProductById(String productId);

	Product updateProductById(Product product);
}
