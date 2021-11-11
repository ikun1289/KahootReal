package com.cnpmm.KahootReal.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.cnpmm.KahootReal.model.Room;
import com.cnpmm.KahootReal.repositories.RoomRepository;


@Service
public class RoomServices {

	@Autowired
	private RoomRepository roomRepository;
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public List<Room> getAllRoom() {
		return roomRepository.findAll();
	}
	
	public Optional<Room> getRoomByID(String roomID) {
		return roomRepository.findById(roomID);
	}
	
	public List<Room> getRoomByCreatorID(String creatorID)
	{
		return roomRepository.findByCreatorID(creatorID);
	}
	
	public Room getRoomByName(String name) {
		return roomRepository.findByName(name);
	}
	
	public void addNewRoom(Room room)
	{
		roomRepository.save(room);
	}
	
	public Room getRoomByQuizID(String id)
	{
		return roomRepository.findByQuizs_Id(id);
	}
	
	public Room getRoomByAnswerID(String id)
	{
		return roomRepository.findByQuizs_aList_Id(id);
	}
	
	public Room getRoomByPincode(String pin)
	{
		return roomRepository.findByPinCode(pin);
	}
	
	public void openRoomWithPin(String pin) {
		Query query = new Query(Criteria.where("pinCode").is(pin));
		Update update = new Update().set("isOpen", true);
		this.mongoTemplate.findAndModify(query, update, Room.class);
	}
	
	public void closeRoomWithPin(String pin) {
		Query query = new Query(Criteria.where("pinCode").is(pin));
		Update update = new Update().set("isOpen", false);
		this.mongoTemplate.findAndModify(query, update, Room.class);
	}
}
