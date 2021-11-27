package com.cnpmm.KahootReal.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.cnpmm.KahootReal.RandomString;
import com.cnpmm.KahootReal.model.Guest;
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
	
	public String openRoomWithId(String id) {
		String generatedPin = new RandomString(9).nextString();
		Query query = new Query(Criteria.where("id").is(id));
		Update update = new Update().set("isOpen", true).set("pinCode", generatedPin).set("guests", new ArrayList<>());
		this.mongoTemplate.findAndModify(query, update, Room.class);
		return generatedPin;
	}
	
	public void closeRoomWithId(String id) {
		Query query = new Query(Criteria.where("id").is(id));
		Update update = new Update().set("isOpen", false);
		this.mongoTemplate.findAndModify(query, update, Room.class);
	}
	
	public void deleteRoomWithId(String roomid)
	{
		roomRepository.deleteById(roomid);
	}

	public List<Guest> addNewGuest(String pin,String name) {
		// TODO Auto-generated method stub
		Guest guest = new Guest();
		guest.setName(name);
		guest.setScore(0);
		Query query = new Query(Criteria.where("pinCode").is(pin));
		Update update = new Update().push("guests", guest);
		this.mongoTemplate.updateFirst(query, update, Room.class);
		return this.mongoTemplate.findOne(query, Room.class).getGuests();
	}

	public boolean checkGuestname(String pin,String string) {
		// TODO Auto-generated method stub
		Query query = new Query(Criteria.where("pinCode").is(pin).andOperator(Criteria.where("guests.name").is(string)));
		List<Room> rooms = this.mongoTemplate.find(query, Room.class);
		if(rooms.isEmpty())
			return false;
		return true;
	}
}
