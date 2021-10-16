package com.cnpmm.KahootReal.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import com.sun.org.apache.xpath.internal.operations.Bool;

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
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public Boolean getIsCorrect() {
		return isCorrect;
	}
	public void setIsCorrect(Boolean isCorrect) {
		this.isCorrect = isCorrect;
	}
	
	
	
}
