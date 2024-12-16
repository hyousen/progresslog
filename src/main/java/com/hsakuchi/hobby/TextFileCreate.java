package com.hsakuchi.hobby;

import java.io.File;
import java.io.IOException;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Data
@Log4j2
public class TextFileCreate {
	private String message;

	public void textCreate(int number) throws IOException {
		
		String fileName = "log" + number;

		File file = new File("C:/hsakuchi/work/ProgressLog/src/main/resources/templates/" + fileName + ".txt");
		if (file.createNewFile()) {
			this.message = "新しいファイルがつくられました";
		} else {
			this.message = "すでにこのファイルは存在します";
		}

	}

}
