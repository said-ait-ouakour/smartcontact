package com.ensah.smartcontact;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SmartcontactApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartcontactApplication.class, args);
	}

}
