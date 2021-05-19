package com.axis.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.axis.model.User;
@Component
public class UserRepositoryImpl implements UserRepository {
	@Autowired
	private DynamoDBMapper mapper;

	@Override
	public User findByUsername(String username) {
		DynamoDBQueryExpression<User> query = new DynamoDBQueryExpression<User>()
				.withHashKeyValues(new User(null, username, null, null, null)).withConsistentRead(false);
		List<User> userList = mapper.query(User.class, query);
		if(userList.size() == 0) {
			return null;
		} else {
			return userList.get(0);	
		}
	}

	@Override
	public Boolean existsByUsername(String username) {

//		Condition condition = new Condition();
//		condition.withComparisonOperator(ComparisonOperator.EQ)
//				.withAttributeValueList(new AttributeValue().withS(username));
		DynamoDBQueryExpression<User> query = new DynamoDBQueryExpression<User>()
				.withHashKeyValues(new User(null, username, null, null, null)).withConsistentRead(false);
		;
		List<User> userList = mapper.query(User.class, query);

//		User user = mapper.load(User.class, username);
//		return user == null ? false : true;
		return userList.size() == 0 ? false : true;
	}

	@Override
	public Boolean existsByEmail(String username, String email) {
		Condition condition = new Condition();
		condition.withComparisonOperator(ComparisonOperator.EQ)
				.withAttributeValueList(new AttributeValue().withS(email));
		DynamoDBQueryExpression<User> query = new DynamoDBQueryExpression<User>()
				.withHashKeyValues(new User(null, username, null, null, null)).withRangeKeyCondition("email", condition)
				.withConsistentRead(false);
		List<User> userList = mapper.query(User.class, query);
		return userList.size() == 0 ? false : true;
	}

	@Override
	public User addUser(User user) {
		mapper.save(user);
		return user;
	}

}
