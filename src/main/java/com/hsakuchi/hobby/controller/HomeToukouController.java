package com.hsakuchi.hobby.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.hsakuchi.hobby.model.FData;

@Controller
@SessionAttributes("fdata")
public class HomeToukouController extends BaseHomeController{

    @GetMapping("/home/toukou")
    public String toukou(
            Model model,
            @ModelAttribute("fdata") FData fdata,
            @RequestParam(required = false) LocalDate logDate) {

        // URLパラメータの日付を最優先
        if (logDate != null) {
            fdata.setLogDate(logDate);
        }

        LocalDate date = fdata.getLogDate();
        String fileName = createFileName(date);

        // 入力初期値
        fdata.setPostText("");
        fdata.setTagType("with");

        model.addAttribute("fileName", fileName);

        return "toukou";
    }

}
