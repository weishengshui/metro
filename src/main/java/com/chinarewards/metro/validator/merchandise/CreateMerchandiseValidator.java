package com.chinarewards.metro.validator.merchandise;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.chinarewards.metro.domain.merchandise.Merchandise;
import com.chinarewards.metro.service.merchandise.IMerchandiseService;

/**
 * 
 * @author weishengshui
 * 
 */
public class CreateMerchandiseValidator implements Validator {

	private IMerchandiseService merchandiseService;

	public CreateMerchandiseValidator() {
	}

	public CreateMerchandiseValidator(IMerchandiseService merchandiseService) {
		this.merchandiseService = merchandiseService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(Merchandise.class);
	}

	@Override
	public void validate(Object target, Errors errors) {

		Merchandise merchandise = (Merchandise) target;
		if(null == merchandise.getBrand().getId()){
			merchandise.setBrand(null);
		}
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name",
				"请输入商品名称");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "code",
				"请输入商品编号");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "model", "model",
				"请输入商品型号");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "purchasePrice",
				"purchasePrice", "请输入采购价");
		// ValidationUtils.rejectIfEmptyOrWhitespace(errors, "supplierName",
		// "请输入供应商名称");
		// TODO ValidationUtils.rejectIfEmptyOrWhitespace(errors, "", "", "");

	}

}
