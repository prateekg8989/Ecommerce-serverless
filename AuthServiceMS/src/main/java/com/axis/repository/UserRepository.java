package com.axis.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.axis.model.User;


public interface UserRepository {
	User addUser(User user);
	User findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String username, String email);
}