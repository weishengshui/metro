package com.chinarewards.metro.core.timer;

import java.util.List;

import com.chinarewards.metro.core.common.JDBCDaoSupport;
import com.chinarewards.metro.domain.account.Business;
import com.chinarewards.metro.domain.account.TransactionQueue;
import com.chinarewards.metro.domain.account.Unit;
import com.chinarewards.metro.service.account.IAccountService;
/**
 * 定时积分生效
 * @author daocao
 *
 */
public class DepositJob  {
	
	private IAccountService accountService;
	
	private JDBCDaoSupport jdbcDaoSupport;

	public void doJob() {
		try{
			String sql = "SELECT tq.* FROM TransactionQueue tq LEFT JOIN `Transaction` t ON tq.tx_txId = t.txId" +
						 " WHERE DATEDIFF(now(),t.transactionDate) >= 7 AND t.busines = ?";
			String sqlUnit = "SELECT * FROM Unit where unitCode = ?";
			List<TransactionQueue> list = jdbcDaoSupport.findTsBySQL(TransactionQueue.class, sql, Business.POS_SALES);
			for(TransactionQueue tq : list){
				if(tq.getType() == 0){ //存
					Unit unit = jdbcDaoSupport.findTBySQL(Unit.class, sqlUnit, tq.getUnitCode());
					accountService.deposit("0", tq.getAccount(), unit, tq.getUnits().doubleValue(), tq.getTx());
				}else if(tq.getType() == 1){ //取
					Unit unit = jdbcDaoSupport.findTBySQL(Unit.class, sqlUnit, tq.getUnitCode());
					accountService.withdrawal("0", tq.getAccount(), unit, tq.getUnits().doubleValue(), tq.getTx());
				}
			}
		}catch(Exception e){
			throw new RuntimeException("定时积分生效报错 ：" + e);
		}
	}

	
	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}

	public void setJdbcDaoSupport(JDBCDaoSupport jdbcDaoSupport) {
		this.jdbcDaoSupport = jdbcDaoSupport;
	}
	
}	
