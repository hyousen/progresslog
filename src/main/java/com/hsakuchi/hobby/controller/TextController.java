package com.hsakuchi.hobby.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hsakuchi.hobby.model.DailyLogViewDto;
import com.hsakuchi.hobby.model.FData;
import com.hsakuchi.hobby.model.TodoViewDto;
import com.hsakuchi.hobby.service.DiaryReadService;
import com.hsakuchi.hobby.service.DiaryWriteService;
import com.hsakuchi.hobby.service.FileService;
import com.hsakuchi.hobby.service.TodoReadService;
import com.hsakuchi.hobby.service.TodoWriteService;

@SessionAttributes("fdata")
@Controller
public class TextController {
	@Autowired
	private FileService fileService;
	@Autowired
	private DiaryWriteService diaryWriteService;
	@Autowired
	private DiaryReadService diaryReadService;
	@Autowired
	private TodoReadService todoReadService;
	@Autowired
	private TodoWriteService todoWriteService;

	@ModelAttribute("fdata")
	public FData setUpFdata() {
		// LocalDate初期化
		FData fdata = new FData();
		fdata.setLogDate(LocalDate.now());
		return fdata;
	}

	// ファイル名生成：yyyyMMdd形式
	private String createFileName(LocalDate date) {
		return "log" + date.format(DateTimeFormatter.BASIC_ISO_DATE); // yyyyMMdd
	}
	
	/**
	 * 日記・TODO 画面に関する画面遷移を担当する Controller。
	 *
	 * 【本来の責務】
	 * ・リクエストを受け取る
	 * ・Service を呼ぶ
	 * ・View に渡す Model を組み立てる
	 *
	 * 【現状の問題】
	 * ・表示加工ロジック
	 * ・日付/文字列整形
	 * ・ファイル名生成
	 * が混在している
	 */


	/* ===========================
	記事作成確認（GET）
	============================ */
	@GetMapping("/home/create")
	public String createConfirm(Model model, @ModelAttribute("fdata") FData fdata)
			throws IOException {

		LocalDate date = fdata.getLogDate();
		String fileName = createFileName(date);

		boolean exists = fileService.exists(fileName);
		fdata.setFileExist(exists);

		model.addAttribute("exists", exists);
		model.addAttribute("fileName", fileName);
		if (exists) {
			model.addAttribute("message", "ファイルは既に存在します。");
		}

		return "create";
	}

	/* ===========================
	記事作成実行（POST）
	============================ */
	@PostMapping("/home/create")
	public String createDo(
			@ModelAttribute("fdata") FData fdata,
			RedirectAttributes attrs,
			Model model) {

		LocalDate date = fdata.getLogDate();

		try {
			diaryWriteService.openOrCreate(date);

			// redirect先へ fdata と付帯情報を渡す
			attrs.addFlashAttribute("fdata", fdata);
			attrs.addFlashAttribute("message", "記事を開きました");
			attrs.addFlashAttribute("exists", true);

			return "redirect:/home/diary";

		} catch (Exception e) {
			model.addAttribute("message", "作成に失敗: " + e.getMessage());
			model.addAttribute("exists", false);
			return "create";
		}
	}

	@GetMapping("/home/diary")
	public String diary(@ModelAttribute("fdata") FData fdata,
			Model model,
			HttpServletResponse response) {

		disableCache(response);

		if (fdata.getLogDate() == null) {
			fdata.setLogDate(LocalDate.now());
		}

		LocalDate date = fdata.getLogDate();
		String fileName = createFileName(date);

		DailyLogViewDto log = diaryReadService.readByDate(date);

		model.addAttribute("fileName", fileName);
		model.addAttribute("log", log);

		return "log_list"; // ← これでOK！
	}

	@PostMapping("/home/diary/write")
	public String writeDiary(
			@ModelAttribute("fdata") FData fdata,
			@RequestParam("sentence") String sentence,
			RedirectAttributes attrs) {


		diaryWriteService.write(
				fdata.getLogDate(),
				fdata.getTagType(),
				sentence
				);

		attrs.addFlashAttribute("message", "保存しました");
		return "redirect:/home/diary";
	}

	@GetMapping("/home/toukou")
	public String toukou(Model model, @ModelAttribute("fdata") FData fdata,@RequestParam(required = false) LocalDate logDate) {


		// ★ URL から来た日付を最優先で使う
		if (logDate != null) {
			fdata.setLogDate(logDate);
		}
		LocalDate date = fdata.getLogDate();
		String fileName = createFileName(date);

		// 入力初期値設定
		fdata.setPostText("");
		fdata.setTagType("with");

		model.addAttribute("fileName", fileName);

		return "toukou";
	}

	@RequestMapping("/home")
	public String home(Model model,
			@ModelAttribute("fdata") FData fdata,
			HttpServletResponse response) {

		disableCache(response);

		LocalDate date = fdata.getLogDate() != null
				? fdata.getLogDate()
						: LocalDate.now();

		fdata.setLogDate(date);

		String fileName = createFileName(date);

		DailyLogViewDto log = diaryReadService.readByDate(date);

		model.addAttribute("log", log);
		model.addAttribute("fileName",fileName);

		return "home";
	}

	@GetMapping("/home/select")
	public String selectDate(@ModelAttribute("fdata") FData fdata) {

		LocalDate date = fdata.getLogDate() != null
				? fdata.getLogDate()
						: LocalDate.now();

		DailyLogViewDto log = diaryReadService.readByDate(date);

		return log.isExists()
				? "redirect:/home/diary"
						: "redirect:/home/create";
	}

	@GetMapping("/home/todo")
	public String todo(Model model, HttpServletResponse response) {

		disableCache(response);

		String fileName = "todo"; // ★ 日付なし

		TodoViewDto todo = todoReadService.read();

		model.addAttribute("fileName", fileName);
		model.addAttribute("todo", todo);
		model.addAttribute("exists", todo.isExists());

		return "todo_list";
	}

	@GetMapping("/home/todo/edit")
	public String editTodo(Model model, HttpServletResponse response) {

		disableCache(response);

		String fileName = "todo";

		TodoViewDto todo = todoReadService.read();


		model.addAttribute("fileName", fileName);
		model.addAttribute("todoText", todo.getText());

		return "todo_edit";
	}

	@PostMapping("/home/todo/save")
	public String saveTodo(
			@RequestParam("todoText") String todoText,
			RedirectAttributes attrs) {

		//		fileService.overwrite("todo", todoText);

		todoWriteService.save(todoText);

		attrs.addFlashAttribute("message", "TODOを保存しました");
		return "redirect:/home/todo";
	}




	/*
	 * 受けとった文章をファイルに書き込み
	 */

	//	@PostMapping("/home/result")
	//	public String result(Model model, @ModelAttribute("fdata") FData fdata) {
	//
	//		LocalDate date = fdata.getLogDate();
	//		String fileName = createFileName(date);
	//
	//		String postSentence = fdata.getPostText();
	//		String tagType = fdata.getTagType();
	//
	//		if ("with".equals(fdata.getTagType())) {
	//			postSentence = createSentence(postSentence, new Date(), fileName);
	//		} else {
	//			postSentence = createTime(new Date(), fileName);
	//		}
	//
	//		fdata.setPostText(postSentence);
	//
	//		model.addAttribute("postSentence", postSentence);
	//		return "result";
	//	}

	private void disableCache(HttpServletResponse res) {
		res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		res.setHeader("Pragma", "no-cache");
		res.setDateHeader("Expires", 0);
	}

}
