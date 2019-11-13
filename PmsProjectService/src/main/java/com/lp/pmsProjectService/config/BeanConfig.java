package com.lp.pmsProjectService.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfig {


	/* 
	 * purpose to create a bean for password Encoding
	 * @return the BCryptPasswordEncoder() object .
	 * */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/* 
	 * purpose to create a bean for mapping object values
	 * @return the ModelMapper() object .
	 * */
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
