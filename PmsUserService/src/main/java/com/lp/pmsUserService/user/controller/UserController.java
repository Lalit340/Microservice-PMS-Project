package com.lp.pmsUserService.user.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lp.pmsUserService.response.Response;
import com.lp.pmsUserService.response.ResponseWithToken;
import com.lp.pmsUserService.user.dto.UserLoginDto;
import com.lp.pmsUserService.user.dto.UserPassword;
import com.lp.pmsUserService.user.dto.UserRegisterDto;
import com.lp.pmsUserService.user.dto.UserUpdateDto;
import com.lp.pmsUserService.user.model.UserModel;
import com.lp.pmsUserService.user.service.UserService;



@RestController
@RequestMapping("/user")
@CrossOrigin(allowedHeaders = "*",origins = "*")
public class UserController {

	@Autowired
	private UserService service;
	
	

	@PostMapping("/register")
	public ResponseEntity<ResponseWithToken> register(@Valid @RequestBody UserRegisterDto usr) {
		ResponseWithToken response = service.saveData(usr);
		return new ResponseEntity<ResponseWithToken>(response, HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<ResponseWithToken> login(@Valid @RequestBody UserLoginDto log) {
		ResponseWithToken response = service.loginUser(log);
		return new ResponseEntity<ResponseWithToken>(response, HttpStatus.OK);
	}

	@PutMapping("/resetpassword")
	public ResponseEntity<Response> reset(@Valid @RequestBody UserPassword password, @RequestHeader String token) {
		System.out.println("user token :"+token);
		Response response = service.resetPassword(password, token);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

//	@GetMapping("/oneUser")
//	public List<ProjectModel> getById(@RequestParam int eid){
//		List<ProjectModel> model = service.findOne(eid)	;
//		return model;
//	}
	@GetMapping("/usersinfo")
	public List<UserModel> find() {
		List<UserModel> model = service.findAll();
		return model;
	}

	@DeleteMapping("/deleteUser/{id}")
	public ResponseEntity<Response> deleteProject(@PathVariable int id) {
		Response response = service.delete(id);
		return new ResponseEntity<Response>(response, HttpStatus.OK);

	}

	@PutMapping("/updateUser/{id}")
	public ResponseEntity<Response> updateProject(@Valid @RequestBody UserUpdateDto dto, @PathVariable int id) {
		Response response = service.update(dto, id);
		return new ResponseEntity<Response>(response, HttpStatus.OK);

	}

}
