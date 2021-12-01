package com.cnpmm.KahootReal.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.cnpmm.KahootReal.model.CurrentQuiz;
import com.cnpmm.KahootReal.repositories.CurrentQuizRepository;

@Service
public class CurrentQuizService {
	@Autowired
	private CurrentQuizRepository currentQuizRepository;
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public CurrentQuiz getCurrentQuizByRoomId(String roomId)
	{
		Query query = new Query(Criteria.where("roomId").is(roomId));
		return this.mongoTemplate.findOne(query, CurrentQuiz.class);
	}
	
	public CurrentQuiz getCurrentQuizByRoomAndQuiz(String roomId, String quizId)
	{
		Query query = new Query(Criteria.where("roomId").is(roomId).andOperator(Criteria.where("currentQuizId").is(quizId)));
		return this.mongoTemplate.findOne(query, CurrentQuiz.class);
	}
	
	public void setCurrentQuiz(String roomId)
	{
		CurrentQuiz currentQuiz = new CurrentQuiz(roomId,"",new Date().getTime());
		currentQuizRepository.save(currentQuiz);
//		Query query = new Query(Criteria.where("roomId").is(roomId).andOperator(Criteria.where("currentQuizId").is(quizId)));
//		Update update = new Update().set("roomId", roomId).set("currentQuizId", quizId).set("startTime", new Date().getTime());
//		this.mongoTemplate.findAndModify(query, update,new FindAndModifyOptions().upsert(true), CurrentQuiz.class);
	}
	
	public void updateCurrentQuiz(String roomId, String quizId)
	{
		Query query = new Query(Criteria.where("roomId").is(roomId));
		Update update = new Update().set("roomId", roomId).set("currentQuizId", quizId).set("startTime", new Date().getTime());
		this.mongoTemplate.findAndModify(query, update, CurrentQuiz.class);
	}
	
	public void deleteCurrentQuiz(String roomId)
	{
		Query query = new Query(Criteria.where("roomId").is(roomId));
		this.mongoTemplate.remove(query, CurrentQuiz.class);
	}
	
}
