package com.axis.model;

import java.util.Set;

public class Product {

	private String id;
	private String productId;

	private String productName;
	private String productSummary;
	private String productDesc;

	private int views;

	private int sellingPrice;
	private int costPrice;
	private int quantities;
	private String mainCategoryName;
	private String subCategoryName;
	private Set<String> imageUrls1;

	private String mainCategoryId;
	private String subCategoryId;

	public Product() {
		super();
	}

	public Product(String id, String productId, String productName, String productSummary, String productDesc,
			int views, int sellingPrice, int costPrice, int quantities, Set<String> imageUrls) {
		super();

		this.id = id;
		this.productId = productId;
		this.productName = productName;
		this.productSummary = productSummary;
		this.productDesc = productDesc;
		this.views = views;
		this.costPrice = costPrice;
		this.sellingPrice = sellingPrice;
		this.quantities = quantities;
	}

	public Product(String productId, String productName, String productSummary, String productDesc, int views,
			int sellingPrice, int costPrice, int quantities) {
		super();
		this.id = id;
		this.productId = productId;
		this.productName = productName;
		this.productSummary = productSummary;
		this.productDesc = productDesc;
		this.views = views;
		this.costPrice = costPrice;
		this.sellingPrice = sellingPrice;
		this.quantities = quantities;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.id = productId;
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductSummary() {
		return productSummary;
	}

	public void setProductSummary(String productSummary) {
		this.productSummary = productSummary;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public int getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(int costPrice) {
		this.costPrice = costPrice;
	}

	public int getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(int sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public int getQuantities() {
		return quantities;
	}

	public void setQuantities(int quantities) {
		this.quantities = quantities;
	}

	public String getMainCategoryName() {
		return mainCategoryName;
	}

	public void setMainCategoryName(String mainCategoryName) {
		this.mainCategoryName = mainCategoryName;
	}

	public String getSubCategoryName() {
		return subCategoryName;
	}

	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}

	public String getSubCategoryId() {
		return subCategoryId;
	}

	public void setSubCategoryId(String subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	public String getMainCategoryId() {
		return mainCategoryId;
	}

	public void setMainCategoryId(String mainCategoryId) {
		this.mainCategoryId = mainCategoryId;
	}

	public Set<String> getImageUrls1() {
		return imageUrls1;
	}

	public void setImageUrls1(Set<String> imageUrls) {
		this.imageUrls1 = imageUrls;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productName=" + productName + ", productSummary=" + productSummary
				+ ", productDesc=" + productDesc + ", views=" + views + ", sellingPrice=" + sellingPrice
				+ ", costPrice=" + costPrice + ", quantities=" + quantities + ", mainCategoryName=" + mainCategoryName
				+ ", subCategoryName=" + subCategoryName + ", imageUrls1=" + String.join(" ", imageUrls1)
				+ ", mainCategoryId=" + mainCategoryId + ", subCategoryId=" + subCategoryId + "]";
	}

}
