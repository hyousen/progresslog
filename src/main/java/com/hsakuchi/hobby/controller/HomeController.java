package com.hsakuchi.hobby.controller;

import java.time.LocalDate;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.hsakuchi.hobby.model.DailyLogViewDto;
import com.hsakuchi.hobby.model.FData;
import com.hsakuchi.hobby.service.DiaryReadService;

@Controller
//@RequestMapping("/home")
@SessionAttributes("fdata")
public class HomeController {
	@Autowired
	private DiaryReadService diaryReadService;
	
	@ModelAttribute("fdata")
	public FData setUpFdata() {
		// LocalDate初期化
		FData fdata = new FData();
		fdata.setLogDate(LocalDate.now());
		return fdata;
	}
//
    @GetMapping("/home")
    public String home(@ModelAttribute("fdata") FData fdata,
                       Model model,
                       HttpServletResponse response) {

        disableCache(response);
        fdata.setLogDate(fdata.getLogDate());

        DailyLogViewDto log = diaryReadService.readByDate(fdata.getLogDate());

        model.addAttribute("log",log);
       // fdata.setLogDate(log.getDate());

        return "home";
    }

//    @GetMapping("/select")
//    public String select(@ModelAttribute("fdata") FData fdata) {
//        return "redirect:/home/create";
//    }
//    
    private void disableCache(HttpServletResponse res) {
		res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		res.setHeader("Pragma", "no-cache");
		res.setDateHeader("Expires", 0);
	}

}
