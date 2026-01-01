package com.hsakuchi.hobby.model;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;

@Getter
public class DailyLogViewDto {

    private final String displayDate;
    private final List<TimeBlockViewDto> blocks;
    private final boolean exists;
//    private final String displayFileName;


    private DailyLogViewDto(
            String displayDate,
            List<TimeBlockViewDto> blocks,
            boolean exists) {
        this.displayDate = displayDate;
        this.blocks = blocks;
        this.exists = exists;
    }

    public static DailyLogViewDto from(ParsedDailyLog parsed) {
        List<TimeBlockViewDto> viewBlocks =
                parsed.getBlocks().stream()
                        .map(TimeBlockViewDtoFactory::from)
                        .toList();

        return new DailyLogViewDto(
                parsed.getDisplayDate(),
                viewBlocks,
                true
        );
    }

    public static DailyLogViewDto empty(LocalDate date) {
        return new DailyLogViewDto(
                date.toString(),
                List.of(),
                false
        );
    }

}

