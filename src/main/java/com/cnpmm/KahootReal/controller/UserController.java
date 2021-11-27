package com.cnpmm.KahootReal.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cnpmm.KahootReal.JWT.JwtAuthenticationFilter;
import com.cnpmm.KahootReal.JWT.JwtTokenProvider;
import com.cnpmm.KahootReal.model.Room;
import com.cnpmm.KahootReal.services.RoomServices;
import com.jayway.jsonpath.spi.cache.Cache;


@RestController
@RequestMapping("/api")
public class UserController {
	
	@Autowired
	RoomServices roomService;

	@GetMapping("/getrooms")
	public ResponseEntity<?> getClasses(HttpServletRequest request)
	{
		String jwtToken = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String id = new JwtTokenProvider().getUserIdFromJWT(jwtToken);
		List<Room> classrooms = roomService.getRoomByCreatorID(id);
		return new ResponseEntity<List<Room>>(classrooms,HttpStatus.FOUND);
	}
	
	@GetMapping("/room")
	public ResponseEntity<?> viewRoomDetail(HttpServletRequest request, @RequestParam("id") String roomid)
	{
		String jwtToken = new JwtAuthenticationFilter().getJwtFromRequest(request);
		String id = new JwtTokenProvider().getUserIdFromJWT(jwtToken);
		Optional<Room> classroom = roomService.getRoomByID(roomid);
		if(classroom.isPresent())
			return new ResponseEntity<Room>(classroom.get(),HttpStatus.FOUND);
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
		return new ResponseEntity<String>("Tạo room thành công",HttpStatus.CREATED);
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
			return new ResponseEntity<String>("Xóa room thành công",HttpStatus.CREATED);
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
		return new ResponseEntity<String>("Sửa room thành công",HttpStatus.CREATED);
	}
}
