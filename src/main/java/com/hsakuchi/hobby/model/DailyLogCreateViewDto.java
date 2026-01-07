package com.hsakuchi.hobby.model;

import java.time.LocalDate;

import com.hsakuchi.hobby.util.FileNameUtil;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DailyLogCreateViewDto {

	LocalDate date;
	String title;
	boolean exists;
	String message;

	/* ===== factory methods ===== */
	
	/**
	 * ファイルが存在しない場合の表示用DTOを生成します。
	 * 新規作成可能な状態を表します。
	 * 
	 * @param date 対象日付（nullの場合は例外）
	 * @return ファイル未存在を示すDTO
	 * @throws IllegalArgumentException dateがnullの場合
	 */
	public static DailyLogCreateViewDto notExists(LocalDate date) {
		if (date == null) {
	        throw new IllegalArgumentException("日付がnullです");
	    }
		
		return DailyLogCreateViewDto.builder()
				.date(date)
				.title(FileNameUtil.buildFileName(date))
				.exists(false)
				.message(null)
				.build();

	}

	/**
     * ファイルが既に存在する場合のDTOを生成
     * 
     * @param date 対象日付
     * @return 既存ファイルを示すDTO
     */
	public static DailyLogCreateViewDto exists(LocalDate date) {
		return DailyLogCreateViewDto.builder()
				.date(date)
				.title(FileNameUtil.buildFileName(date))
				.exists(true)
				.message("ファイルは既に存在します。")
				.build();   }

}