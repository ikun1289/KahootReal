package com.cnpmm.KahootReal.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cnpmm.KahootReal.RandomString;
import com.cnpmm.KahootReal.model.Answer;
import com.cnpmm.KahootReal.model.Quiz;
import com.cnpmm.KahootReal.model.Room;
import com.cnpmm.KahootReal.model.User;
import com.cnpmm.KahootReal.services.QuizService;
import com.cnpmm.KahootReal.services.RoomServices;
import com.cnpmm.KahootReal.services.UserService;

@RestController
public class TestController {
	
	@Autowired
	private RoomServices roomServices;
	@Autowired
	private UserService UserService;
	@Autowired
	private QuizService QuizService;
	

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
	
	@GetMapping(value="/api/getroombycreator")
	public ResponseEntity<List<Room>> getRoomByCreatorID(@RequestParam("id") String creatorID)
	{
		List<Room> room = roomServices.getRoomByCreatorID(creatorID);
		if ( room != null) {
			System.out.println("Room: /n"+room.toString());
			return new ResponseEntity<List<Room>>(room,HttpStatus.OK);
		}
		else {
			System.out.println("Cant found any room with creator ID: "+creatorID);
			return new ResponseEntity<List<Room>>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping(value="/api/getroombyname")
	public ResponseEntity<Room> getRoomByName(@RequestParam("name") String name)
	{
		Room room = roomServices.getRoomByName(name);
		if ( room != null) {
			System.out.println("Room: /n"+room.toString());
			return new ResponseEntity<Room>(room,HttpStatus.OK);
		}
		else {
			System.out.println("Cant found any room with name: "+name);
			return new ResponseEntity<Room>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping(value="/api/add-random-room")
	public String addNewRandomRoom()
	{
		Room room = new Room("room test" +new Random().nextInt(100), "000000", false, "616aa5319352ae0a58224c15",10, null, null);
		roomServices.addNewRoom(room);
		return "Added new room with ID: "+room.getId();
	}
	
	@PostMapping(value="/api/add-random-room-with-quiz")
	public String addNewRandomRoomWithQuiz()
	{
		Room room = new Room("room test" +new Random().nextInt(100), new RandomString(9).nextString(), false,"616aa5319352ae0a58224c15",10, null, null);
		List<Quiz> quizs= new ArrayList<>();
		Quiz quiz = new Quiz("is shrimp gud?",null);
		List<Answer> answers = new ArrayList<Answer>();
		answers.add(new Answer("Yes",true));
		answers.add(new Answer("No",false));
		quiz.setAList(answers);
		quizs.add(quiz);
		quizs.add(quiz);
		room.setQuizs(quizs);
		
		roomServices.addNewRoom(room);
		return "Added new room with ID: "+room.getId();
	}
	
	@PostMapping(value="/addRandomUser")
	public String addRandomUser()
	{
		User user = new User(new ObjectId(),"quoc","1234","","","",true);
		
		UserService.addNewUser(user);
		return "Added new user with ID: "+user.getId();
	}
	
	@GetMapping(value="/api/get-room-by-quiz-id")
	public ResponseEntity<Room> getRoomByQuizID(@RequestParam("id") String quizId)
	{
		Room room = roomServices.getRoomByQuizID(quizId);
		if(room!=null)
			return new ResponseEntity<Room>(room,HttpStatus.OK);
		return new ResponseEntity<Room>(HttpStatus.NOT_FOUND);
	}
	
	@GetMapping(value="/api/get-room-by-answer-id")
	public ResponseEntity<Room> getRoomByAnswerID(@RequestParam("id") String answer)
	{
		Room room = roomServices.getRoomByAnswerID(answer);
		if(room!=null)
			return new ResponseEntity<Room>(room,HttpStatus.OK);
		return new ResponseEntity<Room>(HttpStatus.NOT_FOUND);
	}
	
	@PostMapping(value="/api/addNewQuizTest")
	public ResponseEntity<Quiz> addNewQuiz(@RequestParam("roomId") String roomId)
	{
		List<Quiz> quizs= new ArrayList<>();
		Quiz quiz = new Quiz("is shrimp gud? "+new Random().nextInt(100),null);
		List<Answer> answers = new ArrayList<Answer>();
		answers.add(new Answer("Yes",true));
		answers.add(new Answer("No",false));
		quiz.setAList(answers);
		quizs.add(quiz);
		quizs.add(quiz);
		QuizService.addNewQuizsToRoom(roomId, quizs);
		
		return new ResponseEntity<Quiz>(HttpStatus.CREATED);
	}
}
