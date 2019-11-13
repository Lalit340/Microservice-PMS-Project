package com.lp.pmsUserService.user.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserUpdateDto {

	@NotNull
	private String name;

	@NotNull
	private String desg;

	@NotNull(message = "please provide a valid number")
	@Pattern(regexp = "[0-9]{10}", message = "provide valid mobile number")
	private String mobileNo;

}
