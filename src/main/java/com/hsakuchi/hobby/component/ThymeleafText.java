package com.hsakuchi.hobby.component;

import java.util.Date;
import java.util.Locale;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@RequiredArgsConstructor  // ※
@Log4j2                   // ※
public class ThymeleafText {
	private final TemplateEngine templateEngine;

	public void process() {
		final Context ctx = new Context(Locale.getDefault());
		ctx.setVariable("subscriptionDate", new Date());		
		String text = this.templateEngine.process("/mail/sample.txt", ctx);

		log.info(text);
	}
}
