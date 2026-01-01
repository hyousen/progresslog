package com.hsakuchi.hobby.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

		//		String sentence = fileService.read(fileName);

		// ★ ここ！
		//		boolean exists = sentence != null && !sentence.isBlank();
		//		model.addAttribute("exists", exists);
		//
		//		model.addAttribute("fileName", fileName);
		//		model.addAttribute("lines", convertToLines(sentence));

		//		DailyLogParser parser = new DailyLogParser();
		//	    DailyLogDto log = parser.parse(sentence);


		//		ParsedDailyLog log = dailyLogParser.parse(sentence);

		//		model.addAttribute("log", log);

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


	//		LocalDate date = fdata.getLogDate();
	//		String fileName = createFileName(date);

	//		try {
	//			// ★ ここで初めて「なければ作る」
	//			if (!fileService.exists(fileName)) {
	//				fileService.create(fileName);
	//				fileService.write(fileName, LocalDateTime.now()); // 日付行
	//			}
	//			// ★ 旧「result」の加工処理をここに統合する
	//			String postSentence;
	//			if ("with".equals(fdata.getTagType())) {
	//				postSentence = createSentence(sentence, new Date(), fileName);
	//			} else {
	//				postSentence = createTime(new Date(), fileName);
	//			}
	//
	//			fileService.write(fileName, postSentence);
	//			attrs.addFlashAttribute("message", "保存しました");
	//
	//		} catch (Exception e) {
	//
	//			attrs.addFlashAttribute("error", "失敗: " + e.getMessage());
	//
	//		}
	//
	//		return "redirect:/home/diary";

	//	@PostMapping("/home/diary/write")
	//	public String writeDiary(
	//	        Model model,
	//	        @ModelAttribute("fdata") FData fdata) {
	//
	//	    // --- 日付とファイル名 ---
	//	    LocalDate date = fdata.getLogDate();
	//	    String fileName = createFileName(date);
	//	    
	//	 // ★ createSentence/createTime をここで呼ぶ
	//	    String postSentence;
	//
	//	    if ("with".equals(fdata.getTagType())) {
	//	        postSentence = createSentence(inputSentence, new Date(), fileName);
	//	    } else {
	//	        postSentence = createTime(new Date(), fileName);
	//	    }
	//
	//	    try {
	//	        fileService.write(fileName, postSentence);
	//	        attrs.addFlashAttribute("message", "保存しました");
	//	    } catch (Exception e) {
	//	        attrs.addFlashAttribute("error", "失敗: " + e.getMessage());
	//	    }
	//
	//	    return "redirect:/home/diary";

	//	@GetMapping("/home/diary")
	//	public String view(
	//	        @ModelAttribute("fdata") FData fdata,
	//	        Model model,
	//	        HttpServletResponse response) {
	//
	//	    disableCache(response);
	//
	//	    LocalDate date = fdata.getLogDate();
	//	    String fileName = createFileName(date);
	//
	//	    // GETでの書き込み（現仕様に従って残す）
	//	    if (fdata.getPostText() != null && !fdata.getPostText().isBlank()) {
	//	        try {
	//	            fileService.write(fileName, fdata.getPostText());
	//	            fdata.setPostText(null);
	//	        } catch (IOException e) {
	//	            e.printStackTrace();
	//	        }
	//	    }
	//
	//	    // ファイル内容の取得（nullを許容しない）
	//	    String sentence = fileService.read(fileName);
	//	    if (sentence == null) sentence = ""; // ← null安全性を追加
	//
	//	    model.addAttribute("fileName", fileName);
	//	    model.addAttribute("lines", convertToLines(sentence));
	//
	//	    return "log_list";
	//	}

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

		//		model.addAttribute("exists", log.isExists());
		model.addAttribute("log", log);
		model.addAttribute("fileName",fileName);
		//	    String fileName = date.format(DateTimeFormatter.BASIC_ISO_DATE); // yyyyMMdd

		//		String sentence = fileService.read(fileName);

		//		boolean exists = sentence != null && !sentence.isBlank();
		//		fdata.setFileExist(exists);
		//		model.addAttribute("exists", exists);
		//		model.addAttribute("fileName", fileName);
		//		model.addAttribute("lines", convertToLines(sentence));

		return "home";
	}

	@GetMapping("/home/select")
	public String selectDate(@ModelAttribute("fdata") FData fdata) {

		LocalDate date = fdata.getLogDate() != null
				? fdata.getLogDate()
						: LocalDate.now();

		//		String fileName = "log" + date.format(DateTimeFormatter.BASIC_ISO_DATE);

		DailyLogViewDto log = diaryReadService.readByDate(date);

		//		if (log.isExists()) {
		//		    return "redirect:/home/diary";
		//		}
		//		return "redirect:/home/create";

		return log.isExists()
				? "redirect:/home/diary"
						: "redirect:/home/create";
	}

	//		String sentence = fileService.read(fileName);

	//		if (sentence != null && !sentence.isBlank()) {
	//			// 記事が存在 → 記事画面へ
	//			return "redirect:/home/diary";
	//		}

	// 記事が存在しない → 作成画面へ
	//		return "redirect:/home/create";


	@GetMapping("/home/todo")
	public String todo(Model model, HttpServletResponse response) {

		disableCache(response);

		String fileName = "todo"; // ★ 日付なし
		//		String sentence = fileService.read(fileName);

		//		boolean exists = !sentence.isBlank();

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
		//		String sentence = fileService.read(fileName);

		TodoViewDto todo = todoReadService.read();


		model.addAttribute("fileName", fileName);
		model.addAttribute("todoText", todo.getText());

		return "todo_edit";
	}

	//	@PostMapping("/home/todo/save")
	//	public String saveTodo(
	//	        @RequestParam("text") String text,
	//	        RedirectAttributes attrs) {
	//		
	//		// BOM 除去
	//		//text = text.replace("\uFEFF", "");
	//
	//
	//	    try {
	//	        fileService.write("todo", text);
	//	        attrs.addFlashAttribute("message", "TODOを保存しました");
	//	    } catch (Exception e) {
	//	        attrs.addFlashAttribute("error", "保存に失敗しました");
	//	    }
	//
	//	    return "redirect:/home/todo";
	//	}

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

	private String createSentence(String sentence, Date date, String fileName) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd日(E)HH:mm:ss");
		return "・" + sentence + sdf.format(date);
	}

	private String createTime(Date date, String fileName) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return "" + sdf.format(date);

	}

	private List<String> convertToLines(String sentence) {
		if (sentence == null || sentence.isBlank()) {
			return List.of();
		}

		return Arrays.stream(sentence.split("\\R"))
				.map(line -> line.replaceAll(
						"(\\d{1,2}日\\(.\\)\\d{2}:\\d{2}:\\d{2})$",
						"<span class='hidden-date'>$1</span>"))
				.collect(Collectors.toList());
	}

	private void disableCache(HttpServletResponse res) {
		res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		res.setHeader("Pragma", "no-cache");
		res.setDateHeader("Expires", 0);
	}

}
