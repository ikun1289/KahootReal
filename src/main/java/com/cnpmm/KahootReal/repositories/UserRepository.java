package com.cnpmm.KahootReal.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cnpmm.KahootReal.model.User;

public interface UserRepository extends MongoRepository<User,String>{

	public User findByUserName(String name);
	
}
