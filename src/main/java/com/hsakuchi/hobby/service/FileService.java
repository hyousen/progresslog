package com.hsakuchi.hobby.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileService {

	@Value("${app.log.dir}")
	private String logDir;

	//	private Path resolvePath(String fileName) throws IOException {
	//		Path dir = Paths.get(logDir);
	//		if (Files.notExists(dir)) {
	//			Files.createDirectories(dir); // mkdirs より安全
	//		}
	//		return dir.resolve(fileName + ".txt");
	//	}

	private Path resolvePathForRead(String fileName) {
		return Paths.get(logDir, fileName + ".txt");
	}

	private Path resolvePathForWrite(String fileName) throws IOException {
		Path dir = Paths.get(logDir);
		if (Files.notExists(dir)) {
			Files.createDirectories(dir);
		}
		return dir.resolve(fileName + ".txt");
	}


	//	public boolean exists(String fileName) {
	//		try {
	//			return Files.exists(resolvePath(fileName));
	//		} catch (IOException e) {
	//			return false;
	//		}
	//	}
	@Deprecated
	public boolean exists(String fileName) {
		Path path = resolvePathForRead(fileName);
		return Files.exists(path);
	}

	//	public void create(String fileName) throws IOException {
	//		Path path = resolvePath(fileName);
	//		if (Files.notExists(path)) {
	//			Files.createFile(path);
	//		}
	//	}
	@Deprecated
	public void create(String fileName) throws IOException {
		Path path = resolvePathForWrite(fileName);
		if (Files.notExists(path)) {
			Files.createFile(path);
		}
	}

	//	public void write(String fileName, String content) throws IOException {
	//		Path path = resolvePath(fileName);
	//
	//		// append = true、UTF-8 固定
	//		Files.write(path,
	//				(content + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
	//				StandardOpenOption.CREATE,
	//				StandardOpenOption.APPEND);
	//	}
	@Deprecated
	public void write(String fileName, String content) throws IOException {
		Path path = resolvePathForWrite(fileName);

		Files.write(
				path,
				(content + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
				StandardOpenOption.CREATE,
				StandardOpenOption.APPEND
				);
	}

	//	public void write(String fileName, LocalDateTime date) throws IOException {
	//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日(E)");
	//		write(fileName, sdf.format(date));
	//	}
	


	// ★ 新規：TODO専用 or 上書き専用
	//	public void overwrite(String fileName, String text) {
	//		try {
	//	        Path path = resolvePath(fileName);
	//
	//	        Files.writeString(
	//	            path,
	//	            text.replace("\uFEFF", ""),   // BOM 除去
	//	            StandardCharsets.UTF_8,
	//	            StandardOpenOption.CREATE,
	//	            StandardOpenOption.TRUNCATE_EXISTING
	//	        );
	//
	//	    } catch (IOException e) {
	//	        throw new RuntimeException(e);
	//	    }
	//	}
	@Deprecated
	public void overwrite(String fileName, String text) {
		try {
			Path path = resolvePathForWrite(fileName);

			Files.writeString(
					path,
					text.replace("\uFEFF", ""),
					StandardCharsets.UTF_8,
					StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING
					);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Deprecated
	public String read(String fileName) throws IOException {
		Path path = resolvePathForRead(fileName);

		if (!Files.exists(path)) {
			return null;   // ← 作らない
		}

		return Files.readString(path, StandardCharsets.UTF_8);

	}

	// ===== diary public API =====

	public boolean existsDiary(LocalDate date) {
	    return exists(diaryFileName(date));
	}

	public void createDiary(LocalDate date) throws IOException {
	    create(diaryFileName(date));
	}

	public void writeDiary(LocalDate date, String content) throws IOException {
	    write(diaryFileName(date), content);
	}

	public String readDiary(LocalDate date) throws IOException {
	    return read(diaryFileName(date));
	}
	
	// ★ 新規追加（todo 用）
	public void overwriteByKey(String key, String text) {
	    overwrite(key, text);
	}
	
	// ===== key-based public API (todo 用) =====
	public String readByKey(String key) throws IOException {
	    return read(key); // 既存の fileName API を内部利用
	}


	
	// ★ private に閉じる
	private String diaryFileName(LocalDate date) {
		return "log" + date.format(DateTimeFormatter.BASIC_ISO_DATE);
	}
	
}
//	public String read(String fileName) {
//		try {
//			Path path = resolvePath(fileName);
//			if (Files.notExists(path)) return null;
//			return Files.readString(path, StandardCharsets.UTF_8);
//		} catch (IOException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}




