package com.hsakuchi.hobby;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class TextFileWrite {

//	public void textWrite(String sentence, String fileName) {
//
//		try {
//			File file = new File(
//					"C:\\hsakuchi\\work\\ProgressLog\\src\\main\\resources\\templates\\hobby\\" + fileName + ".txt");
//
//			// ❌ファイルがなければ、絶対に作らない！
//			// ←これが超重要！
//		    if (!file.exists() || !file.isFile() || !file.canWrite()) {
//		        System.out.println("ファイルが存在しないため書き込みしません");
//		        return;
//		    }
//
//			try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
//				writer.write(sentence);
//				writer.newLine();
//				writer.flush(); // ← ここも
//			}
//		} catch (IOException e) {
//			System.out.println(e);
//		}
////	}
	
	public void textWrite(String sentence, String fileName) {
		
		if(sentence == null) {
			sentence ="";
		}
	    try {
	        File file = new File("C:\\hsakuchi\\work\\ProgressLog\\src\\main\\resources\\templates\\hobby\\" + fileName + ".txt");

	        // 🔥ファイルが存在しなければ新規作成してOK（上書き防止じゃない）
	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
	            writer.write(sentence);
	            writer.newLine();
	            writer.flush(); // ← 即座に反映
	        }
	    } catch (IOException e) {
	        System.out.println(e);
	    }
	}

	public void textOverWrite(String sentence, String fileName) {
	    try {
	        File file = new File("C:\\hsakuchi\\work\\ProgressLog\\src\\main\\resources\\templates\\hobby\\" + fileName + ".txt");

	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
	            writer.write(sentence);
	            writer.flush(); // ← 即座に反映
	        }
	    } catch (IOException e) {
	        System.out.println(e);
	    }
	}


	//		try {
	//			File file = new File(
	//					"C:\\hsakuchi\\work\\ProgressLog\\src\\main\\resources\\templates\\hobby\\" + fileName + ".txt");
	//
	//			if (checkBeforeWritefile(file)) {
	//				try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
	//					writer.write(sentence);
	//					writer.newLine();
	//					writer.flush(); // ← 即座に反映！
	//				}
	//			} else {
	//				System.out.println("ファイルに書き込めません");
	//			}
	//		} catch (IOException e) {
	//			System.out.println(e);
	//		}

	public void titleWrite(Date date, String fileName) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日(E)");
			File file = new File(
					"C:\\hsakuchi\\work\\ProgressLog\\src\\main\\resources\\templates\\hobby\\" + fileName + ".txt");
			
			// ←これが超重要！
		    if (!file.exists() || !file.isFile() || !file.canWrite()) {
		        System.out.println("ファイルが存在しないため書き込みしません");
		        return;
		    }

			if (checkBeforeWritefile(file) && checkFileEmpty(file)) {
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
					writer.write(sdf.format(date));
					writer.newLine();
					writer.flush(); // ← ここも
				}
			} else {
				System.out.println("すでにタイトルがあります");
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	private boolean checkBeforeWritefile(File file) {
		return file.exists() && file.isFile() && file.canWrite();
	}

	private boolean checkFileEmpty(File file) {
		return file.length() == 0L;
	}

//	public void textOverWrite(String sentence, String fileName) {
//		try {
//			File file = new File(
//					"C:\\hsakuchi\\work\\ProgressLog\\src\\main\\resources\\templates\\hobby\\" + fileName + ".txt");
//
//			// ❌ファイルがなければ、絶対に作らない！
//			// ←これが超重要！
//		    if (!file.exists() || !file.isFile() || !file.canWrite()) {
//		        System.out.println("ファイルが存在しないため書き込みしません");
//		        return;
//		    }
//			if (checkBeforeWritefile(file)) {
//				try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
//					writer.write(sentence);
//					writer.flush(); // ← 即座に反映
//				}
//			} else {
//				System.out.println("ファイルに書き込めません");
//			}
//		} catch (IOException e) {
//			System.out.println(e);
//		}
//	}
}
