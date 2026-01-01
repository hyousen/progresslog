package com.hsakuchi.hobby.service;

import org.springframework.stereotype.Service;

@Service
public class TodoWriteService {

    private final FileService fileService;

    public TodoWriteService(FileService fileService) {
        this.fileService = fileService;
    }

    public void save(String text) {
        try {
            fileService.overwriteByKey("todo", text);
        } catch (Exception e) {
            // 何もなかったことにする or ログだけ
        }
    }
}
