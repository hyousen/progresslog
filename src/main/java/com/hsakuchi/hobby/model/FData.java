package com.hsakuchi.hobby.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FData {
	
	private String htmlText;
	private String postText;
	private int dateNumber;
	private String fileName;
	private String tagType;
	private boolean fileExist;
	private LocalDate logDate;
	
}
