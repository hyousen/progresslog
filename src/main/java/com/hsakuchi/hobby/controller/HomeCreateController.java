package com.hsakuchi.hobby.controller;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hsakuchi.hobby.model.DailyLogCreateViewDto;
import com.hsakuchi.hobby.model.FData;
import com.hsakuchi.hobby.service.DiaryWriteService;

@Controller
@SessionAttributes("fdata")
public class HomeCreateController extends BaseHomeController{

	private final  DiaryWriteService diaryWriteService;
	
	public HomeCreateController(DiaryWriteService diaryWriteService) {
		this.diaryWriteService = diaryWriteService;
	}

	/* ===========================
       記事作成確認（GET）
       ========================== */
	@GetMapping("/home/create")
	public String createConfirm(
			Model model,
			@ModelAttribute("fdata") FData fdata)
					throws IOException {

		DailyLogCreateViewDto view = diaryWriteService.getCreateView(fdata.getLogDate());

		model.addAttribute("view", view);

		return "create"; 
		
	}

	/* ===========================
       記事作成実行（POST）
       ========================== */
	@PostMapping("/home/create")
	public String createDo(
			@ModelAttribute("fdata") FData fdata,
			RedirectAttributes attrs,
			Model model) {

		LocalDate date = fdata.getLogDate();

		try {
			diaryWriteService.openOrCreate(date);
			attrs.addFlashAttribute("message", "記事を開きました");
			return "redirect:/home/diary";

		} catch (Exception e) {
			model.addAttribute("message", "作成に失敗: " + e.getMessage());
			return "create";
		}
	}

}
