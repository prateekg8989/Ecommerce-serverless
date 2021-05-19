package com.axis.repository;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.axis.model.MainCategory;
import com.axis.model.Product;
import com.axis.model.SubCategory;

@Component
public class ProductRepositoryImpl implements ProductRepository {

	@Autowired
	private DynamoDBMapper mapper;

	public List<Product> getAllProducts() {
		return mapper.scan(Product.class, new DynamoDBScanExpression());
	}

	public Product saveProduct(Product product) {
		MainCategory mainCategory = mapper.load(MainCategory.class, product.getMainCategoryId());
		product.setMainCategoryName(mainCategory.getMainCategoryName());
		String subCategoryName = "";
		List<SubCategory> subCategories = mainCategory.getSubCategories();
		for (SubCategory subCategory : subCategories) {
			if (subCategory.getSubCategoryId().equals(product.getSubCategoryId())) {
				subCategoryName = subCategory.getSubCategoryName();
			}
		}
		product.setSubCategoryName(subCategoryName);
		mapper.save(product);
		return product;
	}

	public Product getProductById(String productId) {
		return mapper.load(Product.class, productId);
	}

	public List<Product> getProductsByMainCategoryId(String mainCategoryId) {

		Condition rangeKeyCondition = new Condition();
		rangeKeyCondition.withComparisonOperator(ComparisonOperator.EQ)
				.withAttributeValueList(new AttributeValue().withS(mainCategoryId));
		DynamoDBScanExpression scanExpr = new DynamoDBScanExpression();
		scanExpr.withFilterConditionEntry("mainCategoryId", rangeKeyCondition);
		return mapper.scan(Product.class, scanExpr);
	}

	public List<Product> getProductsBySubCategoryId(String subCategoryId) {
		Condition rangeKeyCondition = new Condition();
		rangeKeyCondition.withComparisonOperator(ComparisonOperator.EQ)
				.withAttributeValueList(new AttributeValue().withS(subCategoryId));
		DynamoDBScanExpression scanExpr = new DynamoDBScanExpression();
		scanExpr.withFilterConditionEntry("subCategoryId", rangeKeyCondition);
		return mapper.scan(Product.class, scanExpr);
	}

	public String deleteProductById(String productId) {
		mapper.delete(new Product(productId, null, null, null, 0, 0, 0, 0, null, null, null, null, null));
		return "product deleted with id:- " + productId;
	}

	public Product updateProductById(Product product) {
		Product savedProduct = mapper.load(Product.class, product.getProductId());
		if (product.getProductName() != null) {
			savedProduct.setProductName(product.getProductName());
		}
		if (product.getProductSummary() != null) {
			savedProduct.setProductSummary(product.getProductSummary());
		}
		if (product.getProductDesc() != null) {
			savedProduct.setProductDesc(product.getProductDesc());
		}

		int views = product.getViews();
		if (views != 0) {
			savedProduct.setViews(views);
		}
		int costPrice = product.getCostPrice();
		if (costPrice != 0) {
			savedProduct.setCostPrice(costPrice);
		}
		int sellingPrice = product.getSellingPrice();
		if (sellingPrice != 0) {
			savedProduct.setSellingPrice(sellingPrice);
		}
		int quantities = product.getQuantities();
		savedProduct.setQuantities(quantities);
		if (product.getImageUrls1() != null) {
			savedProduct.setImageUrls1(product.getImageUrls1());
		}
		mapper.save(savedProduct);
		return savedProduct;
	}
}
