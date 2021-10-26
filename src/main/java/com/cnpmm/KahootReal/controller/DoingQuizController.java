package com.cnpmm.KahootReal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cnpmm.KahootReal.model.Quiz;
import com.cnpmm.KahootReal.services.QuizService;

@RestController
public class DoingQuizController {

	@Autowired
	QuizService quizService;
	@Autowired
    private SimpMessagingTemplate template;
	
	@MessageMapping("/getquiz/{i}")
	@SendTo("/doquiz/room/{i}")
	public void doQuiz(@DestinationVariable String i) {
		List<Quiz> quizs = quizService.getQuizsByRoomID(i);
		System.out.println("doQuiz");
		
		if(quizs!=null) {
			for (Quiz quiz : quizService.getQuizsByRoomID(i)) {
				try {
					Thread.sleep(8000);
					this.template.convertAndSend("/doquiz/room/"+i,quiz);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else {
			this.template.convertAndSend("/doquiz/room/"+i,"NULL");
		}
		
	}
	
}
