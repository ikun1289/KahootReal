package com.cnpmm.KahootReal.controller;

import com.cnpmm.KahootReal.JWT.JwtAuthenticationFilter;
import com.cnpmm.KahootReal.JWT.JwtTokenProvider;
import com.cnpmm.KahootReal.model.Room;
import com.cnpmm.KahootReal.model.User;
import com.cnpmm.KahootReal.services.RoomServices;
import com.cnpmm.KahootReal.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class UserController {
	
	@Autowired
	RoomServices roomService;
	@Autowired
	UserService userService;
	@Autowired
	PasswordEncoder passwordEncoder;

	@GetMapping("/getrooms")
	public ResponseEntity<?> getClasses(HttpServletRequest request)
	{
		String jwtToken = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String id = new JwtTokenProvider().getUserIdFromJWT(jwtToken);
		List<Room> classrooms = roomService.getRoomByCreatorID(id);
		return new ResponseEntity<List<Room>>(classrooms,HttpStatus.OK);
	}
	
	@GetMapping("/room")
	public ResponseEntity<?> viewRoomDetail(HttpServletRequest request, @RequestParam("id") String roomid)
	{
		String jwtToken = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String id = new JwtTokenProvider().getUserIdFromJWT(jwtToken);
		Optional<Room> classroom = roomService.getRoomByID(roomid);
		if(classroom.isPresent())
			return new ResponseEntity<Room>(classroom.get(),HttpStatus.OK);
		return new ResponseEntity<String>("Room không tòn tại",HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/createRoom")
	public ResponseEntity<?> createRoom(HttpServletRequest request, @RequestBody Room room)
	{
		String jwtToken = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String id = new JwtTokenProvider().getUserIdFromJWT(jwtToken);
		
		room.setCreatorID(id);
		room.setIsOpen(false);
		roomService.addNewRoom(room);
		return new ResponseEntity<String>("Tạo room thành công",HttpStatus.OK);
	}
	
	@PostMapping("/deleteRoom")
	public ResponseEntity<?> deleteRoom(HttpServletRequest request, @RequestParam("id") String roomid)
	{
		String jwtToken = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String id = new JwtTokenProvider().getUserIdFromJWT(jwtToken);
		Optional<Room> classroom = roomService.getRoomByID(roomid);
		if(classroom.isPresent() && classroom.get().getCreatorID().equals(id))
		{
			roomService.deleteRoomWithId(roomid);
			return new ResponseEntity<String>("Xóa room thành công",HttpStatus.OK);
		}
		return new ResponseEntity<String>("Room không tòn tại",HttpStatus.NOT_FOUND);
		
	}
	
	@PostMapping("/editRoom")
	public ResponseEntity<?> editRoom(HttpServletRequest request, @RequestBody Room room)
	{
		String jwtToken = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String id = new JwtTokenProvider().getUserIdFromJWT(jwtToken);
		
		room.setCreatorID(id);
		room.setIsOpen(false);
		roomService.addNewRoom(room);
		return new ResponseEntity<String>("Sửa room thành công",HttpStatus.OK);
	}
	
	@GetMapping("/profile")
	public ResponseEntity<?> getProfile(HttpServletRequest request)
	{
		String jwtToken = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String id = new JwtTokenProvider().getUserIdFromJWT(jwtToken);
		
		Optional<User> u = userService.getUserById(id);
		if(u.isPresent())
		{
			User user = u.get();
			user.setPasswd(null);
			return new ResponseEntity<User>(user,HttpStatus.OK);
		}
		return new ResponseEntity<String>("Tài khoản không tồn tại trong cơ sở dữ liệu",HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/edit-profile")
	public ResponseEntity<?> editProfile(HttpServletRequest request, @RequestBody User userMap)
	{
		String jwt = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String userId = new JwtTokenProvider().getUserIdFromJWT(jwt);
		if(!userMap.getName().isEmpty())
		{
			Optional<User> user = userService.getUserById(userId);
			if(user.isPresent())
			{
				User u = user.get();
				u.setName(userMap.getName());
				u.setPhone(userMap.getPhone());
				u.setGender(userMap.getGender());
				userService.addNewUser(u);
			}
			return ResponseEntity.status(HttpStatus.OK).body("Chỉnh sửa thông tin thành công!");
			
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Trường thông tin nhập không hợp lệ");
	}
	
	@PostMapping("/change-pass")
	public ResponseEntity<?> changePass(HttpServletRequest request, @RequestBody Map<String, String> userMap)
	{
		String oldPass;
		String newPass;
		try{
			oldPass = userMap.get("oldpass");
			newPass = userMap.get("newpass");
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		if(newPass.length()<8)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password cannot be shorter than 8 characters!");
		System.out.println(oldPass+" "+newPass);
		String jwt = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String userId = new JwtTokenProvider().getUserIdFromJWT(jwt);
		Optional<User> user = userService.getUserById(userId);
		if(user.isPresent()){
			User uChangePass = user.get();
			String passwd = uChangePass.getPasswd();
			if(passwordEncoder.matches(oldPass, passwd)){
				uChangePass.setPasswd(newPass);
				userService.addNewUser(uChangePass);
				return ResponseEntity.status(HttpStatus.OK).body("Change pass complete!");
			}
			else{
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password not match!");
			}
				
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user not exist!");
		}
	}
}
