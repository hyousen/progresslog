package com.hsakuchi.hobby.component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.hsakuchi.hobby.model.FData;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@RequiredArgsConstructor // ※
@Log4j2 // ※
public class ThymeleafText {
	private final TemplateEngine templateEngine;

	public void process() {
		final Context ctx = new Context(Locale.getDefault());
		ctx.setVariable("subscriptionDate", new Date());
		ctx.setVariable("display", true);
		String text = this.templateEngine.process("/mail/sample.txt", ctx);

		log.info(text);
	}

	public void process(FData fdata) {
		final Context ctx = new Context(Locale.getDefault());
		LocalDateTime date = LocalDateTime.now();
		String text = this.templateEngine.process("/log20200806.txt", ctx);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH/mm/ss");
		String sentence = text + "　　　" + dtf.format(date);

		fdata.setHtmlText(sentence);
	}

}
