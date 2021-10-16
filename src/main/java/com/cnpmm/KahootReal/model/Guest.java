package com.cnpmm.KahootReal.model;

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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	
	
}
