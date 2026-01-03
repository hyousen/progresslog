package com.hsakuchi.hobby.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

@Service
public class DiaryWriteService {

    private final FileService fileService;

    public DiaryWriteService(FileService fileService) {
        this.fileService = fileService;
    }

    public void write(LocalDate logDate, String tagType, String sentence) {

        try {
        	
        	ensureDiaryExists(logDate);
            // ① ファイルがなければ作る
//            if (!fileService.existsDiary(logDate)){
//                fileService.createDiary(logDate);
//
//                // ② 最初の1行に日付を書く
//                fileService.writeDiary(logDate, formatDisplayDate(logDate));
//            }

            // ③ 本文を整形
            String line = formatLine(tagType, sentence);

            // ④ 書き込み
            fileService.writeDiary(logDate, line);

        } catch (IOException e) {
            throw new RuntimeException("日記の書き込みに失敗しました", e);
        }
    }
    
    public void openOrCreate(LocalDate date) {
        try {
        	
        	ensureDiaryExists(date);
//            if (!fileService.existsDiary(date)) {
//                fileService.createDiary(date);
//                fileService.writeDiary(date, formatDisplayDate(date));
//            }
        } catch (IOException e) {
            throw new RuntimeException("日記の作成に失敗しました", e);
        }
    }


    // ====== private helpers ======

    private String formatDisplayDate(LocalDate date) {
        DateTimeFormatter fmt =
            DateTimeFormatter.ofPattern("yyyy年MM月dd日(E)");
        return date.format(fmt);
    }

    private String formatLine(String tagType, String sentence) {

        LocalDateTime now = LocalDateTime.now();

        if ("with".equals(tagType)) {
            return "・" + sentence +
                   now.format(DateTimeFormatter.ofPattern("dd日(E)HH:mm:ss"));
        }

        // time の場合
        return now.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
    
    private void ensureDiaryExists(LocalDate date) throws IOException {
        if (!fileService.existsDiary(date)) {
            fileService.createDiary(date);
            fileService.writeDiary(date, formatDisplayDate(date));
        }
    }
}


