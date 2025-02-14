package com.homedelivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HomeDeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeDeliveryApplication.class, args);
	}

}