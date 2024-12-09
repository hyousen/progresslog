package com.hsakuchi.hobby.controller;

import java.io.IOException;
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

@RequestMapping
@Controller
public class TextController {
	@Autowired
	private ThymeleafText thymeleafText;
	private TextFileWrite textFileWrite;
	private TextFileCreate textFileCreate;
	private int number;

//	@GetMapping("/home")
//	public String home(Model model) {
//		FData fdata = new FData();
//		model.addAttribute(fdata);
//		return "create_html";
//	}
	@GetMapping("/home/diary")
	public String view(Model model,FData fdata) {
		number = fdata.getDateNumber();
		thymeleafText.process(fdata, number);
		String sentence = fdata.getHtmlText();
		model.addAttribute("sentence", sentence);
		return "log_list";
	}

	@GetMapping("/toukou")
	public String toukou(Model model) {
		model.addAttribute("fdata", new FData());
		return "toukou";
	}
	
	@GetMapping("/home/create")
	public String create(Model model,FData fdata) {
		model.addAttribute("fdata", fdata);
		number = fdata.getDateNumber();
		String message = "問題ありません";
		textFileCreate = new TextFileCreate();
		try {
			textFileCreate.textCreate(number);
			message =textFileCreate.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.addAttribute("text", message);
		return "create_html";
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
		sentence = createSentence(sentence,date);
		model.addAttribute("sentence", sentence);
		return "result";
	}
	
	private String createSentence(String sentence,Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String postSentence = "・" + sentence + "　　　" + sdf.format(date);
		textFileWrite = new TextFileWrite();
		textFileWrite.textWrite(postSentence,number);
		return postSentence;
	}
}








