package com.lp.pmsUserService.user.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "indus")
@Setter
@Getter
@AllArgsConstructor
public class UserModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "EID")
	private Long id;

	@Column(name = "Ename")
	@NotNull
	private String name;

	@Column(name = "Designation")
	@NotNull
	private String desg;

	@Column(name = "Email")
	@Email(regexp =  "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.(?:[A-Z]{2,}|com|org))+$")
	@NotNull(message = "please provide a valid email")
	private String mail;

	@Column(name = "MobNumber")
	@Pattern(regexp = "[0-9]{10}" , message = "provide valid mobile number")
	@NotNull(message = "please provide a  number")
	private String mobileNo;

	@Column(name = "Password")
	@NotNull(message = "please provide a valid password")
	private String password;
	
	public UserModel() {
		super();
	}

}
