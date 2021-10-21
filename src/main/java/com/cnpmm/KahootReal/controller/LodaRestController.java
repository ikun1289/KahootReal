package com.cnpmm.KahootReal.controller;

import com.cnpmm.KahootReal.model.User;
import com.cnpmm.KahootReal.payload.*;
import com.cnpmm.KahootReal.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cnpmm.KahootReal.JWT.JwtTokenProvider;
import com.cnpmm.KahootReal.security.CustomUserDetails;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class LodaRestController {

	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	private UserService UserService;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@PostMapping("/api/login")
	public LoginResponse authenticateUser(@RequestBody LoginRequest loginRequest) {

		// Xác thực từ username và password.
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		// Nếu không xảy ra exception tức là thông tin hợp lệ
		// Set thông tin authentication vào Security Context
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Trả về jwt cho người dùng.
		String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
		System.out.println(loginRequest.getUsername() + " " + loginRequest.getPassword());
		return new LoginResponse(jwt);
	}

	// Api /api/random yêu cầu phải xác thực mới có thể request
	@GetMapping("/random")
	public RandomStuff randomStuff() {
		return new RandomStuff("JWT Hợp lệ mới có thể thấy được message này");
	}



	@PostMapping("/api/register")
	public ResponseEntity<GeneralResponse> registerUser(@RequestBody RegisterRequest registerRequrest) {
		if(UserService.checkUsername(registerRequrest.getUsername())) {
			ResponseEntity.status(HttpStatus.CONFLICT);
			return new ResponseEntity<>( new GeneralResponse("Đã tồn tại username"), HttpStatus.CONFLICT);
		}

		User user = new User(registerRequrest.getUsername(), registerRequrest.getPassword(),registerRequrest.getEmail());

		UserService.addNewUser(user);
		GeneralResponse res = new GeneralResponse("Đăng ký thành công");
		ResponseEntity.status(HttpStatus.OK);
		return new ResponseEntity<>( new GeneralResponse("Đăng ký thành công"), HttpStatus.OK);
	}

}
