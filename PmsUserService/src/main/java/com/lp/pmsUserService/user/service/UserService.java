package com.lp.pmsUserService.user.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.lp.pmsUserService.response.Response;
import com.lp.pmsUserService.response.ResponseWithToken;
import com.lp.pmsUserService.user.dto.UserLoginDto;
import com.lp.pmsUserService.user.dto.UserPassword;
import com.lp.pmsUserService.user.dto.UserRegisterDto;
import com.lp.pmsUserService.user.dto.UserUpdateDto;
import com.lp.pmsUserService.user.model.UserModel;


@Service
public interface UserService {

	public ResponseWithToken saveData(UserRegisterDto user);

	public ResponseWithToken loginUser(UserLoginDto log);

	public Response resetPassword(UserPassword password, String token);

	public List<UserModel> findAll();

	public Response delete(long id);

	public Response update(UserUpdateDto dto, long id);
	
//	public List<ProjectModel> findOne(int eid);


}
