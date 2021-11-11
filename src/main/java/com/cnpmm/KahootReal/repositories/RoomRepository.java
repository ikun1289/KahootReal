package com.cnpmm.KahootReal.repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cnpmm.KahootReal.model.Room;;

@Repository
public interface RoomRepository extends MongoRepository<Room,String>{

	public List<Room> findByCreatorID(String creatorID);
	
	public Room findByName(String name);
	
	public Room findByQuizs_Id(String id);
	
	public Room findByQuizs_aList_Id(String id);
	
	public Room findByPinCode(String pin);
}
