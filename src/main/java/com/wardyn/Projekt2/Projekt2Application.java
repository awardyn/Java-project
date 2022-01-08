package com.wardyn.Projekt2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;


@SpringBootApplication
@ImportResource("classpath:beans.xml")
public class Projekt2Application {

	public static void main(String[] args) {
		SpringApplication.run(Projekt2Application.class, args);
	}

}
