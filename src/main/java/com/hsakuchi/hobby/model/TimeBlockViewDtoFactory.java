package com.hsakuchi.hobby.model;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class TimeBlockViewDtoFactory {

    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("HH:mm");

    public static TimeBlockViewDto from(ParsedTimeBlock block) {

        String time = block.getTime() != null
                ? block.getTime().format(TIME_FMT)
                : "";

        List<String> items = block.getItems();

        return new TimeBlockViewDto(time, items);
    }
}
