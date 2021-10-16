package com.cnpmm.KahootReal.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cnpmm.KahootReal.model.Room;;

@Repository
public interface RoomRepository extends MongoRepository<Room,String>{

	public Room findBycreatorID(String creatorID);
	
}
