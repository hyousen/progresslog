package com.hsakuchi.hobby.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hsakuchi.hobby.TextFileCreate;
import com.hsakuchi.hobby.TextFileWrite;
import com.hsakuchi.hobby.component.ThymeleafText;
import com.hsakuchi.hobby.model.FData;

@Controller
public class TextController {
	@Autowired
	private ThymeleafText thymeleafText;
	private TextFileWrite textFileWrite;
	private TextFileCreate textFileCreate;

	@RequestMapping("/home/create")
	public String home(Model model,FData fdata) {
		model.addAttribute(fdata);
		return "create";
	}
	
	@RequestMapping("/home/diary")
	public String view(Model model,FData fdata) {
		int number = fdata.getDateNumber();
		thymeleafText.process(fdata, number);
		String sentence = fdata.getHtmlText();
		model.addAttribute("sentence", sentence);
		return "log_list";
	}

	@GetMapping("/home/toukou")
	public String toukou(Model model) {
		model.addAttribute("fdata", new FData());
		return "toukou";
	}
	
	@GetMapping("/home")
	public String create(Model model,FData fdata) {
		model.addAttribute("fdata", fdata);
		return "home";
	}
//
//	@PostMapping("/home/kakunin")
//	public String kakunin(Model model, FData fdata) {
//		int number =fdata.getDateNumber();
//		String sentence = " " + number;
//		model.addAttribute("sentence", sentence);
//		return "kakunin";
//	}
//	
	@PostMapping("/home/result")
	public String result(Model model,FData fdata){
		String sentence = fdata.getPostText();
		Date date = new Date();
		String fileName ="log" + fdata.getDateNumber();
		sentence = createSentence(sentence,date,fileName);
		model.addAttribute("sentence", sentence);
		return "result";
	}
	
	private String createSentence(String sentence,Date date, String fileName) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String postSentence = "・" + sentence + "　　　" + sdf.format(date);
		textFileWrite = new TextFileWrite();
		textFileWrite.textWrite(postSentence,fileName);
		return postSentence;
	}
}








