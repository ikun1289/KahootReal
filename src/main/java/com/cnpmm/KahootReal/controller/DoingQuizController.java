package com.cnpmm.KahootReal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cnpmm.KahootReal.model.Quiz;
import com.cnpmm.KahootReal.model.Room;
import com.cnpmm.KahootReal.services.QuizService;
import com.cnpmm.KahootReal.services.RoomServices;

@RestController
public class DoingQuizController {

	@Autowired
	QuizService quizService;
	@Autowired
	RoomServices roomService;
	@Autowired
    private SimpMessagingTemplate template;
	
	@MessageMapping("/getquiz/{i}")
	@SendTo("/doquiz/room/{i}")
	public void doQuiz(@DestinationVariable String i) {
		Room room = roomService.getRoomByPincode(i);
		System.out.println("doQuiz");
		System.out.println(room.getQuizs().toString());
		if(room!=null) {
			for (Quiz quiz : room.getQuizs()) {
				try {
					this.template.convertAndSend("/doquiz/room/"+i,quiz);
					Thread.sleep(room.getTime()<=0?8000:room.getTime()*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else {
			this.template.convertAndSend("/doquiz/room/"+i,"is not open or not exist");
		}
		roomService.closeRoomWithPin(i);
		System.out.println("closedoQuiz");
		
	}
	
	@PostMapping("/openroom")
	public ResponseEntity<String> openRoom(@RequestParam("pin") String pin)
	{
		
		roomService.openRoomWithPin(pin);
		return new ResponseEntity<String>("Opened room with pin: "+pin,HttpStatus.OK);
	}
	
	@GetMapping("/getin")
	public ResponseEntity<String> getInRoom(@RequestParam("pin") String pin)
	{
		if(roomService.getRoomByPincode(pin).getIsOpen())
		{
			return new ResponseEntity<String>("Get in room with pin: "+pin,HttpStatus.OK);
		}
		
		return new ResponseEntity<String>("room with pin: "+pin+" is not opened",HttpStatus.BAD_REQUEST);
	}
	
}
