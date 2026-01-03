package com.hsakuchi.hobby.service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hsakuchi.hobby.model.ParsedDailyLog;
import com.hsakuchi.hobby.model.ParsedTimeBlock;


@Service
public class DailyLogParser {

	private static final Logger log = LoggerFactory.getLogger(DailyLogParser.class);

	// 時刻フォーマット
	private static final DateTimeFormatter TIME_FORMAT = 
			DateTimeFormatter.ofPattern("HH:mm");

	// 日付フォーマット（例: 2026年1月3日(金)）
	private static final DateTimeFormatter DATE_FORMAT = 
			DateTimeFormatter.ofPattern("yyyy年MM月dd日(E)");

	// 正規表現パターン
	private static final Pattern DATE_LINE_PATTERN = 
			Pattern.compile("(\\d{4})年(\\d{1,2})月(\\d{1,2})日\\((.)\\)");

	private static final Pattern TIME_PATTERN = 
			Pattern.compile("\\d{2}:\\d{2}");

	private static final Pattern TRAILING_DATE_PATTERN = 
			Pattern.compile("\\d{1,2}日\\(.\\)\\d{2}:\\d{2}:\\d{2}$");


	//	private static final DateTimeFormatter TIME_FMT =DateTimeFormatter.ofPattern("HH:mm");
	//
	//	private static final String TRAILING_DATE_REGEX ="\\d{1,2}日\\(.\\)\\d{2}:\\d{2}:\\d{2}$";

	public ParsedDailyLog parse(String text) {
		ParsedDailyLog parsedLog = new ParsedDailyLog();

		if (text == null || text.isBlank()) {
			log.debug("空のテキストが渡されました");
			return parsedLog;
		}
		//		ParsedDailyLog log = new ParsedDailyLog();
		ParsedTimeBlock currentBlock = null;

		//		if (text == null || text.isBlank()) {
		//			return log;
		//		}

		String[] lines = text.split("\\R");

		for (String line : lines) {
			line = line.trim();

			// 空行はスキップ
			if (line.isEmpty()) {
				continue;
			}

			//		for (String line : text.split("\\R")) {
			//
			//			line = line.trim();
			//			if (line.isEmpty()) continue;

			// ① 日付行
			if (parsedLog.getDisplayDate() == null && isDateLine(line)) {
				parsedLog.setDisplayDate(line);
				continue;
			}

			// ② 時刻行（04:18）
//			if (isTimeLine(line)) {
//				currentBlock = new ParsedTimeBlock();
//				currentBlock.setTime(LocalTime.parse(line, TIME_FMT));
//				log.getBlocks().add(currentBlock);
//				continue;
//			}
			
			// ② 時刻行の処理（04:18）
            if (isTimeLine(line)) {
                LocalTime time = tryParseTime(line);
                if (time != null) {
                    currentBlock = new ParsedTimeBlock();
                    currentBlock.setTime(time);
                    parsedLog.getBlocks().add(currentBlock);
                }
                continue;
            }

			// ③ 箇条書き
//			if (line.startsWith("・")) {
//
//				if (currentBlock == null) {
//					currentBlock = new ParsedTimeBlock(); // time = null
//					log.getBlocks().add(currentBlock);
//				}
//
//				String textOnly =
//						stripTrailingDate(line.substring(1));
//
//				currentBlock.getItems().add(textOnly);
//			}
			
			// ③ 箇条書きの処理（・で始まる行）
            if (line.startsWith("・")) {
                // 時刻ブロックがない場合は作成
                if (currentBlock == null) {
                    currentBlock = new ParsedTimeBlock(); // time = null
                    parsedLog.getBlocks().add(currentBlock);
                }
                
                String itemText = line.substring(1).trim();
                String cleanedText = stripTrailingDate(itemText);
                
                if (!cleanedText.isEmpty()) {
                    currentBlock.getItems().add(cleanedText);
                }
            }
		}

		return parsedLog;
	}

//	private boolean isTime(String line) {
//		return line.matches("\\d{2}:\\d{2}");
//	}
//
//	private boolean isDateLine(String line) {
//		return line.matches("\\d{4}年\\d{1,2}月\\d{1,2}日\\(.\\)");
//	}

//	private String stripTrailingDate(String text) {
//		return text.replaceAll(TRAILING_DATE_REGEX, "");
//	}
	
	/**
     * 時刻行かどうかを判定
     */
    private boolean isTimeLine(String line) {
        return TIME_PATTERN.matcher(line).matches();
    }
    
    /**
     * 日付行かどうかを判定
     */
    private boolean isDateLine(String line) {
        return DATE_LINE_PATTERN.matcher(line).matches();
    }
    
	 /**
     * 時刻をパース（失敗時はnull）
     */
    private LocalTime tryParseTime(String line) {
        try {
            return LocalTime.parse(line, TIME_FORMAT);
        } catch (DateTimeParseException e) {
            log.warn("時刻のパースに失敗: line={}, error={}", line, e.getMessage());
            return null;
        }
    }
    
    /**
     * 末尾の日付タイムスタンプを削除
     * 例: "作業内容3日(金)12:34:56" → "作業内容"
     */
    private String stripTrailingDate(String text) {
        return TRAILING_DATE_PATTERN.matcher(text).replaceAll("").trim();
    }
}
