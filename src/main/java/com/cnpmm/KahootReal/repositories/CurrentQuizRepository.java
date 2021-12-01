package com.cnpmm.KahootReal.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cnpmm.KahootReal.model.CurrentQuiz;

@Repository
public interface CurrentQuizRepository extends MongoRepository<CurrentQuiz,String>{

}
