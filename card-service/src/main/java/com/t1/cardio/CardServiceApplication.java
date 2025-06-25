package com.t1.cardio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // Activer la planification des tâches
public class CardServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardServiceApplication.class, args);
	}
}
