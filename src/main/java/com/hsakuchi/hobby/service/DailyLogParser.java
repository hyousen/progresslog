package com.hsakuchi.hobby.service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.hsakuchi.hobby.model.ParsedDailyLog;
import com.hsakuchi.hobby.model.ParsedTimeBlock;


@Service
public class DailyLogParser {

	private static final DateTimeFormatter TIME_FMT =DateTimeFormatter.ofPattern("HH:mm");

	private static final String TRAILING_DATE_REGEX ="\\d{1,2}日\\(.\\)\\d{2}:\\d{2}:\\d{2}$";

	public ParsedDailyLog parse(String text) {

		ParsedDailyLog log = new ParsedDailyLog();
		ParsedTimeBlock currentBlock = null;
		
		if (text == null || text.isBlank()) {
            return log;
        }

		for (String line : text.split("\\R")) {

			line = line.trim();
			if (line.isEmpty()) continue;

			// ① 日付行
			if (log.getDisplayDate() == null && isDateLine(line)) {
				log.setDisplayDate(line);
				continue;
			}

			// ② 時刻行（04:18）
			if (isTime(line)) {
				currentBlock = new ParsedTimeBlock();
				currentBlock.setTime(LocalTime.parse(line, TIME_FMT));
				log.getBlocks().add(currentBlock);
				continue;
			}

			// ③ 箇条書き
			if (line.startsWith("・")) {

				if (currentBlock == null) {
					currentBlock = new ParsedTimeBlock(); // time = null
					log.getBlocks().add(currentBlock);
				}

				String textOnly =
						stripTrailingDate(line.substring(1));

				currentBlock.getItems().add(textOnly);
			}
		}

		return log;
	}

	private boolean isTime(String line) {
		return line.matches("\\d{2}:\\d{2}");
	}

	private boolean isDateLine(String line) {
		return line.matches("\\d{4}年\\d{1,2}月\\d{1,2}日\\(.\\)");
	}

	private String stripTrailingDate(String text) {
		return text.replaceAll(TRAILING_DATE_REGEX, "");
	}
}
