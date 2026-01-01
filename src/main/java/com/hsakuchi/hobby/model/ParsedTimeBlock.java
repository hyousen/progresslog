package com.hsakuchi.hobby.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ParsedTimeBlock {

    private LocalTime time;
    private List<String> items = new ArrayList<>();

    // getter / setter
}
