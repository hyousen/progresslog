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
            // ① ファイルがなければ作る
            if (!fileService.existsDiary(logDate)){
                fileService.createDiary(logDate);

                // ② 最初の1行に日付を書く
                fileService.writeDiary(logDate, formatDisplayDate(logDate));
            }

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
            if (!fileService.existsDiary(date)) {
                fileService.createDiary(date);
                fileService.writeDiary(date, formatDisplayDate(date));
            }
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
}

//@Service
//public class DiaryWriteService {
//
//    private final FileService fileService;
//
//    public DiaryWriteService(FileService fileService) {
//        this.fileService = fileService;
//    }
//
//    public void appendEntry(String fileName, LogEntryDto entry) throws IOException {
//        String line = formatEntry(entry);
//        fileService.write(fileName, line);
//    }
//
//    private String formatEntry(LogEntryDto entry) {
//        // フォーマット責務
//    	StringBuilder sb = new StringBuilder();
//
//        // 時間がある場合のみ出力
//        if (entry.getTime() != null) {
//            sb.append(entry.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
//            sb.append(System.lineSeparator());
//        }
//
//        sb.append("・");
//        sb.append(entry.getContent());
//
//        return sb.toString();
//    }
    

    
    /** 新しいログファイルを作り、先頭に日付を書く 
     * @throws IOException */
//    public void createNewDiary(String fileName, LocalDate date) throws IOException {
//
//        String displayDate = formatDisplayDate(date);
//        fileService.write(fileName, displayDate + System.lineSeparator());
//    }
//
//    private String formatDisplayDate(LocalDate date) {
//        DateTimeFormatter fmt =
//                DateTimeFormatter.ofPattern("yyyy年MM月dd日(E)", Locale.JAPAN);
//        return date.format(fmt);
//    }


