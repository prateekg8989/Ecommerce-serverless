package com.axis.repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.axis.model.MainCategory;
import com.axis.model.SubCategory;

@Component
public class MainCategoryRepository {

	@Autowired
	private DynamoDBMapper mapper;

	public List<MainCategory> getAllMainCategories() {
		return mapper.scan(MainCategory.class, new DynamoDBScanExpression());
	}

	public MainCategory saveMainCategory(MainCategory mainCategory) {
		mapper.save(mainCategory);
		return mainCategory;
	}

	public MainCategory getMainCategoryById(String mainCategoryId) {
		return mapper.load(MainCategory.class, mainCategoryId);
	}

	public String deleteMainCategoryById(String mainCategoryId) {
		mapper.delete(new MainCategory(mainCategoryId, null, null, null));
		return "product deleted with id:- " + mainCategoryId;
	}

	public MainCategory saveSubCategoryToMainCategory(SubCategory subCategory, String mainCategoryId) {
		MainCategory mainCategory = mapper.load(MainCategory.class, mainCategoryId);
		if (mainCategory.getSubCategories() == null) {
			List<SubCategory> arr = new ArrayList<SubCategory>();
			subCategory.setSubCategoryId("1-" + mainCategoryId);
			arr.add(subCategory);
			mainCategory.setSubCategories(arr);
		} else {
			subCategory.setSubCategoryId((mainCategory.getSubCategories().size() + 1) + "-" + mainCategoryId);
			mainCategory.getSubCategories().add(subCategory);
		}

		mapper.save(mainCategory);
		return mainCategory;
	}

	public MainCategory deleteSubCategoryById(String mainCategoryId, String subCategoryId) {
		MainCategory mainCategory = mapper.load(MainCategory.class, mainCategoryId);
		if (mainCategory.getSubCategories() != null) {
			SubCategory subCategory = new SubCategory(subCategoryId, null, null);
			mainCategory.getSubCategories().remove(subCategory);
		}
		mapper.save(mainCategory);
		return mainCategory;
	}

}
