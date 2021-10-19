package com.cnpmm.KahootReal.model;

import lombok.Data;

@Data
public class Guest {

	private String name;
	private int score;
	public Guest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Guest(String name, int score) {
		super();
		this.name = name;
		this.score = score;
	}
	
	
	
}
