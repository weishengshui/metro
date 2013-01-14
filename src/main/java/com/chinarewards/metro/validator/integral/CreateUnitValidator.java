package com.chinarewards.metro.validator.integral;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.chinarewards.metro.domain.account.Unit;

public class CreateUnitValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(Unit.class);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "displayName", "displayName", "请输入积分名称");
		
		Unit unit = (Unit)target;
		if(null == unit.getAvailable() || unit.getAvailable() <= 0){
			errors.rejectValue("available", "available", "请输入有效的积分有效期");
		}
		if(unit.getPrice() <= 0){
			errors.rejectValue("price", "price", "请输入有效的积分价值");
		}
	}

}
