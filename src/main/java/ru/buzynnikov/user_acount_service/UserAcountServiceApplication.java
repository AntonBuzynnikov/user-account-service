package ru.buzynnikov.user_acount_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UserAcountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserAcountServiceApplication.class, args);
	}

}
