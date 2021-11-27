package com.cnpmm.KahootReal.payload;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class DoQuizMessage {
	
	private String message;
	private HttpStatus status; //httpstatus
	
	public DoQuizMessage(String string, HttpStatus iAmATeapot) {
		this.message = string;
		this.status = iAmATeapot;
	}
}
