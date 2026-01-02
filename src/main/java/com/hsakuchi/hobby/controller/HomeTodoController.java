package com.hsakuchi.hobby.controller;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hsakuchi.hobby.model.TodoViewDto;
import com.hsakuchi.hobby.service.TodoReadService;
import com.hsakuchi.hobby.service.TodoWriteService;

@Controller
@SessionAttributes("fdata") // ← fdata使ってなくても問題なし
public class HomeTodoController extends BaseHomeController{

    @Autowired
    private TodoReadService todoReadService;

    @Autowired
    private TodoWriteService todoWriteService;

    /* ===========================
       TODO 一覧
       ========================== */
    @GetMapping("/home/todo")
    public String todo(
            Model model,
            HttpServletResponse response) {

        disableCache(response);

        String fileName = "todo";

        TodoViewDto todo = todoReadService.read();

        model.addAttribute("fileName", fileName);
        model.addAttribute("todo", todo);
        model.addAttribute("exists", todo.isExists());

        return "todo_list";
    }

    /* ===========================
       TODO 編集画面
       ========================== */
    @GetMapping("/home/todo/edit")
    public String editTodo(
            Model model,
            HttpServletResponse response) {

        disableCache(response);

        String fileName = "todo";

        TodoViewDto todo = todoReadService.read();

        model.addAttribute("fileName", fileName);
        model.addAttribute("todoText", todo.getText());

        return "todo_edit";
    }

    /* ===========================
       TODO 保存
       ========================== */
    @PostMapping("/home/todo/save")
    public String saveTodo(
            @RequestParam("todoText") String todoText,
            RedirectAttributes attrs) {

        todoWriteService.save(todoText);

        attrs.addFlashAttribute("message", "TODOを保存しました");
        return "redirect:/home/todo";
    }
    
}
