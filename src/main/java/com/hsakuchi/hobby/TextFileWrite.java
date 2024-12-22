package com.hsakuchi.hobby;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TextFileWrite {

	public void textWrite(String sentence, String fileName) {

		try {
			File file = new File("C:\\hsakuchi\\work\\ProgressLog\\src\\main\\resources\\templates\\" + fileName + ".txt");
			
			if (checkBeforeWritefile(file)) {
				FileWriter filewriter = new FileWriter(file, true);

				filewriter.write(sentence + "\n");

				filewriter.close();
			} else {
				System.out.println("ファイルに書き込めません");
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	private boolean checkBeforeWritefile(File file) {

		if (file.exists()) {
			if (file.isFile() && file.canWrite()) {
				return true;
			}
		}
		return false;
	}

	public void titleWrite(Date date, String fileName) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日(E)");
			File file = new File("C:\\hsakuchi\\work\\ProgressLog\\src\\main\\resources\\templates\\" + fileName + ".txt");
			
			if ((checkBeforeWritefile(file)) && (checkFileEmpty(file))) {
				FileWriter filewriter = new FileWriter(file, true);

				filewriter.write(sdf.format(date) + "\n");

				filewriter.close();
			} else {
				System.out.println("すでにタイトルがあります");
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	private boolean checkFileEmpty(File file) {

		if (file.length() == 0L) {
            return true;
        }else {
        	return false;
        }
		
	}

}