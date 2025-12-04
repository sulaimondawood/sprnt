package com.dawood.sprnt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class SprntApplication {

	public static void main(String[] args) {
		SpringApplication.run(SprntApplication.class, args);
	}

}
