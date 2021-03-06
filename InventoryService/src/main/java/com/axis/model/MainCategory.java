package com.axis.model;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "mainCategory")
public class MainCategory {
	@DynamoDBHashKey
	@DynamoDBAutoGeneratedKey
	private String mainCategoryId;
	@DynamoDBAttribute
	private String mainCategoryName;
	@DynamoDBAttribute
	private String mainCategoryDesc;
	@DynamoDBAttribute
	List<SubCategory> subCategories = new ArrayList<SubCategory>();

	public MainCategory() {
		super();
	}

	public MainCategory(String mainCategoryId, String mainCategoryName, String mainCategoryDesc,
			List<SubCategory> subCategories) {
		super();
		this.mainCategoryId = mainCategoryId;
		this.mainCategoryName = mainCategoryName;
		this.mainCategoryDesc = mainCategoryDesc;
		this.subCategories = subCategories;
	}

	public String getMainCategoryId() {
		return mainCategoryId;
	}

	public void setMainCategoryId(String mainCategoryId) {
		this.mainCategoryId = mainCategoryId;
	}

	public String getMainCategoryName() {
		return mainCategoryName;
	}

	public void setMainCategoryName(String mainCategoryName) {
		this.mainCategoryName = mainCategoryName;
	}

	public String getMainCategoryDesc() {
		return mainCategoryDesc;
	}

	public void setMainCategoryDesc(String mainCategoryDesc) {
		this.mainCategoryDesc = mainCategoryDesc;
	}

	public List<SubCategory> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(List<SubCategory> subCategories) {
		this.subCategories = subCategories;
	}

	@Override
	public String toString() {
		return "MainCategory [mainCategoryId=" + mainCategoryId + ", mainCategoryName=" + mainCategoryName
				+ ", mainCategoryDesc=" + mainCategoryDesc + ", subCategories=" + subCategories + "]";
	}

}
