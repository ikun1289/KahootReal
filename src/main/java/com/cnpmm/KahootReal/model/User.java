package com.cnpmm.KahootReal.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class User {

	@Id
	private ObjectId id = new ObjectId();
	private String name;
	private String email;
	private String phone;
	private Boolean gender;//true == male
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public User(String name, String email, String phone, Boolean gender) {
		super();
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.gender = gender;
	}
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Boolean getGender() {
		return gender;
	}
	public void setGender(Boolean gender) {
		this.gender = gender;
	}
	
	
	
}
