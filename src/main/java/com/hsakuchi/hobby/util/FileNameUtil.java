package com.hsakuchi.hobby.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FileNameUtil {
    
    private static final String FILE_PREFIX_LOG = "log";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;
    
    private FileNameUtil() {
        // ユーティリティクラスなのでインスタンス化禁止
    }
    
    /**
     * 日付からファイル名を生成
     * 例: 2026-01-03 → log20260103
     */
    public static String buildFileName(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("日付がnullです");
        }
        return FILE_PREFIX_LOG + date.format(FORMATTER);
    }
    
    /**
     * ファイル名から日付を抽出
     * 例: log20260103 → 2026-01-03
     */
//    public static LocalDate parseFileName(String fileName) {
//        if (fileName == null || !fileName.startsWith(FILE_PREFIX)) {
//            throw new IllegalArgumentException("不正なファイル名形式です: " + fileName);
//        }
//        String dateStr = fileName.substring(FILE_PREFIX.length());
//        return LocalDate.parse(dateStr, FORMATTER);
//    }
}