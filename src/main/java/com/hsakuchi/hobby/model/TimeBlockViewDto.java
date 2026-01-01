package com.hsakuchi.hobby.model;

import java.util.List;

import lombok.Getter;

@Getter
public class TimeBlockViewDto {

    private final String time;        // 表示用（"09:00" / ""）
    private final List<String> items; // 表示用テキストのみ

    public TimeBlockViewDto(String time, List<String> items) {
        this.time = time;
        this.items = items;
    }

}
