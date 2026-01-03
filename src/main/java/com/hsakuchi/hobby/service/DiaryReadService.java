package com.hsakuchi.hobby.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.hsakuchi.hobby.model.DailyLogViewDto;
import com.hsakuchi.hobby.model.ParsedDailyLog;

@Service
public class DiaryReadService {


	private final FileService fileService;
	private final DailyLogParser parser;

	public DiaryReadService(FileService fileService,DailyLogParser parser) {
		this.fileService = fileService;
		this.parser = parser;
	}

	public DailyLogViewDto readByDate(LocalDate date) {
		//		String fileName = "log" + date.format(DateTimeFormatter.BASIC_ISO_DATE);

		String raw;
		try {
			raw = fileService.readDiary(date);
		} catch (IOException e) {
			// 読めない = 表示できない
			return DailyLogViewDto.empty(date);
		}

		if (raw == null || raw.isBlank()) {
			return DailyLogViewDto.empty(date);
		}

		ParsedDailyLog parsed = parser.parse(raw);

		String title = buildTitle(date);

		return DailyLogViewDto.from(parsed, title);
	}

	private String buildTitle(LocalDate date) {
		return "log" + date.format(DateTimeFormatter.BASIC_ISO_DATE);
	}



}