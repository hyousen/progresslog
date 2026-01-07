package com.hsakuchi.hobby.controller;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.hsakuchi.hobby.model.DailyLogViewDto;
import com.hsakuchi.hobby.model.FData;
import com.hsakuchi.hobby.service.DiaryReadService;

@Controller
@RequestMapping("/home")
@SessionAttributes("fdata")
public class HomeController extends BaseHomeController{
	@Autowired
	private DiaryReadService diaryReadService;

	@RequestMapping
    public String home(@ModelAttribute("fdata") FData fdata,
                       Model model,
                       HttpServletResponse response) {

        disableCache(response);

        DailyLogViewDto log = diaryReadService.readByDate(fdata.getLogDate());

        model.addAttribute("log",log);

        return "home";
    }

}
