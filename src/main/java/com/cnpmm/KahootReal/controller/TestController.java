package com.cnpmm.KahootReal.controller;

import java.util.ArrayList;import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Random;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cnpmm.KahootReal.model.Answer;
import com.cnpmm.KahootReal.model.Quiz;
import com.cnpmm.KahootReal.model.Room;
import com.cnpmm.KahootReal.repositories.RoomRepository;
import com.cnpmm.KahootReal.services.RoomServices;

@RestController
public class TestController {
	
	@Autowired
	private RoomServices roomServices;

	@GetMapping(value="/api/getroom")
	public ResponseEntity<Room> getRoomByID(@RequestParam("id") String roomId)
	{
		Optional<Room> room = roomServices.getRoomByID(roomId);
		if ( room.isPresent()) {
			System.out.println("Room: /n"+room.get());
			return new ResponseEntity<Room>(room.get(),HttpStatus.OK);
		}
		else {
			System.out.println("Cant found any room with ID: "+roomId);
			return new ResponseEntity<Room>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping(value="/api/add-random-room")
	public String addNewRandomRoom()
	{
		Room room = new Room("room test" +new Random().nextInt(100), "000000", false, new ObjectId("616aa5319352ae0a58224c15"), null, null);
		roomServices.addNewRoom(room);
		return "Added new room with ID: "+room.getId();
	}
	
	@PostMapping(value="/api/add-random-room-with-quiz")
	public String addNewRandomRoomWithQuiz()
	{
		Room room = new Room("room test" +new Random().nextInt(100), "000000", false, new ObjectId("616aa5319352ae0a58224c15"), null, null);
		List<Quiz> quizs= new ArrayList<>();
		Quiz quiz = new Quiz("is shrimp gud?",null);
		List<Answer> answers = new ArrayList<Answer>();
		answers.add(new Answer("Yes",true));
		answers.add(new Answer("No",false));
		quiz.setaList(answers);
		quizs.add(quiz);
		quizs.add(quiz);
		room.setQuizs(quizs);
		
		roomServices.addNewRoom(room);
		return "Added new room with ID: "+room.getId();
	}
}
