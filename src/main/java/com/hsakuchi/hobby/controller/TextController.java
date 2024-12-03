package com.hsakuchi.hobby.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hsakuchi.hobby.component.ThymeleafText;
import com.hsakuchi.hobby.model.FData;

@RequestMapping
@Controller
public class TextController {
	@Autowired
	private ThymeleafText thymeleafText;

	@GetMapping("/home")
	public String view(Model model, FData fdata) {
		thymeleafText.process(fdata);
		String sentence = fdata.getHtmlText();
		model.addAttribute("sentence", sentence);
		return "log_list";
	}

	@GetMapping("/diary")
	public String toukou( Model model) {
		model.addAttribute("fdata", new FData());
		return "diary";
	}
	
	@PostMapping("/result")
	public String result(Model model, FData fdata) {
		String sentence = fdata.getSampleText();
		model.addAttribute("sentence", sentence);
		return "result";
	}
}