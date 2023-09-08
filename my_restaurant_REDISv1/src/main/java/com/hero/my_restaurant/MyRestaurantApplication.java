package com.hero.my_restaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MyRestaurantApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyRestaurantApplication.class, args);
	}

}