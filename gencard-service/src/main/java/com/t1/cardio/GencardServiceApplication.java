package com.t1.cardio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GencardServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GencardServiceApplication.class, args);
	}
}
