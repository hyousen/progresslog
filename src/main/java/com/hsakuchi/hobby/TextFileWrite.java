package com.hsakuchi.hobby;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TextFileWrite {

	public void textWrite (String sentence, String fileName) {

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
	
	public void titleWrite() {
		
	}

}