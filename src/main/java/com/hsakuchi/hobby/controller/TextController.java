package com.hsakuchi.hobby.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hsakuchi.hobby.component.ThymeleafText;
import com.hsakuchi.hobby.model.FData;

@Controller
public class TextController {
	@Autowired
	private ThymeleafText thymeleafText;
	
	
	@GetMapping("/home")
	public String view(Model model,FData fdata) {
		thymeleafText.process(fdata);
		String sentence = fdata.getHtmlText();
		model.addAttribute("sentence", sentence);
		return "log_list";
	}
	
//	@PostMapping("/test")
//	public String test(@ModelAttribute FData fdata,Model model) {
//		thymeleafText.process(fdata);
//		model.addAttribute("fdata", fdata);
//		return "test";
//	}
	
}