package com.hsakuchi.hobby.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

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
	private String postSentence;

	@GetMapping("/home")
	public String view(Model model, FData fdata) {
		thymeleafText.process(fdata);
		String sentence = fdata.getHtmlText();
		model.addAttribute("sentence", sentence);
		return "log_list";
	}

	@GetMapping("/toukou")
	public String toukou(Model model) {
		model.addAttribute("fdata", new FData());
		return "toukou";
	}

//	@PostMapping("/kakunin")
//	public String kakunin(Model model, FData fdata) {
//		String sentence = fdata.getPostText();
//		Date date = new Date();
//		sentence = convert(sentence,date);
//		model.addAttribute("sentence", sentence);
//		return "kakunin";
//	}
	
	@PostMapping("/result")
	public String result(Model model,FData fdata){
		String sentence = fdata.getPostText();
		Date date = new Date();
		sentence = convert(sentence,date);
		model.addAttribute("sentence", sentence);
		return "result";
	}
	
	private String convert(String sentence,Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		postSentence = "・" + sentence + "　　　" + sdf.format(date);
		return postSentence;
	}
}