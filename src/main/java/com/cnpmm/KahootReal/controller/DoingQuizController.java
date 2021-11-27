package com.cnpmm.KahootReal.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cnpmm.KahootReal.model.Guest;
import com.cnpmm.KahootReal.model.Quiz;
import com.cnpmm.KahootReal.model.Room;
import com.cnpmm.KahootReal.payload.DoQuizMessage;
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
		System.out.println(room.toString());
		for (Quiz quiz : room.getQuizs()) {
			try {
				System.out.println(quiz.toString());
				this.template.convertAndSend("/doquiz/room/"+i,quiz);
				Thread.sleep(room.getTime()<=0?8000:room.getTime()*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		roomService.closeRoomWithPin(i);
		System.out.println("closedoQuiz");
		this.template.convertAndSend("/doquiz/message/"+i,new DoQuizMessage("Kết thúc!",HttpStatus.I_AM_A_TEAPOT));
		
	}
	
	@PostMapping("/openroom")
	public ResponseEntity<String> openRoom(@RequestParam("id") String id)
	{
		
		String pin = roomService.openRoomWithId(id);
		return new ResponseEntity<String>(pin,HttpStatus.OK);
	}
	
	@GetMapping("/getin")
	public ResponseEntity<String> getInRoom(@RequestParam("pin") String pin, @RequestBody Map<String, String> body)
	{
		String name = body.get("name");
		System.out.println(name);
		int i = 0;
		while(roomService.checkGuestname(pin,name + (i==0?"":i)))
		{
			i++;
		}
		name = name +(i==0?"":i);
		if(roomService.getRoomByPincode(pin).getIsOpen())
		{
			try {
				List<Guest> guests = roomService.addNewGuest(pin,name);
				Thread.sleep(1000);
				this.template.convertAndSend("/doquiz/score/"+pin,guests);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return new ResponseEntity<String>(name,HttpStatus.OK);
		}
		
		return new ResponseEntity<String>("room with pin: "+pin+" is not opened",HttpStatus.BAD_REQUEST);
	}
	
}
