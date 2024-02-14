package com.project.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.project.application.repositories")
@ComponentScan(basePackages = {"com.project.application.controller","com.project.application.repositories","com.project.application.security","com.project.application.services","com.project.application.dependencies"})
public class Application {

	public static void main(String[] args) {
		System.out.println("Check start");
		SpringApplication.run(Application.class, args)
	}


}
