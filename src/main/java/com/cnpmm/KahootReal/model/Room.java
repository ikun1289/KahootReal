package com.cnpmm.KahootReal.model;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Room")
public class Room {

	@Id
	private ObjectId id = new ObjectId();
	private String name;
	private String pinCode;
	private Boolean isOpen;
	private ObjectId creatorID;
	private List<Guest> guests;
	private List<Quiz> quizs;
	public Room() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Room(String name, String pinCode, Boolean isOpen, ObjectId creatorID, List<Guest> guests,
			List<Quiz> quizs) {
		super();
		this.name = name;
		this.pinCode = pinCode;
		this.isOpen = isOpen;
		this.creatorID = creatorID;
		this.guests = guests;
		this.quizs = quizs;
	}
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPinCode() {
		return pinCode;
	}
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	public Boolean getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(Boolean isOpen) {
		this.isOpen = isOpen;
	}
	public ObjectId getCreatorID() {
		return creatorID;
	}
	public void setCreatorID(ObjectId creatorID) {
		this.creatorID = creatorID;
	}
	public List<Guest> getGuests() {
		return guests;
	}
	public void setGuests(List<Guest> guests) {
		this.guests = guests;
	}
	public List<Quiz> getQuizs() {
		return quizs;
	}
	public void setQuizs(List<Quiz> quizs) {
		this.quizs = quizs;
	}
	
	
}
