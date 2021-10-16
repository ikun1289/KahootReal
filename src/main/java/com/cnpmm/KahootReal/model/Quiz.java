package com.cnpmm.KahootReal.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class Quiz {

	@Id
	private ObjectId id = new ObjectId();
	private String content;
	private List<Answer> aList = new ArrayList<Answer>();

	
	
	public Quiz() {
		super();
	}



	public Quiz(String content, List<Answer> aList) {
		super();
		this.content = content;
		this.aList = aList;
	}



	public ObjectId getId() {
		return id;
	}



	public void setId(ObjectId id) {
		this.id = id;
	}



	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}



	public List<Answer> getaList() {
		return aList;
	}



	public void setaList(List<Answer> aList) {
		this.aList = aList;
	}
	
	
	
}
