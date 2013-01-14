package com.chinarewards.metro.core.security;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.WebSecurityExpressionRoot;


public class AccessWebSecurityExpressionHandler implements SecurityExpressionHandler<FilterInvocation>{

	private ExpressionParser expressionParser;
	
	public AccessWebSecurityExpressionHandler() {
		expressionParser = new AccessExpressionParser();
	}
	
	@Override
	public ExpressionParser getExpressionParser() {
		return expressionParser;
	}
	
	@Override
	public EvaluationContext createEvaluationContext(Authentication authentication, FilterInvocation invocation) {
		StandardEvaluationContext ctx = new StandardEvaluationContext();
        SecurityExpressionRoot root = new WebSecurityExpressionRoot(authentication,invocation);
        ctx.setRootObject(root);
		return ctx;
	}
}
