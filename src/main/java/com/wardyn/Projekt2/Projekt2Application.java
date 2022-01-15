package com.wardyn.Projekt2;

import com.wardyn.Projekt2.services.interfaces.AppService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:beans.xml")
public class Projekt2Application {

	public static void main(String[] args) {
		SpringApplication.run(Projekt2Application.class, args);
	}

	@Bean
	public CommandLineRunner setUpApp(AppService appService) {
		return (args) -> {
			appService.learning();
		};
	}
}
