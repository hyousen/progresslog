package com.hsakuchi.hobby.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.hsakuchi.hobby.util.FileNameUtil;

import lombok.Builder;
import lombok.Value;

/**
 * 日記表示用DTO
 */
@Value
@Builder
public class DailyLogViewDto {
	
	private static final DateTimeFormatter DISPLAY_DATE_FORMAT = 
            DateTimeFormatter.ofPattern("yyyy年MM月dd日(E)");

	private final String displayDate;
	private final String title;
	@Builder.Default
	private final List<TimeBlockViewDto> blocks = List.of();
	private final boolean exists;

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
	
	

    /**
     * ParsedDailyLogから生成（タイトル自動生成）
     */
    public static DailyLogViewDto from(ParsedDailyLog parsed) {
        LocalDate date = parsed.getDate(); // ← ParsedDailyLogにgetDate()があると仮定
        return from(parsed, FileNameUtil.buildFileName(date));
    }
//	public static DailyLogViewDto from(ParsedDailyLog parsed) {
//	    return from(parsed, null);
//	}


	
	/**
     * ParsedDailyLogから生成（タイトル指定）
     */
    public static DailyLogViewDto from(ParsedDailyLog parsed, String title) {
        List<TimeBlockViewDto> viewBlocks = parsed.getBlocks().stream()
                .map(TimeBlockViewDtoFactory::from)
                .toList();
        
        return DailyLogViewDto.builder()
                .displayDate(parsed.getDisplayDate())
                .title(title)
                .blocks(viewBlocks)
                .exists(true)
                .build();
    }
//	public static DailyLogViewDto from(
//			ParsedDailyLog parsed,
//			String title
//			) {
//		List<TimeBlockViewDto> viewBlocks =
//				parsed.getBlocks().stream()
//				.map(TimeBlockViewDtoFactory::from)
//				.toList();
//
//		return new DailyLogViewDto(
//				parsed.getDisplayDate(),
//				title,
//				viewBlocks,
//				true
//				);
//	}


    
    /**
     * 空のDTOを生成（ファイルが存在しない場合）
     */
    public static DailyLogViewDto empty(LocalDate date) {
        return DailyLogViewDto.builder()
                .displayDate(formatDisplayDate(date))
                .title(FileNameUtil.buildFileName(date))
                .blocks(List.of())
                .exists(false)
                .build();
    }
//	public static DailyLogViewDto empty(LocalDate date) {
//		return new DailyLogViewDto(
//				date.toString(),
//				"log" + date.format(DateTimeFormatter.BASIC_ISO_DATE),
//				List.of(),
//				false
//				);
//	}
    
    /**
     * 表示用の日付文字列を生成
     */
    private static String formatDisplayDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(DISPLAY_DATE_FORMAT);
    }

}

