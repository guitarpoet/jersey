package com.thinkingcloud.tools.js.runner.db;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.core.meta.Function;
import com.thinkingcloud.tools.js.runner.core.meta.Module;
import com.thinkingcloud.tools.js.runner.core.meta.Parameter;

@Service("db")
@Module(doc = "The service for database access.")
public class DBService {
	@Autowired
	private JdbcTemplate jdbc;

	/**
	 * @param sql
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#batchUpdate(java.lang.String[])
	 */
	public int[] batchUpdate(String[] sql) throws DataAccessException {
		return jdbc.batchUpdate(sql);
	}

	/**
	 * @param sql
	 * @param args
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForMap(java.lang.String,
	 *      java.lang.Object[])
	 */
	@Function(doc = "Query for a map", parameters = { @Parameter(name = "sql", type = "string", doc = "The sql"),
	        @Parameter(name = "args", multi = true, type = "object", doc = "The args for sql", optional = true) }, returns = "The result map.")
	public Map<String, Object> queryForMap(String sql, Object... args) throws DataAccessException {
		return jdbc.queryForMap(sql, args);
	}

	public Object qu(String sql, Object[] args) throws SecurityException, NoSuchMethodException,
	        IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method method = JdbcTemplate.class.getMethod("queryForList", new Class<?>[] { String.class, Object[].class });
		return method.invoke(jdbc, new Object[] { sql, args });
	}

	@Function(doc = "Query for the database ", parameters = {
	        @Parameter(name = "sql", type = "string", doc = "The query sql"),
	        @Parameter(name = "args", multi = true, doc = "The args for sql", optional = true, type = "object") })
	public Object query(String sql, Object... args) throws SecurityException, IllegalArgumentException,
	        NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		return qu(sql, args);
	}

	/**
	 * @param sql
	 * @param args
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForLong(java.lang.String,
	 *      java.lang.Object[])
	 */
	@Function(doc = "Query for a long value", parameters = {
	        @Parameter(name = "sql", type = "string", doc = "The sql"),
	        @Parameter(name = "args", multi = true, type = "object", doc = "The args for sql", optional = true) }, returns = "The long result.")
	public long queryForLong(String sql, Object... args) throws DataAccessException {
		return jdbc.queryForLong(sql, args);
	}

	/**
	 * @param sql
	 * @param args
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForInt(java.lang.String,
	 *      java.lang.Object[])
	 */
	@Function(doc = "Query for a int value", parameters = { @Parameter(name = "sql", type = "string", doc = "The sql"),
	        @Parameter(name = "args", multi = true, type = "object", doc = "The args for sql", optional = true) }, returns = "The int result.")
	public int queryForInt(String sql, Object... args) throws DataAccessException {
		return jdbc.queryForInt(sql, args);
	}

	/**
	 * @param sql
	 * @param args
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForList(java.lang.String,
	 *      java.lang.Object[])
	 */
	@Function(doc = "Query for a map list", parameters = { @Parameter(name = "sql", type = "string", doc = "The sql"),
	        @Parameter(name = "args", multi = true, type = "object", doc = "The args for sql", optional = true) }, returns = "The map list result.")
	public List<Map<String, Object>> queryForList(String sql, Object... args) throws DataAccessException {
		return jdbc.queryForList(sql, args);
	}

	@Function(doc = "Update the database.", parameters = { @Parameter(name = "sql", type = "string", doc = "The sql"),
	        @Parameter(name = "args", multi = true, type = "list", doc = "The args for sql", optional = true) })
	public int up(String sql, Object[] args) throws SecurityException, NoSuchMethodException, IllegalArgumentException,
	        IllegalAccessException, InvocationTargetException {
		Method m = JdbcTemplate.class.getMethod("update", String.class, Object[].class);
		return (Integer) m.invoke(jdbc, sql, args);
	}

	/**
	 * @param sql
	 * @param args
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#update(java.lang.String,
	 *      java.lang.Object[])
	 */
	@Function(doc = "Update the database.", parameters = { @Parameter(name = "sql", type = "string", doc = "The sql"),
	        @Parameter(name = "args", multi = true, type = "object", doc = "The args for sql", optional = true) })
	public int update(String sql, Object... args) throws DataAccessException {
		return jdbc.update(sql, args);
	}
}
