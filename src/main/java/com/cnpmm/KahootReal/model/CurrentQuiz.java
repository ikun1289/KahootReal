package com.cnpmm.KahootReal.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "CurrentQuiz")
@Data
public class CurrentQuiz {
	private ObjectId id = new ObjectId();
	private String roomId;
	private String currentQuizId;
	private long startTime;
	
	
	
	public CurrentQuiz(String roomId, String currentQuizId, long startTime) {
		super();
		this.roomId = roomId;
		this.currentQuizId = currentQuizId;
		this.startTime = startTime;
	}



	public CurrentQuiz() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
