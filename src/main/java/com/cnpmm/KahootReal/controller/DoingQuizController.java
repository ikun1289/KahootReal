package com.cnpmm.KahootReal.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.x500.X500Principal;
import javax.servlet.http.HttpServletRequest;

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

import com.cnpmm.KahootReal.model.Answer;
import com.cnpmm.KahootReal.model.CurrentQuiz;
import com.cnpmm.KahootReal.model.Guest;
import com.cnpmm.KahootReal.model.Quiz;
import com.cnpmm.KahootReal.model.Room;
import com.cnpmm.KahootReal.payload.DoQuizMessage;
import com.cnpmm.KahootReal.services.CurrentQuizService;
import com.cnpmm.KahootReal.services.QuizService;
import com.cnpmm.KahootReal.services.RoomServices;

@RestController
public class DoingQuizController {

	@Autowired
	QuizService quizService;
	@Autowired
	RoomServices roomService;
	@Autowired
	CurrentQuizService currentQuizService;
	@Autowired
	private SimpMessagingTemplate template;

	@MessageMapping("/getquiz/{i}")
	@SendTo("/doquiz/room/{i}")
	public void doQuiz(@DestinationVariable String i) {
		Room room = roomService.getRoomByPincode(i);
		String roomId = room.getId().toHexString();
		roomService.startDoQuizInRoomWithId(roomId);
		System.out.println("doQuiz");
		System.out.println(room.toString());
		Map<String, HttpStatus> response = new HashMap<String, HttpStatus>();
		response.put("status", HttpStatus.I_AM_A_TEAPOT);
		
		currentQuizService.setCurrentQuiz(roomId);
		for (Quiz quiz : room.getQuizs()) {
			try {
				System.out.println("wait 3s...");
				this.template.convertAndSend("/doquiz/room/" + i, response);
				Thread.sleep(3000);
				currentQuizService.updateCurrentQuiz(roomId, quiz.getId().toHexString());
				System.out.println(quiz.toString());
				this.template.convertAndSend("/doquiz/room/" + i, quiz);
				Thread.sleep(room.getTime() <= 0 ? 10000 : room.getTime() * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// roomService.closeRoomWithPin(i);
		System.out.println("closedoQuiz");
		this.template.convertAndSend("/doquiz/message/" + i, new DoQuizMessage("Kết thúc!", HttpStatus.I_AM_A_TEAPOT));
		try {
			Thread.sleep(3000);
			roomService.stopDoQuizInRoomWithId(roomId);
			currentQuizService.deleteCurrentQuiz(roomId);
			roomService.closeRoomWithId(room.getId().toHexString());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/openroom")
	public ResponseEntity<String> openRoom(@RequestParam("id") String id) {
		String pin = roomService.openRoomWithId(id);
		return new ResponseEntity<String>(pin, HttpStatus.OK);
	}

	@GetMapping("/getin")
	public ResponseEntity<String> getInRoom(@RequestParam("pin") String pin, @RequestBody Map<String, String> body) {
		String name = body.get("name");
		System.out.println(name);
		int i = 0;
		while (roomService.checkGuestname(pin, name + (i == 0 ? "" : i))) {
			i++;
		}
		name = name + (i == 0 ? "" : i);
		if (roomService.getRoomByPincode(pin).getIsOpen()) {
			try {
				List<Guest> guests = roomService.addNewGuest(pin, name);
				Thread.sleep(1000);
				this.template.convertAndSend("/doquiz/score/" + pin, guests);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return new ResponseEntity<String>(name, HttpStatus.OK);
		}

		return new ResponseEntity<String>("room with pin: " + pin + " is not opened", HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/submitAnswer")
	public ResponseEntity<String> submitAnswer(@RequestParam("pin") String pin, @RequestBody Map<String, String> body) {
		Room room = roomService.getRoomByPincode(pin);
		Guest guest = room.getGuests().stream().filter(x -> x.getName().equals(body.get("name"))).findFirst()
				.orElse(null);
		System.out.println(room);
		System.out.println(guest);
		if (room != null && room.getIsStart() && guest != null) {
			CurrentQuiz currentQuiz = currentQuizService.getCurrentQuizByRoomAndQuiz(room.getId().toHexString(),
					body.get("quizId"));
			if (currentQuiz == null) {
				return new ResponseEntity<String>("Quiz này chưa bắt đầu hoặc đã kết thúc", HttpStatus.BAD_REQUEST);
			}
			Quiz r = room.getQuizs().stream().filter(quiz -> quiz.getId().toHexString().equals(body.get("quizId")))
					.findFirst().orElse(null);
			if (r != null) {
				Answer a = r.getAList().stream()
						.filter(Answer -> body.get("answerId").equals(Answer.getId().toHexString())).findFirst()
						.orElse(null);
				if (a != null && a.getIsCorrect()) {
					int score = guest.getScore();
					System.out.println("score1:"+score);
					Date date = new Date(currentQuiz.getStartTime());
					Date submitDate = new Date();
					long diff = submitDate.getTime() - date.getTime();
					long x = (room.getTime()<=0?8:room.getTime()) * 1000;
					score += (x - diff) / 100;
					System.out.println("start quiz time:"+date);
					System.out.println("Submit time:"+submitDate);
					System.out.println("diff"+diff);
					System.out.println("time:"+x);
					System.out.println("score2:"+score);
					roomService.updateScore(room.getId().toHexString(), body.get("name"), score);
				}
			}
			return new ResponseEntity<String>("Trả lời thành công", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Lỗi đủ thứ lười bắt từng cái", HttpStatus.BAD_REQUEST);
	}

}
