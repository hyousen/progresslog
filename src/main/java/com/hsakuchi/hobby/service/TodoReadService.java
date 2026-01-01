package com.hsakuchi.hobby.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.hsakuchi.hobby.model.TodoViewDto;

@Service
public class TodoReadService {

    private final FileService fileService;

    public TodoReadService(FileService fileService) {
        this.fileService = fileService;
    }

    public TodoViewDto read() {
        try {
            String raw = fileService.readByKey("todo");

            if (raw == null || raw.isBlank()) {
                return TodoViewDto.empty();
            }

            return TodoViewDto.from(raw);

        } catch (IOException e) {
            // 何もなかったことにする
            return TodoViewDto.empty();
        }
    }
}
