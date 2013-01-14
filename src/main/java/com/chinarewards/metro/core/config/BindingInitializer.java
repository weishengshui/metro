package com.chinarewards.metro.core.config;

import java.util.Date;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;


public class BindingInitializer implements WebBindingInitializer {

	@Override
	public void initBinder(WebDataBinder binder, WebRequest request) {
		binder.registerCustomEditor(Date.class, new DateEditor(Date.class));
	}
}
