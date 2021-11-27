package com.cnpmm.KahootReal.controller;

import com.cnpmm.KahootReal.model.User;
import com.cnpmm.KahootReal.model.VerifyToken;
import com.cnpmm.KahootReal.payload.*;
import com.cnpmm.KahootReal.services.EmailService;
import com.cnpmm.KahootReal.services.UserService;
import com.cnpmm.KahootReal.services.VerifyTokenService;

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
import org.springframework.web.bind.annotation.RequestParam;
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
	private VerifyTokenService tokenService;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private EmailService EmailService;

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
	public ResponseEntity<GeneralResponse> registerUser(@RequestBody User registerRequrest) {
		System.out.println(registerRequrest.toString());
		if(registerRequrest.getUserName()== "" || registerRequrest.getEmail()=="" || registerRequrest.getPasswd()=="")
		{
			return new ResponseEntity<>(new GeneralResponse("Những trường quan trọng không được để trống"), HttpStatus.BAD_REQUEST);
		}
		User user1 = UserService.findByEmail(registerRequrest.getEmail());
		if (user1 != null) {
			if (user1.getUserName().equals(registerRequrest.getUserName())) {
				if (user1.getEnable()) {
					ResponseEntity.status(HttpStatus.CONFLICT);
					System.out.println("Tài khoản này đã được tạo rồi");
					return new ResponseEntity<>(new GeneralResponse("Đã tồn tại tài khoản này"), HttpStatus.CONFLICT);
				} else {
					ResponseEntity.status(HttpStatus.ALREADY_REPORTED);
					System.out.println("Tài khoản này đã được tạo rồi mà chưa kích hoạt");
					return new ResponseEntity<>(
							new GeneralResponse("Tài khoản này đã tạo mà chưa kích hoạt, bạn có muốn tạo lại?"),
							HttpStatus.ALREADY_REPORTED);
				}
			} else {
				ResponseEntity.status(HttpStatus.CONFLICT);
				System.out.println("email đã tồn tại");
				return new ResponseEntity<>(new GeneralResponse("Email đã được sử dụng"), HttpStatus.CONFLICT);
			}
		}

		if (UserService.checkUsername(registerRequrest.getUserName())) {
			ResponseEntity.status(HttpStatus.CONFLICT);
			System.out.println("user đã tồn tại");
			return new ResponseEntity<>(new GeneralResponse("Đã tồn tại username"), HttpStatus.CONFLICT);
		}

		User user = UserService.register(registerRequrest);
		VerifyToken token = UserService.createToken(user);

		ResponseEntity.status(HttpStatus.OK);
		System.out.println("đk thành công");
		Thread thread = new Thread() {
			public void run() {
				try {
					EmailService.sendMail(user, token);
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e.getMessage());
				}

			}
		};
		thread.start();
		return new ResponseEntity<>(new GeneralResponse("Đăng ký thành công! \nHãy kiểm tra mail của bạn"),
				HttpStatus.OK);

	}

	@GetMapping("/verify")
	public String verifyAccount(@RequestParam("token") String token) {

		VerifyToken vToken = tokenService.getVTokenByToken(token);
		System.out.println("verifying");
		if (vToken != null) {
			UserService.enableUser(vToken.getUserId());
			tokenService.removeToken(token);
			return "Account Verified!";
		} else {
			return "Verify failed";
		}
	}

}
