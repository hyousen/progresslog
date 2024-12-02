package com.hsakuchi.hobby.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FData {
	
	private String htmlText;
	private String sampleText;
	private int num = 1;

}