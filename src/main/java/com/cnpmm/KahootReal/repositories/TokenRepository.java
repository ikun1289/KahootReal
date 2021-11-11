package com.cnpmm.KahootReal.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cnpmm.KahootReal.model.VerifyToken;



@Repository
public interface TokenRepository extends MongoRepository<VerifyToken,String>{

	public VerifyToken findByUserId(String userId);
	
	public VerifyToken findByToken(String Token);
	
	public void deleteByToken(String token);
	
}
