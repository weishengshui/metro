package com.chinarewards.metro.service.merchandise;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.model.merchandise.MerchandiseCriteria;
import com.chinarewards.metro.model.merchandise.MerchandiseVo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@TransactionConfiguration
@Transactional
public class TestMerchandiseService {
	
	@Autowired
	private IMerchandiseService merchandiseService;
	
	@Test
	public void testSearchMercCatagorys(){
		MerchandiseCriteria merchandiseCriteria = new MerchandiseCriteria();
		Page page = new Page();
		page.setPage(1);
		page.setRows(10);
		merchandiseCriteria.setPaginationDetail(page);
		List<MerchandiseVo> list = merchandiseService.searchMerchandises(merchandiseCriteria);
		System.out.println("list size is "+list.size());
	}
}
