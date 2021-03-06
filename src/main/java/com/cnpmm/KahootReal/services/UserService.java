package com.cnpmm.KahootReal.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cnpmm.KahootReal.model.User;
import com.cnpmm.KahootReal.model.VerifyToken;
import com.cnpmm.KahootReal.repositories.UserRepository;
import com.cnpmm.KahootReal.security.CustomUserDetails;

import lombok.extern.java.Log;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	VerifyTokenService tokenService;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	EmailService emailService;
	public List<User> getAllUser() {
		return userRepository.findAll();
	}

	public Optional<User> getUserById(String id) {
		return userRepository.findById(id);
	}

	public void addNewUser(User user) {
		user.setPasswd(passwordEncoder.encode(user.getPasswd()));
		userRepository.save(user);
	}

	public User findUsername(String username) {
		return userRepository.findByUserName(username);
	}

	public Boolean checkUsername(String username) {
		return  (userRepository.findByUserName(username) != null) ? true : false;
	}

	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = userRepository.findByUserName(name);
		if (user == null) {
			System.out.println("User not found!");
			throw new UsernameNotFoundException("User not found!");
		} else {
			System.out.println("User found: " + user.getUserName() +" "+user.getPasswd());
		}
		return new CustomUserDetails(user);

	}

	public UserDetails loadUserByID(String userId) {
		// TODO Auto-generated method stub
		Optional<User> user = userRepository.findById(userId);
		if (!user.isPresent()) {
			System.out.println("User not found!");
			throw new UsernameNotFoundException("User not found!");
		} else {
			System.out.println("User found: " + user.get().getUserName() +" "+user.get().getPasswd());
		}
		return new CustomUserDetails(user.get());
	}
	
	public User register(User user) // String siteURL
	{
		user.setPasswd(passwordEncoder.encode(user.getPasswd()));
		user.setEnable(false);
		userRepository.save(user);
		return user;
	}

	public VerifyToken createToken(User user) {
		VerifyToken token = tokenService.createNewToken(user.getId().toHexString());
		tokenService.saveNewVerifyToken(token);
		return token;
	}

	public User enableUser(String id) {
		Query query = new Query(Criteria.where("id").is(id));
		Update update = new Update().set("enable", true);
		return this.mongoTemplate.findAndModify(query, update, User.class);
	}

	public User findByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.findByEmail(email);
	}
}

