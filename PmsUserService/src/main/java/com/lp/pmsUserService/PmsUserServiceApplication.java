package com.lp.pmsUserService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class PmsUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PmsUserServiceApplication.class, args);
	}

}
