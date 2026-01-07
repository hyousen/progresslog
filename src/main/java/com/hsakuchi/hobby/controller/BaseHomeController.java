package com.hsakuchi.hobby.controller;

import java.time.LocalDate;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ModelAttribute;

import com.hsakuchi.hobby.model.FData;
import com.hsakuchi.hobby.util.FileNameUtil;

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
		return FileNameUtil.buildFileName(date);
	}
}
