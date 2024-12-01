package com.hsakuchi.hobby;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.hsakuchi.hobby.component.ThymeleafText;

public class SpringThymeleaf3TextApplication implements CommandLineRunner{
	
	
	@Autowired
	private ThymeleafText thymeleafText;
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(SpringThymeleaf3TextApplication.class).web(false).run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		thymeleafText.process();	
	}

}
