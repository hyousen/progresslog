package com.hsakuchi.hobby.controller;

import org.springframework.stereotype.Controller;

@Controller
public class BasicController {
	
	@GetMpping("/home")
	public String home() {
		return "log_list";
	}

}
