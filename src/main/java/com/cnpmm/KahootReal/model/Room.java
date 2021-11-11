package com.cnpmm.KahootReal.model;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;


@Document(collection = "Room")
@Data
public class Room {

	@Id
	private ObjectId id = new ObjectId();
	private String name;
	private String pinCode;
	private Boolean isOpen;
	private String creatorID;
	private int time = 0;
	private List<Guest> guests;
	private List<Quiz> quizs;
	public Room() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Room(String name, String pinCode, Boolean isOpen, String creatorID, int time,
			List<Guest> guests, List<Quiz> quizs) {
		super();
		this.name = name;
		this.pinCode = pinCode;
		this.isOpen = isOpen;
		this.creatorID = creatorID;
		this.time = time;
		this.guests = guests;
		this.quizs = quizs;
	}
	
	
	
	
}
