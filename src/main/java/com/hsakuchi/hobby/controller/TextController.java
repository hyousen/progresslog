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

@Controller
public class TextController {
	@Autowired
	private ThymeleafText thymeleafText;
	private TextFileWrite textFileWrite;
	private TextFileCreate textFileCreate;
	private int dateNumber;

	@RequestMapping("/home/create")
	public String create(Model model, FData fdata) {
		dateNumber = fdata.getDateNumber();
		String message = "問題ありません";
		textFileCreate = new TextFileCreate();
		try {
			textFileCreate.textCreate(dateNumber);
			message = textFileCreate.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String fileName = createFileName();
		String sentence = fileName + ".txtを指定します";
		Date date = new Date();
		textFileWrite = new TextFileWrite();
		textFileWrite.titleWrite(date, fileName);
		textFileWrite.textOverWrite(Integer.toString(dateNumber), "dateNumber");
		model.addAttribute("sentence", sentence);
		model.addAttribute("message", message);
		return "create";
	}

	@RequestMapping("/home/diary")
	public String view(Model model, FData fdata) {
		String fileName = createFileName();
		long sleepTime = 2000;
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		String sentence = thymeleafText.process(fileName);
		model.addAttribute("fileName", fileName);
		model.addAttribute("sentence", sentence);
		return "log_list";
	}

	@GetMapping("/home/toukou")
	public String toukou(Model model) {
		model.addAttribute("fdata", new FData());
		return "toukou";
	}

	@RequestMapping("/home")
	public String home(Model model, FData fdata) {
		String dateText =thymeleafText.process("dateNumber");
		dateNumber = Integer.parseInt(dateText);
		fdata.setDateNumber(dateNumber);
		model.addAttribute("fdata", fdata);

		String fileName = createFileName();
		String sentence = thymeleafText.process(fileName);
		model.addAttribute("fileName", fileName);
		model.addAttribute("sentence", sentence);
		return "home";
	}

	//	@RequestMapping("/home/next")
	//	public String next(Model model, FData fdata) {
	//		String postSentence = fdata.getPostText();
	//		int dateNumber = 6;
	//		String fileName = "log" + dateNumber;
	//		Date date = new Date();
	//		postSentence = createSentence(postSentence, date, fileName);
	//		String sentence = thymeleafText.process(fileName);
	//		model.addAttribute("fileName", fileName);
	//		model.addAttribute("fdata", fdata);
	//		model.addAttribute("sentence", sentence);
	//		return "home";
	//	}
	//	@PostMapping("/home/kakunin")
	//	public String kakunin(Model model, FData fdata) {
	//		String sentence = fdata.getPostText();
	//		model.addAttribute("sentence", sentence);
	//		return "kakunin";
	//	}
	
	/*
	 * 受けとった文章をファイルに書き込み
	 */

	@PostMapping("/home/result")
	public String result(Model model, FData fdata) {
		String postSentence = fdata.getPostText();
		Date date = new Date();
		String fileName = "log" + dateNumber;
		postSentence = createSentence(postSentence, date, fileName);
		model.addAttribute("postSentence", postSentence);
		return "result";
	}

	private String createSentence(String sentence, Date date, String fileName) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd日(E)HH:mm:ss");
		String postSentence = "・" + sentence + "　　　" + sdf.format(date);
		textFileWrite = new TextFileWrite();
		textFileWrite.textWrite(postSentence, fileName);
		return postSentence;
	}
	
	private String createFileName() {
		if(dateNumber == 0) {
			return "todo";
		}else {
			return "log" + dateNumber;
		}
	}
	
}
