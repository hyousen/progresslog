package com.hsakuchi.hobby.model;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;


@Getter
public class TodoViewDto {

    private final String text;
    private final List<String> lines;
    private final boolean exists;
    private final String displayFileName;


    private TodoViewDto(String text, List<String> lines, boolean exists, String displayFileName) {
        this.text = text;
        this.lines = lines;
        this.exists = exists;
        this.displayFileName = displayFileName;
    }

    public static TodoViewDto empty() {
        return new TodoViewDto("", List.of(), false,"todo");
    }

    public static TodoViewDto from(String raw) {
        List<String> lines = Arrays.asList(raw.split("\\R"));
        return new TodoViewDto(raw, lines, true,"todo");
    }

}
