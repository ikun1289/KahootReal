package com.cnpmm.KahootReal.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.cnpmm.KahootReal.model.Quiz;
import com.cnpmm.KahootReal.model.Room;
import com.cnpmm.KahootReal.repositories.RoomRepository;

@Service
public class QuizService {

	@Autowired
	RoomRepository roomRepository;
	@Autowired
	MongoTemplate mongoTemplate;
	
	public List<Quiz> getQuizsByRoomID(String roomID) {
		Optional<Room> room =  roomRepository.findById(roomID);
		if(room.isPresent()) {
			return room.get().getQuizs();
		}
		return null;
	}
	
	public void addNewQuizsToRoom(String roomID, List<Quiz> quiz) {
		Query query = new Query(Criteria.where("id").is(roomID));
		Update update = new Update().push("quizs").each(quiz);
		
		
		this.mongoTemplate.findAndModify(query, update, Room.class);
		//this.mongoTemplate.find(query, Classroom.class);
	}
	
}
