package com.chinarewards.metro.core.security;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.ParserContext;

public class AccessExpressionParser implements ExpressionParser{

	ParserContext NON_TEMPLATE_PARSER_CONTEXT = new ParserContext() {
		@Override
		public boolean isTemplate() {
			return false;
		}
		@Override
		public String getExpressionSuffix() {
			return null;
		}
		@Override
		public String getExpressionPrefix() {
			return null;
		}
	};
	
	@Override
	public Expression parseExpression(String expressionString)throws ParseException {
		return parseExpression(expressionString,NON_TEMPLATE_PARSER_CONTEXT);
	}
	
	@Override
	public Expression parseExpression(String expressionString,ParserContext context) throws ParseException {
		if(context == null){
			context = NON_TEMPLATE_PARSER_CONTEXT;
		}
		return new AccessExpression(expressionString);
	}
	
}
