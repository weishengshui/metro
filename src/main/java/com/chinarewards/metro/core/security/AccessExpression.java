package com.chinarewards.metro.core.security;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class AccessExpression extends LiteralExpression {
	
	private String literalValue;
	
	public AccessExpression(String literalValue) {
		super(literalValue);
		this.literalValue = literalValue;
	}

	@Override
	public String getValue(EvaluationContext context) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		String access = attributes.getRequest().getParameter("access");
		if (access != null) {
			int value = (int) Math.pow(2, Integer.parseInt(literalValue));
			return ((int) Integer.parseInt(access) & value) == value ? "true" : "false";
		}
		return super.getValue(context);
	}
}