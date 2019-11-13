package com.lp.pmsUserService.user.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lp.pmsUserService.exceptions.InternalException;
import com.lp.pmsUserService.exceptions.LoginException;
import com.lp.pmsUserService.exceptions.PasswordException;
import com.lp.pmsUserService.exceptions.RegisterException;
import com.lp.pmsUserService.exceptions.TokenGenerateException;
import com.lp.pmsUserService.response.Response;
import com.lp.pmsUserService.response.ResponseWithToken;
import com.lp.pmsUserService.user.dto.UserLoginDto;
import com.lp.pmsUserService.user.dto.UserPassword;
import com.lp.pmsUserService.user.dto.UserRegisterDto;
import com.lp.pmsUserService.user.dto.UserUpdateDto;
import com.lp.pmsUserService.user.model.UserModel;
import com.lp.pmsUserService.user.repository.UserRepository;
import com.lp.pmsUserService.utility.ResponseHelper;
import com.lp.pmsUserService.utility.UserTokenGenerate;


@Service("pmsUserService")
public class UserServiceImplementation implements UserService {

	@Autowired
	private UserRepository repository;

	@Autowired
    private UserTokenGenerate userTokenGenerate;
	
	@Autowired
	private PasswordEncoder password;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserTokenGenerate tokenGenerate;

	public ResponseWithToken saveData(UserRegisterDto user) {
		System.out.println("object :"+user.getMail() +" "+user.getMobileNo()+"  "+user.getDesg()+"  "+user.getName()+"  "+user.getPassword());
		ResponseWithToken response = null;
		user.setPassword(password.encode(user.getPassword()));
		Optional<UserModel> validate = repository.findByMail(user.getMail());
		if (validate.isPresent()) {
			throw new RegisterException("already this user is present", 502);
		}

		UserModel model = modelMapper.map(user, UserModel.class);
		UserModel dataResponse = repository.save(model);
		if (dataResponse == null) 
			throw new RegisterException("data is not saved in database", 501);
		
		String token = userTokenGenerate.generateToken(dataResponse.getId());
		String userName = dataResponse.getName();
        System.out.println("this is the token :: "+token );	
		response = ResponseHelper.responseTokenSender("Registration Successful", HttpStatus.OK.value() ,token , userName);

		return response;
	}// saveData

	@Override
	public ResponseWithToken loginUser(UserLoginDto log) {
        System.out.println("object" + log);
		Optional<UserModel> validate =  repository.findByMail(log.getMail());
		ResponseWithToken response = null;
		

		if (validate.isPresent()) {
			if (password.matches(log.getPassword(), validate.get().getPassword())) {
				String generateToken = tokenGenerate.generateToken(validate.get().getId());
				if (validate.get().getDesg().equals("Developer")) {
					response = ResponseHelper.responseTokenSender("User Login Successful ", 201, generateToken,
							validate.get().getName());
					return response;
				} else if (validate.get().getDesg().equals("Team Leader")) {
					response = ResponseHelper.responseTokenSender("User Login Successful ", 202, generateToken,
							validate.get().getName());
					return response;
				} else if (validate.get().getDesg().equals("Project Manager")) {
					response = ResponseHelper.responseTokenSender("User Login Successful ", 203, generateToken,
							validate.get().getName());
					return response;
				} else if (validate.get().getDesg().equals("Designer")) {
					response = ResponseHelper.responseTokenSender("User Login Successful ", 204, generateToken,
							validate.get().getName());
					return response;
				} else {
					throw new LoginException("designation not matched", 400);
				}

			} else
				throw new LoginException("Password is not valid", 401);
		} else {
			throw new LoginException("Email is not exist", 402);
		}

	}// loginUser

	@Override
	public Response resetPassword(UserPassword password, String token) {
		Optional<UserModel> validate = null;
		Response response = null;

		try {
			long id = tokenGenerate.varifyToken(token);
			validate = repository.findById(id);
		} catch (UnsupportedEncodingException e) {
			throw new TokenGenerateException("Invalid Token", 420);
		}

		if (password.getPassword().equals(password.getConformPassword())) {
			validate.get().setPassword(this.password.encode(password.getPassword()));
			repository.save(validate.get());
			response = ResponseHelper.responseSender(" password changes Successfully ", 205);
			return response;
		} else {
			throw new PasswordException("two password are not matched", 410);
		}
	}// resetPassword

	@Override
	public List<UserModel> findAll() {
		List<UserModel> model = null;
		model = repository.findAll();
		if (model != null) {
			return model;
		} else
			throw new RegisterException("Data not found", 412);

	}// findAll

	@Override
	public Response delete(long id) {
		repository.deleteById(id);
		Response response = ResponseHelper.responseSender(" Deleted Successfully ", 200);
		return response;
	}

	@Override
	public Response update(UserUpdateDto user, long id) {
		Optional<UserModel> model = repository.findById(id);
		if (!model.isPresent()) {
			throw new InternalException("Data not Updated", 401);
		}

		model.get().setDesg(user.getDesg());
		model.get().setMobileNo(user.getMobileNo());
		model.get().setName(user.getName());

		UserModel responseData = repository.save(model.get());
		if (responseData == null) {
			throw new InternalException("Data not Updated because some internal problem", 402);
		}
		Response response = ResponseHelper.responseSender(" Update Successfully ", 200);
		return response;
	}

//	@Override
//	public List<ProjectModel> findOne(int eid) {
//		Optional<UserModel> model = repository.findById(eid);
//		List<ProjectModel> projModel= model.get().getProjectModel();
//		if(projModel == null) {
//			throw new RegisterException("no project found", 401);
//		}
//		return projModel;
//	}

}// class
