package com.axis.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGenerated;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBDocument
public class SubCategory {
	@DynamoDBAttribute
	private String subCategoryId;
	@DynamoDBAttribute
	private String subCategoryName;
	@DynamoDBAttribute
	private String subCategoryDesc;

	public SubCategory() {
		super();
	}

	public SubCategory(String subCategoryId, String subCategoryName, String subCategoryDesc) {
		super();
		this.subCategoryId = subCategoryId;
		this.subCategoryName = subCategoryName;
		this.subCategoryDesc = subCategoryDesc;
	}

	public String getSubCategoryId() {
		return subCategoryId;
	}

	public void setSubCategoryId(String subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	public String getSubCategoryName() {
		return subCategoryName;
	}

	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}

	public String getSubCategoryDesc() {
		return subCategoryDesc;
	}

	public void setSubCategoryDesc(String subCategoryDesc) {
		this.subCategoryDesc = subCategoryDesc;
	}

	@Override
	public String toString() {
		return "SubCategory [subCategoryId=" + subCategoryId + ", subCategoryName=" + subCategoryName
				+ ", subCategoryDesc=" + subCategoryDesc + "]";
	}

	@Override
	public boolean equals(Object obj) {
		SubCategory other = (SubCategory) obj;
		return subCategoryId.equals(other.getSubCategoryId());
	}

}
