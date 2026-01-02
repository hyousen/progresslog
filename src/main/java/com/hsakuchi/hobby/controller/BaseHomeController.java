package com.hsakuchi.hobby.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ModelAttribute;

import com.hsakuchi.hobby.model.FData;

public abstract class BaseHomeController {

    /* ===========================
       セッション共通データ
       ========================== */
    @ModelAttribute("fdata")
    public FData setUpFdata() {
        FData fdata = new FData();
        fdata.setLogDate(LocalDate.now());
        return fdata;
    }

    /* ===========================
       キャッシュ無効化
       ========================== */
    protected void disableCache(HttpServletResponse res) {
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        res.setHeader("Pragma", "no-cache");
        res.setDateHeader("Expires", 0);
    }

    /* ===========================
       ファイル名生成
       ========================== */
    protected String createFileName(LocalDate date) {
		return "log" + date.format(DateTimeFormatter.BASIC_ISO_DATE); // yyyyMMdd
	}
}
