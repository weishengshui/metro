package com.chinarewards.metro.validator.brand;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.chinarewards.metro.domain.brand.Brand;
import com.chinarewards.metro.service.brand.IBrandService;

public class CreateBrandValidator implements Validator {

	private IBrandService brandService;

	public CreateBrandValidator(IBrandService brandService) {
		this.brandService = brandService;
	}
	
	public CreateBrandValidator() {
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(Brand.class);
	}

	@Override
	public void validate(Object target, Errors errors) {

		Brand brand = (Brand) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name",
				"品牌名称不能为空");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "companyName",
				"companyName", "公司名称不能为空");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "companyWebSite",
				"companyWebSite", "公司网址不能为空");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contact", "contact",
				"联系人不能为空");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phoneNumber",
				"phoneNumber", "联系电话不能为空");
		if (!errors.hasErrors()) {
			if (brandService.checkNameExists(brand)) {
				errors.rejectValue("name", "name", "品牌名称已存在");
			}
			if (brandService.checkCompanyNameExists(brand)) {
				errors.rejectValue("companyName", "companyName", "公司名称已存在");
			}
			if (brandService.checkCompanyWebSiteExists(brand)) {
				errors.rejectValue("companyWebSite", "companyWebSite",
						"公司网址已存在");
			}
			if (brandService.checkPhoneNumberExists(brand)) {
				errors.rejectValue("phoneNumber", "phoneNumber", "联系电话已存在");
			}
		}
		// TODO
		// ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description",
		// "description","");
	}

}
