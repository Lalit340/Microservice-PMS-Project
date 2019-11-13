package com.lp.pmsProjectService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class PmsProjectServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PmsProjectServiceApplication.class, args);
	}

}
