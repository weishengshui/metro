package com.chinarewards.metro.core.common;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.chinarewards.metro.core.config.BeanRowMapper;
import com.chinarewards.metro.core.config.ListRowMapper;


public class JDBCDaoSupport extends JdbcDaoSupport{

	public int findCount(String sql, Object... args) {
		return getJdbcTemplate().queryForInt(sql, args);
	}
	
	
	public <I> I findForObject(Class<I> clazz, String sql, Object... args) {
		return getJdbcTemplate().queryForObject(sql, clazz, args);
	}
	
	
	public Map<String, Object> findMapBySQL(String sql, Object... args) {
		return getJdbcTemplate().queryForMap(sql, args);
	}

	
	public <T> T findTBySQL(Class<T> c, String sql, Object... args) {
		List<T> list = findTsBySQL(c, sql, args);
		if (list.size() != 0)
			return list.get(0);
		return null;
	}

	
	public List<List<Object>> findListBySQL(String sql, Object... args) {
		return findTsBySQL(new ListRowMapper(), sql, args);
	}

	
	public <T> List<T> findTsBySQL(RowMapper<T> rowMapper, String sql, Object... args) {
		return getJdbcTemplate().query(sql, args, rowMapper);
	}
	
	
	public List<Map<String, Object>> findMapsBySQL(String sql, Object...args) {
		return getJdbcTemplate().queryForList(sql, args);
	}

	
	public <T> List<T> findTsPageBySQL(RowMapper<T> rowMapper, Page page, String regex, String sql, Object... args) {
		StringBuffer buf = new StringBuffer();
		buf.append("select count(*) from (").append(sql).append(")");
		page.setTotalRows(getJdbcTemplate().queryForInt(buf.toString(), args));
		buf.setLength(0);
		buf.append("select * from (").append(sql).append(") ").append(regex);
		Object[] objs = new Object[args.length + 2];
		for (int i = 0; i < args.length; i++)
			objs[i] = args[i];
		objs[objs.length - 2] = page.getRows() * (page.getPage() - 1);
		objs[objs.length - 1] = page.getRows();
		return getJdbcTemplate().query(buf.toString(), objs, rowMapper);
	}

	
	public <T> List<T> findTsBySQL(Class<T> c, String sql, Object... args) {
		BeanRowMapper<T> rowMapper = new BeanRowMapper<T>(c);
		return findTsBySQL(rowMapper, sql, args);
	}
	
	
	public <T> List<T> findTsPageBySQL(Class<T> c, Page page, String regex, String sql, Object... args) {
		BeanRowMapper<T> rowMapper = new BeanRowMapper<T>(c);
		return findTsPageBySQL(rowMapper, page, regex, sql, args);
	}
	
	
	public Integer execute(String sql, final Object... args) {
		return getJdbcTemplate().execute(sql, new PreparedStatementCallback<Integer>() {
			public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				for (int i = 0; i < args.length; i++)
					ps.setObject(i + 1, args[i]);
				return ps.executeUpdate();
			}			
		});
	}
}
