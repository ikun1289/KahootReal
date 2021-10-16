package com.cnpmm.KahootReal.services;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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
	
	public Room getRoomByCreatorID(String creatorID)
	{
		return roomRepository.findBycreatorID(creatorID);
	}
	
	public void addNewRoom(Room room)
	{
		roomRepository.save(room);
	}
}
