package com.hsakuchi.hobby.controller;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hsakuchi.hobby.model.FData;
import com.hsakuchi.hobby.service.DiaryWriteService;
import com.hsakuchi.hobby.service.FileService;

@Controller
@SessionAttributes("fdata")
public class HomeCreateController extends BaseHomeController{

    @Autowired
    private FileService fileService;

    @Autowired
    private DiaryWriteService diaryWriteService;

    /* ===========================
       記事作成確認（GET）
       ========================== */
    @GetMapping("/home/create")
    public String createConfirm(
            Model model,
            @ModelAttribute("fdata") FData fdata)
            throws IOException {

        LocalDate date = fdata.getLogDate();
        String fileName = createFileName(date);

        boolean exists = fileService.exists(fileName);
        fdata.setFileExist(exists);

        model.addAttribute("exists", exists);
        model.addAttribute("fileName", fileName);

        if (exists) {
            model.addAttribute("message", "ファイルは既に存在します。");
        }

        return "create"; // ← 元と同じ
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

            attrs.addFlashAttribute("fdata", fdata);
            attrs.addFlashAttribute("message", "記事を開きました");
            attrs.addFlashAttribute("exists", true);

            return "redirect:/home/diary";

        } catch (Exception e) {
            model.addAttribute("message", "作成に失敗: " + e.getMessage());
            model.addAttribute("exists", false);
            return "create";
        }
    }

}
