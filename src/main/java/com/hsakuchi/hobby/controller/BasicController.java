package com.hsakuchi.hobby.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BasicController {
	
	@GetMapping("/home")
	public String home() {
		return "sample";
	}

}
