package info.thinkingcloud.jersey.db;

import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.meta.Module;
import info.thinkingcloud.jersey.core.meta.Parameter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.RowSelection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service("db")
@Module(doc = "The service for database access.")
public class DBService {

	private static final Logger logger = LoggerFactory.getLogger(DBService.class);

	@Autowired
	private JdbcTemplate jdbc;

	private Dialect dialect;

	@PostConstruct
	public void init() {
		logger.debug("Initializing the dialect using the configuration of {}", System.getProperty("hibernate.dialect"));
		dialect = Dialect.getDialect(System.getProperties());
	}

	@Function(doc = "Iterate the sql results using page.", parameters = {
	        @Parameter(name = "sql", doc = "The sql query for iterate", type = "string"),
	        @Parameter(name = "fetchSize", type = "int", doc = "The fetch size for every fetch"),
	        @Parameter(name = "args", type = "object", multi = true, doc = "The arguments for the query") })
	public SqlIterator iterate(String sql, int fetchSize, Object... args) {
		return new SqlIterator(sql, args, fetchSize, this);
	}

	public String pagerSql(String sql, int fetchSize, int firstRow, int total) {
		RowSelection rs = new RowSelection();
		rs.setFetchSize(fetchSize);
		rs.setFirstRow(firstRow);
		rs.setMaxRows(total);
		rs.setTimeout(-1);
		return dialect.buildLimitHandler(sql, rs).getProcessedSql();
	}

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

	@SuppressWarnings("rawtypes")
	@Function(doc = "Query for the database ", parameters = {
	        @Parameter(name = "sql", type = "string", doc = "The query sql"),
	        @Parameter(name = "args", multi = true, doc = "The args for sql", optional = true, type = "object") })
	public Object[] query(String sql, Object... args) throws SecurityException, IllegalArgumentException,
	        NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		return ((List) qu(sql, args)).toArray();
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

	public List<Map<String, Object>> queryForListInner(String sql, Object[] args) throws DataAccessException {
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
