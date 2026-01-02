//package com.hsakuchi.hobby.controller;
//
//import java.io.IOException;
//import java.time.LocalDate;
//
//import jakarta.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.SessionAttributes;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import com.hsakuchi.hobby.model.DailyLogViewDto;
//import com.hsakuchi.hobby.model.FData;
//import com.hsakuchi.hobby.service.DiaryReadService;
//import com.hsakuchi.hobby.service.DiaryWriteService;
//
//@Controller
//@RequestMapping("/diary")
//@SessionAttributes("fdata")
//public class DiaryController {
//	@Autowired
//	private DiaryReadService diaryReadService;
//	@Autowired
//	private DiaryWriteService diaryWriteService;
//	
//	@ModelAttribute("fdata")
//	public FData setUpFdata() {
//		// LocalDate初期化
//		FData fdata = new FData();
//		fdata.setLogDate(LocalDate.now());
//		return fdata;
//	}
//
//
//    @GetMapping
//    public String diary(@ModelAttribute("fdata") FData fdata,
//                        Model model,
//                        HttpServletResponse response) {
//
//        disableCache(response);
//
//        DailyLogViewDto log = diaryReadService.readByDate(fdata.getLogDate());
//
//        model.addAttribute("log", log);
//        return "log_list";
//    }
//
//    @GetMapping("/create")
//    public String createConfirm(@ModelAttribute("fdata") FData fdata,
//                                Model model) throws IOException {
//
//        DailyLogViewDto log = diaryReadService.readByDate(fdata.getLogDate());
//
//        model.addAttribute("log", log);
//        return "diary/create";
//    }
//
//    @PostMapping("/create")
//    public String create(@ModelAttribute("fdata") FData fdata,
//                         RedirectAttributes attrs) {
//
//        diaryWriteService.openOrCreate(fdata.getLogDate());
//        attrs.addFlashAttribute("message", "記事を開きました");
//
//        return "redirect:/diary";
//    }
//
//    @PostMapping("/write")
//    public String write(@ModelAttribute("fdata") FData fdata,
//                        @RequestParam String sentence,
//                        RedirectAttributes attrs) {
//
//        diaryWriteService.write(
//                fdata.getLogDate(),
//                fdata.getTagType(),
//                sentence
//        );
//
//        attrs.addFlashAttribute("message", "保存しました");
//        return "redirect:/diary";
//    }
//    
//    private void disableCache(HttpServletResponse res) {
//		res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
//		res.setHeader("Pragma", "no-cache");
//		res.setDateHeader("Expires", 0);
//	}
//}
