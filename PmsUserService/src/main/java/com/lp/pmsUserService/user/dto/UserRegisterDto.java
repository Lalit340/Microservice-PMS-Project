package com.lp.pmsUserService.user.dto;




import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class UserRegisterDto {

	
	private String  name;
	
	private String desg;
	
	@Email(regexp = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.(?:[A-Z]{2,}|com|org))+$")
	private String mail;
	
	@Pattern(regexp = "[0-9]{10}" , message = "provide valid mobile number")
	private String mobileNo;

	@Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})", message = "provide valid password ")
	private String password;

	public UserRegisterDto() {
		super();
		// TODO Auto-generated constructor stub
	}


	
}
