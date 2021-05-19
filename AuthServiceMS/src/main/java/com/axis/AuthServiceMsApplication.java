package com.axis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.axis")
public class AuthServiceMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceMsApplication.class, args);
	}
}
