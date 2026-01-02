package com.hsakuchi.hobby.controller;

import java.time.LocalDate;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hsakuchi.hobby.model.DailyLogViewDto;
import com.hsakuchi.hobby.model.FData;
import com.hsakuchi.hobby.service.DiaryReadService;
import com.hsakuchi.hobby.service.DiaryWriteService;

@Controller
@SessionAttributes("fdata")
public class HomeDiaryController extends BaseHomeController {

    @Autowired
    private DiaryReadService diaryReadService;

    @Autowired
    private DiaryWriteService diaryWriteService;

    /* ===========================
       日記表示
       ========================== */
    @GetMapping("/home/diary")
    public String diary(
            @ModelAttribute("fdata") FData fdata,
            Model model,
            HttpServletResponse response) {

        disableCache(response);

        if (fdata.getLogDate() == null) {
            fdata.setLogDate(LocalDate.now());
        }

        LocalDate date = fdata.getLogDate();
        String fileName = createFileName(date);

        DailyLogViewDto log = diaryReadService.readByDate(date);

        model.addAttribute("fileName", fileName);
        model.addAttribute("log", log);

        return "log_list";
    }

    /* ===========================
       日記保存
       ========================== */
    @PostMapping("/home/diary/write")
    public String writeDiary(
            @ModelAttribute("fdata") FData fdata,
            @RequestParam("sentence") String sentence,
            RedirectAttributes attrs) {

        diaryWriteService.write(
                fdata.getLogDate(),
                fdata.getTagType(),
                sentence
        );

        attrs.addFlashAttribute("message", "保存しました");
        return "redirect:/home/diary";
    }
    
}
