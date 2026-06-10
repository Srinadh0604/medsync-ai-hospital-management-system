package com.srinadh.medsync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MedsyncApplication {

	public static void main(String[] args) {SpringApplication.run(MedsyncApplication.class, args);
	}


}
