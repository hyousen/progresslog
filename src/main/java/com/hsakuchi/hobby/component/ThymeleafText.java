package com.hsakuchi.hobby.component;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@RequiredArgsConstructor // ※
@Log4j2 // ※
public class ThymeleafText {
	private final TemplateEngine templateEngine;

//	public void process() {
//		final Context ctx = new Context(Locale.getDefault());
//		ctx.setVariable("subscriptionDate", new Date());
//		ctx.setVariable("display", true);
//		String text = this.templateEngine.process("/mail/sample.txt", ctx);
//
//		log.info(text);
//	}

//	public String process(int number) {
//		final Context ctx = new Context(Locale.getDefault());
//		String text = this.templateEngine.process("/hobby/log" + number + ".txt", ctx);
//		//System.out.println(text);
//		return text;
//	}
//
//	public String process(String fileName) {
//		final Context ctx = new Context(Locale.getDefault());
//		String text = this.templateEngine.process("/hobby/" + fileName + ".txt", ctx);
//		//System.out.println(text);
//		return text;
//
//	}
	
//	public String readFile(String fileName) {
//	    try {
//	        String path = "C:/hsakuchi/work/ProgressLog/src/main/resources/templates/hobby/" + fileName + ".txt";
//	        return java.nio.file.Files.readString(java.nio.file.Path.of(path));
//	    } catch (IOException ex) {
//	        log.error("Failed to read: {}", fileName, ex);
//	        return "";
//	    }
//	}

	
	public String readFile(String fileName) {
	    try (var resource = getClass().getResourceAsStream("/templates/hobby/" + fileName + ".txt")) {
	        if (resource == null) {
	            log.warn("File not found: " + fileName);
	            return "";
	        }
	        byte[] bytes = resource.readAllBytes();
	        return new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
	    } catch (IOException ex) {
	        log.error("File read failed: " + fileName, ex);
	        return "";
	    }
	}


}
