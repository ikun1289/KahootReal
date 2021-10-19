package com.cnpmm.KahootReal.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Answer {

	@Id
	private ObjectId id = new ObjectId();
	private String answer;
	private Boolean isCorrect;
	public Answer() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Answer(String answer, Boolean isCorrect) {
		super();
		this.answer = answer;
		this.isCorrect = isCorrect;
	}
	
	
	
	
}
