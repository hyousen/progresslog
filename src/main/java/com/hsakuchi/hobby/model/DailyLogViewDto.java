package com.hsakuchi.hobby.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import lombok.Getter;

@Getter
public class DailyLogViewDto {

	private final String displayDate;
	private final String title;
	private final List<TimeBlockViewDto> blocks;
	private final boolean exists;
	//    private final String displayFileName;


	private DailyLogViewDto(
			String displayDate,
			String title,
			List<TimeBlockViewDto> blocks,
			boolean exists) {
		this.displayDate = displayDate;
		this.title= title;
		this.blocks = blocks;
		this.exists = exists;
	}

//	public static DailyLogViewDto from(ParsedDailyLog parsed) {
//		List<TimeBlockViewDto> viewBlocks =
//				parsed.getBlocks().stream()
//				.map(TimeBlockViewDtoFactory::from)
//				.toList();
//
//		return new DailyLogViewDto(
//				parsed.getDisplayDate(),
//				null,
//				viewBlocks,
//				true
//				);
//	}
	
	public static DailyLogViewDto from(ParsedDailyLog parsed) {
	    return from(parsed, null);
	}


	public static DailyLogViewDto from(
			ParsedDailyLog parsed,
			String title
			) {
		List<TimeBlockViewDto> viewBlocks =
				parsed.getBlocks().stream()
				.map(TimeBlockViewDtoFactory::from)
				.toList();

		return new DailyLogViewDto(
				parsed.getDisplayDate(),
				title,
				viewBlocks,
				true
				);
	}


	public static DailyLogViewDto empty(LocalDate date) {
		return new DailyLogViewDto(
				date.toString(),
				"log" + date.format(DateTimeFormatter.BASIC_ISO_DATE),
				List.of(),
				false
				);
	}

}

