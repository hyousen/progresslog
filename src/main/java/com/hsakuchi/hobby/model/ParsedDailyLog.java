package com.hsakuchi.hobby.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ParsedDailyLog {

    private LocalDate date;
    private String displayDate;
    private List<ParsedTimeBlock> blocks = new ArrayList<>();
//    private String displayFileName;

    // getter / setter 
}
