package com.cnpmm.KahootReal.model;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection="VerifyToken")
@Data
public class VerifyToken {
	@Id
	private ObjectId id = new ObjectId();
	private String token;
	private String userId;
	@Indexed(name="expireAt",expireAfterSeconds = 28800)
	private Date expireAt;
	
	
	public VerifyToken() {
		super();
		// TODO Auto-generated constructor stub
	}


	public VerifyToken(String token, String userId, Date expireAt) {
		super();
		this.token = token;
		this.userId = userId;
		this.expireAt = expireAt;
	}
	
	
}
