package com.hsakuchi.hobby.service;

import java.io.IOException;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hsakuchi.hobby.model.DailyLogViewDto;
import com.hsakuchi.hobby.model.ParsedDailyLog;
import com.hsakuchi.hobby.util.FileNameUtil;

@Service
public class DiaryReadService {

	private static final Logger log = LoggerFactory.getLogger(DiaryReadService.class);
    
	private final FileService fileService;
	private final DailyLogParser parser;

	public DiaryReadService(FileService fileService,DailyLogParser parser) {
		this.fileService = fileService;
		this.parser = parser;
	}


	/**
	 * 指定日付の日記を読み込んでDTOに変換
	 * 
	 * @param date 読み込む日付
	 * @return 日記のDTO（読み込めない場合は空のDTO）
	 */
	public DailyLogViewDto readByDate(LocalDate date) {
		if (date == null) {
			log.warn("日付がnullです");
			return DailyLogViewDto.empty(LocalDate.now());
		}
		String raw;
        try {
            raw = fileService.readDiary(date);
        } catch (IOException e) {
            log.error("日記の読み込みに失敗しました: date={}, error={}", date, e.getMessage());
            return DailyLogViewDto.empty(date);
        }

     // ファイルが存在しないか空の場合
        if (raw == null || raw.isBlank()) {
            log.debug("日記が空です: date={}", date);
            return DailyLogViewDto.empty(date);
        }

		ParsedDailyLog parsed = parser.parse(raw);

		String title = FileNameUtil.buildFileName(date);

		return DailyLogViewDto.from(parsed, title);
	}

//	private String buildTitle(LocalDate date) {
//		return "log" + date.format(DateTimeFormatter.BASIC_ISO_DATE);
//	}



}