package com.hsakuchi.hobby.model;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LogEntryDto {
    private LocalDate date;          // 2025-12-22
    private LocalTime time;          // 04:18（ない場合 null）
    private String content;          // 起床 / よく眠れた
}
