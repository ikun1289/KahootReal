package com.cnpmm.KahootReal.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
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

}
