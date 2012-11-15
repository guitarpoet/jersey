package com.thinkingcloud.tools.js.runner.db;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

@Service("db")
public class DBService {
	@Autowired
	private JdbcTemplate jdbc;

	/**
	 * @param dbName
	 * @see org.springframework.jdbc.support.JdbcAccessor#setDatabaseProductName(java.lang.String)
	 */
	public void setDatabaseProductName(String dbName) {
		jdbc.setDatabaseProductName(dbName);
	}

	/**
	 * @param exceptionTranslator
	 * @see org.springframework.jdbc.support.JdbcAccessor#setExceptionTranslator(org.springframework.jdbc.support.SQLExceptionTranslator)
	 */
	public void setExceptionTranslator(SQLExceptionTranslator exceptionTranslator) {
		jdbc.setExceptionTranslator(exceptionTranslator);
	}

	/**
	 * @return
	 * @see org.springframework.jdbc.support.JdbcAccessor#getExceptionTranslator()
	 */
	public SQLExceptionTranslator getExceptionTranslator() {
		return jdbc.getExceptionTranslator();
	}

	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return jdbc.equals(obj);
	}

	/**
	 * @param lazyInit
	 * @see org.springframework.jdbc.support.JdbcAccessor#setLazyInit(boolean)
	 */
	public void setLazyInit(boolean lazyInit) {
		jdbc.setLazyInit(lazyInit);
	}

	/**
	 * @return
	 * @see org.springframework.jdbc.support.JdbcAccessor#isLazyInit()
	 */
	public boolean isLazyInit() {
		return jdbc.isLazyInit();
	}

	/**
	 * @param extractor
	 * @see org.springframework.jdbc.core.JdbcTemplate#setNativeJdbcExtractor(org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor)
	 */
	public void setNativeJdbcExtractor(NativeJdbcExtractor extractor) {
		jdbc.setNativeJdbcExtractor(extractor);
	}

	/**
	 * @return
	 * @see org.springframework.jdbc.core.JdbcTemplate#getNativeJdbcExtractor()
	 */
	public NativeJdbcExtractor getNativeJdbcExtractor() {
		return jdbc.getNativeJdbcExtractor();
	}

	/**
	 * @param ignoreWarnings
	 * @see org.springframework.jdbc.core.JdbcTemplate#setIgnoreWarnings(boolean)
	 */
	public void setIgnoreWarnings(boolean ignoreWarnings) {
		jdbc.setIgnoreWarnings(ignoreWarnings);
	}

	/**
	 * @return
	 * @see org.springframework.jdbc.core.JdbcTemplate#isIgnoreWarnings()
	 */
	public boolean isIgnoreWarnings() {
		return jdbc.isIgnoreWarnings();
	}

	/**
	 * @param fetchSize
	 * @see org.springframework.jdbc.core.JdbcTemplate#setFetchSize(int)
	 */
	public void setFetchSize(int fetchSize) {
		jdbc.setFetchSize(fetchSize);
	}

	/**
	 * @return
	 * @see org.springframework.jdbc.core.JdbcTemplate#getFetchSize()
	 */
	public int getFetchSize() {
		return jdbc.getFetchSize();
	}

	/**
	 * @param maxRows
	 * @see org.springframework.jdbc.core.JdbcTemplate#setMaxRows(int)
	 */
	public void setMaxRows(int maxRows) {
		jdbc.setMaxRows(maxRows);
	}

	/**
	 * @return
	 * @see org.springframework.jdbc.core.JdbcTemplate#getMaxRows()
	 */
	public int getMaxRows() {
		return jdbc.getMaxRows();
	}

	/**
	 * @param queryTimeout
	 * @see org.springframework.jdbc.core.JdbcTemplate#setQueryTimeout(int)
	 */
	public void setQueryTimeout(int queryTimeout) {
		jdbc.setQueryTimeout(queryTimeout);
	}

	/**
	 * @return
	 * @see org.springframework.jdbc.core.JdbcTemplate#getQueryTimeout()
	 */
	public int getQueryTimeout() {
		return jdbc.getQueryTimeout();
	}

	/**
	 * @param skipResultsProcessing
	 * @see org.springframework.jdbc.core.JdbcTemplate#setSkipResultsProcessing(boolean)
	 */
	public void setSkipResultsProcessing(boolean skipResultsProcessing) {
		jdbc.setSkipResultsProcessing(skipResultsProcessing);
	}

	/**
	 * @return
	 * @see org.springframework.jdbc.core.JdbcTemplate#isSkipResultsProcessing()
	 */
	public boolean isSkipResultsProcessing() {
		return jdbc.isSkipResultsProcessing();
	}

	/**
	 * @param skipUndeclaredResults
	 * @see org.springframework.jdbc.core.JdbcTemplate#setSkipUndeclaredResults(boolean)
	 */
	public void setSkipUndeclaredResults(boolean skipUndeclaredResults) {
		jdbc.setSkipUndeclaredResults(skipUndeclaredResults);
	}

	/**
	 * @return
	 * @see org.springframework.jdbc.core.JdbcTemplate#isSkipUndeclaredResults()
	 */
	public boolean isSkipUndeclaredResults() {
		return jdbc.isSkipUndeclaredResults();
	}

	/**
	 * @param resultsMapCaseInsensitive
	 * @see org.springframework.jdbc.core.JdbcTemplate#setResultsMapCaseInsensitive(boolean)
	 */
	public void setResultsMapCaseInsensitive(boolean resultsMapCaseInsensitive) {
		jdbc.setResultsMapCaseInsensitive(resultsMapCaseInsensitive);
	}

	/**
	 * @return
	 * @see org.springframework.jdbc.core.JdbcTemplate#isResultsMapCaseInsensitive()
	 */
	public boolean isResultsMapCaseInsensitive() {
		return jdbc.isResultsMapCaseInsensitive();
	}

	/**
	 * @param action
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#execute(org.springframework.jdbc.core.ConnectionCallback)
	 */
	public <T> T execute(ConnectionCallback<T> action) throws DataAccessException {
		return jdbc.execute(action);
	}

	/**
	 * @param action
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#execute(org.springframework.jdbc.core.StatementCallback)
	 */
	public <T> T execute(StatementCallback<T> action) throws DataAccessException {
		return jdbc.execute(action);
	}

	/**
	 * @param sql
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#execute(java.lang.String)
	 */
	public void execute(String sql) throws DataAccessException {
		jdbc.execute(sql);
	}

	/**
	 * @param sql
	 * @param rse
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String,
	 *      org.springframework.jdbc.core.ResultSetExtractor)
	 */
	public <T> T query(String sql, ResultSetExtractor<T> rse) throws DataAccessException {
		return jdbc.query(sql, rse);
	}

	/**
	 * @param sql
	 * @param rch
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String,
	 *      org.springframework.jdbc.core.RowCallbackHandler)
	 */
	public void query(String sql, RowCallbackHandler rch) throws DataAccessException {
		jdbc.query(sql, rch);
	}

	/**
	 * @param sql
	 * @param rowMapper
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String,
	 *      org.springframework.jdbc.core.RowMapper)
	 */
	public <T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException {
		return jdbc.query(sql, rowMapper);
	}

	/**
	 * @param sql
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForMap(java.lang.String)
	 */
	public Map<String, Object> queryForMap(String sql) throws DataAccessException {
		return jdbc.queryForMap(sql);
	}

	/**
	 * @param sql
	 * @param rowMapper
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForObject(java.lang.String,
	 *      org.springframework.jdbc.core.RowMapper)
	 */
	public <T> T queryForObject(String sql, RowMapper<T> rowMapper) throws DataAccessException {
		return jdbc.queryForObject(sql, rowMapper);
	}

	/**
	 * @param sql
	 * @param requiredType
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForObject(java.lang.String,
	 *      java.lang.Class)
	 */
	public <T> T queryForObject(String sql, Class<T> requiredType) throws DataAccessException {
		return jdbc.queryForObject(sql, requiredType);
	}

	/**
	 * @param sql
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForLong(java.lang.String)
	 */
	public long queryForLong(String sql) throws DataAccessException {
		return jdbc.queryForLong(sql);
	}

	/**
	 * @param sql
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForInt(java.lang.String)
	 */
	public int queryForInt(String sql) throws DataAccessException {
		return jdbc.queryForInt(sql);
	}

	/**
	 * @param sql
	 * @param elementType
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForList(java.lang.String,
	 *      java.lang.Class)
	 */
	public <T> List<T> queryForList(String sql, Class<T> elementType) throws DataAccessException {
		return jdbc.queryForList(sql, elementType);
	}

	/**
	 * @param sql
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForList(java.lang.String)
	 */
	public List<Map<String, Object>> queryForList(String sql) throws DataAccessException {
		return jdbc.queryForList(sql);
	}

	/**
	 * @param sql
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForRowSet(java.lang.String)
	 */
	public SqlRowSet queryForRowSet(String sql) throws DataAccessException {
		return jdbc.queryForRowSet(sql);
	}

	/**
	 * @param sql
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#update(java.lang.String)
	 */
	public int update(String sql) throws DataAccessException {
		return jdbc.update(sql);
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
	 * @param psc
	 * @param action
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#execute(org.springframework.jdbc.core.PreparedStatementCreator,
	 *      org.springframework.jdbc.core.PreparedStatementCallback)
	 */
	public <T> T execute(PreparedStatementCreator psc, PreparedStatementCallback<T> action) throws DataAccessException {
		return jdbc.execute(psc, action);
	}

	/**
	 * @param sql
	 * @param action
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#execute(java.lang.String,
	 *      org.springframework.jdbc.core.PreparedStatementCallback)
	 */
	public <T> T execute(String sql, PreparedStatementCallback<T> action) throws DataAccessException {
		return jdbc.execute(sql, action);
	}

	/**
	 * @param psc
	 * @param pss
	 * @param rse
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(org.springframework.jdbc.core.PreparedStatementCreator,
	 *      org.springframework.jdbc.core.PreparedStatementSetter,
	 *      org.springframework.jdbc.core.ResultSetExtractor)
	 */
	public <T> T query(PreparedStatementCreator psc, PreparedStatementSetter pss, ResultSetExtractor<T> rse)
	        throws DataAccessException {
		return jdbc.query(psc, pss, rse);
	}

	/**
	 * @param psc
	 * @param rse
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(org.springframework.jdbc.core.PreparedStatementCreator,
	 *      org.springframework.jdbc.core.ResultSetExtractor)
	 */
	public <T> T query(PreparedStatementCreator psc, ResultSetExtractor<T> rse) throws DataAccessException {
		return jdbc.query(psc, rse);
	}

	/**
	 * @param sql
	 * @param pss
	 * @param rse
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String,
	 *      org.springframework.jdbc.core.PreparedStatementSetter,
	 *      org.springframework.jdbc.core.ResultSetExtractor)
	 */
	public <T> T query(String sql, PreparedStatementSetter pss, ResultSetExtractor<T> rse) throws DataAccessException {
		return jdbc.query(sql, pss, rse);
	}

	/**
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @param rse
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String,
	 *      java.lang.Object[], int[],
	 *      org.springframework.jdbc.core.ResultSetExtractor)
	 */
	public <T> T query(String sql, Object[] args, int[] argTypes, ResultSetExtractor<T> rse) throws DataAccessException {
		return jdbc.query(sql, args, argTypes, rse);
	}

	/**
	 * @param sql
	 * @param args
	 * @param rse
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String,
	 *      java.lang.Object[],
	 *      org.springframework.jdbc.core.ResultSetExtractor)
	 */
	public <T> T query(String sql, Object[] args, ResultSetExtractor<T> rse) throws DataAccessException {
		return jdbc.query(sql, args, rse);
	}

	/**
	 * @param sql
	 * @param rse
	 * @param args
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String,
	 *      org.springframework.jdbc.core.ResultSetExtractor,
	 *      java.lang.Object[])
	 */
	public <T> T query(String sql, ResultSetExtractor<T> rse, Object... args) throws DataAccessException {
		return jdbc.query(sql, rse, args);
	}

	/**
	 * @param psc
	 * @param rch
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(org.springframework.jdbc.core.PreparedStatementCreator,
	 *      org.springframework.jdbc.core.RowCallbackHandler)
	 */
	public void query(PreparedStatementCreator psc, RowCallbackHandler rch) throws DataAccessException {
		jdbc.query(psc, rch);
	}

	/**
	 * @param sql
	 * @param pss
	 * @param rch
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String,
	 *      org.springframework.jdbc.core.PreparedStatementSetter,
	 *      org.springframework.jdbc.core.RowCallbackHandler)
	 */
	public void query(String sql, PreparedStatementSetter pss, RowCallbackHandler rch) throws DataAccessException {
		jdbc.query(sql, pss, rch);
	}

	/**
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @param rch
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String,
	 *      java.lang.Object[], int[],
	 *      org.springframework.jdbc.core.RowCallbackHandler)
	 */
	public void query(String sql, Object[] args, int[] argTypes, RowCallbackHandler rch) throws DataAccessException {
		jdbc.query(sql, args, argTypes, rch);
	}

	/**
	 * @param sql
	 * @param args
	 * @param rch
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String,
	 *      java.lang.Object[],
	 *      org.springframework.jdbc.core.RowCallbackHandler)
	 */
	public void query(String sql, Object[] args, RowCallbackHandler rch) throws DataAccessException {
		jdbc.query(sql, args, rch);
	}

	/**
	 * @param sql
	 * @param rch
	 * @param args
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String,
	 *      org.springframework.jdbc.core.RowCallbackHandler,
	 *      java.lang.Object[])
	 */
	public void query(String sql, RowCallbackHandler rch, Object... args) throws DataAccessException {
		jdbc.query(sql, rch, args);
	}

	/**
	 * @param psc
	 * @param rowMapper
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(org.springframework.jdbc.core.PreparedStatementCreator,
	 *      org.springframework.jdbc.core.RowMapper)
	 */
	public <T> List<T> query(PreparedStatementCreator psc, RowMapper<T> rowMapper) throws DataAccessException {
		return jdbc.query(psc, rowMapper);
	}

	/**
	 * @param sql
	 * @param pss
	 * @param rowMapper
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String,
	 *      org.springframework.jdbc.core.PreparedStatementSetter,
	 *      org.springframework.jdbc.core.RowMapper)
	 */
	public <T> List<T> query(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper)
	        throws DataAccessException {
		return jdbc.query(sql, pss, rowMapper);
	}

	/**
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @param rowMapper
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String,
	 *      java.lang.Object[], int[], org.springframework.jdbc.core.RowMapper)
	 */
	public <T> List<T> query(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper)
	        throws DataAccessException {
		return jdbc.query(sql, args, argTypes, rowMapper);
	}

	/**
	 * @param sql
	 * @param args
	 * @param rowMapper
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String,
	 *      java.lang.Object[], org.springframework.jdbc.core.RowMapper)
	 */
	public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
		return jdbc.query(sql, args, rowMapper);
	}

	/**
	 * @param sql
	 * @param rowMapper
	 * @param args
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String,
	 *      org.springframework.jdbc.core.RowMapper, java.lang.Object[])
	 */
	public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException {
		return jdbc.query(sql, rowMapper, args);
	}

	/**
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @param rowMapper
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForObject(java.lang.String,
	 *      java.lang.Object[], int[], org.springframework.jdbc.core.RowMapper)
	 */
	public <T> T queryForObject(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper)
	        throws DataAccessException {
		return jdbc.queryForObject(sql, args, argTypes, rowMapper);
	}

	/**
	 * @param sql
	 * @param args
	 * @param rowMapper
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForObject(java.lang.String,
	 *      java.lang.Object[], org.springframework.jdbc.core.RowMapper)
	 */
	public <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
		return jdbc.queryForObject(sql, args, rowMapper);
	}

	/**
	 * @param sql
	 * @param rowMapper
	 * @param args
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForObject(java.lang.String,
	 *      org.springframework.jdbc.core.RowMapper, java.lang.Object[])
	 */
	public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException {
		return jdbc.queryForObject(sql, rowMapper, args);
	}

	/**
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @param requiredType
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForObject(java.lang.String,
	 *      java.lang.Object[], int[], java.lang.Class)
	 */
	public <T> T queryForObject(String sql, Object[] args, int[] argTypes, Class<T> requiredType)
	        throws DataAccessException {
		return jdbc.queryForObject(sql, args, argTypes, requiredType);
	}

	/**
	 * @param sql
	 * @param args
	 * @param requiredType
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForObject(java.lang.String,
	 *      java.lang.Object[], java.lang.Class)
	 */
	public <T> T queryForObject(String sql, Object[] args, Class<T> requiredType) throws DataAccessException {
		return jdbc.queryForObject(sql, args, requiredType);
	}

	/**
	 * @param sql
	 * @param requiredType
	 * @param args
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForObject(java.lang.String,
	 *      java.lang.Class, java.lang.Object[])
	 */
	public <T> T queryForObject(String sql, Class<T> requiredType, Object... args) throws DataAccessException {
		return jdbc.queryForObject(sql, requiredType, args);
	}

	/**
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForMap(java.lang.String,
	 *      java.lang.Object[], int[])
	 */
	public Map<String, Object> queryForMap(String sql, Object[] args, int[] argTypes) throws DataAccessException {
		return jdbc.queryForMap(sql, args, argTypes);
	}

	/**
	 * @param sql
	 * @param args
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForMap(java.lang.String,
	 *      java.lang.Object[])
	 */
	public Map<String, Object> queryForMap(String sql, Object... args) throws DataAccessException {
		return jdbc.queryForMap(sql, args);
	}

	/**
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForLong(java.lang.String,
	 *      java.lang.Object[], int[])
	 */
	public long queryForLong(String sql, Object[] args, int[] argTypes) throws DataAccessException {
		return jdbc.queryForLong(sql, args, argTypes);
	}

	/**
	 * @param sql
	 * @param args
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForLong(java.lang.String,
	 *      java.lang.Object[])
	 */
	public long queryForLong(String sql, Object... args) throws DataAccessException {
		return jdbc.queryForLong(sql, args);
	}

	/**
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForInt(java.lang.String,
	 *      java.lang.Object[], int[])
	 */
	public int queryForInt(String sql, Object[] args, int[] argTypes) throws DataAccessException {
		return jdbc.queryForInt(sql, args, argTypes);
	}

	/**
	 * @param sql
	 * @param args
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForInt(java.lang.String,
	 *      java.lang.Object[])
	 */
	public int queryForInt(String sql, Object... args) throws DataAccessException {
		return jdbc.queryForInt(sql, args);
	}

	/**
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @param elementType
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForList(java.lang.String,
	 *      java.lang.Object[], int[], java.lang.Class)
	 */
	public <T> List<T> queryForList(String sql, Object[] args, int[] argTypes, Class<T> elementType)
	        throws DataAccessException {
		return jdbc.queryForList(sql, args, argTypes, elementType);
	}

	/**
	 * @param sql
	 * @param args
	 * @param elementType
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForList(java.lang.String,
	 *      java.lang.Object[], java.lang.Class)
	 */
	public <T> List<T> queryForList(String sql, Object[] args, Class<T> elementType) throws DataAccessException {
		return jdbc.queryForList(sql, args, elementType);
	}

	/**
	 * @param sql
	 * @param elementType
	 * @param args
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForList(java.lang.String,
	 *      java.lang.Class, java.lang.Object[])
	 */
	public <T> List<T> queryForList(String sql, Class<T> elementType, Object... args) throws DataAccessException {
		return jdbc.queryForList(sql, elementType, args);
	}

	/**
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForList(java.lang.String,
	 *      java.lang.Object[], int[])
	 */
	public List<Map<String, Object>> queryForList(String sql, Object[] args, int[] argTypes) throws DataAccessException {
		return jdbc.queryForList(sql, args, argTypes);
	}

	/**
	 * @param sql
	 * @param args
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForList(java.lang.String,
	 *      java.lang.Object[])
	 */
	public List<Map<String, Object>> queryForList(String sql, Object... args) throws DataAccessException {
		return jdbc.queryForList(sql, args);
	}

	/**
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForRowSet(java.lang.String,
	 *      java.lang.Object[], int[])
	 */
	public SqlRowSet queryForRowSet(String sql, Object[] args, int[] argTypes) throws DataAccessException {
		return jdbc.queryForRowSet(sql, args, argTypes);
	}

	/**
	 * @param sql
	 * @param args
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForRowSet(java.lang.String,
	 *      java.lang.Object[])
	 */
	public SqlRowSet queryForRowSet(String sql, Object... args) throws DataAccessException {
		return jdbc.queryForRowSet(sql, args);
	}

	/**
	 * @param psc
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#update(org.springframework.jdbc.core.PreparedStatementCreator)
	 */
	public int update(PreparedStatementCreator psc) throws DataAccessException {
		return jdbc.update(psc);
	}

	/**
	 * @param psc
	 * @param generatedKeyHolder
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#update(org.springframework.jdbc.core.PreparedStatementCreator,
	 *      org.springframework.jdbc.support.KeyHolder)
	 */
	public int update(PreparedStatementCreator psc, KeyHolder generatedKeyHolder) throws DataAccessException {
		return jdbc.update(psc, generatedKeyHolder);
	}

	/**
	 * @param sql
	 * @param pss
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#update(java.lang.String,
	 *      org.springframework.jdbc.core.PreparedStatementSetter)
	 */
	public int update(String sql, PreparedStatementSetter pss) throws DataAccessException {
		return jdbc.update(sql, pss);
	}

	/**
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#update(java.lang.String,
	 *      java.lang.Object[], int[])
	 */
	public int update(String sql, Object[] args, int[] argTypes) throws DataAccessException {
		return jdbc.update(sql, args, argTypes);
	}

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
	public int update(String sql, Object... args) throws DataAccessException {
		return jdbc.update(sql, args);
	}

	/**
	 * @param sql
	 * @param pss
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#batchUpdate(java.lang.String,
	 *      org.springframework.jdbc.core.BatchPreparedStatementSetter)
	 */
	public int[] batchUpdate(String sql, BatchPreparedStatementSetter pss) throws DataAccessException {
		return jdbc.batchUpdate(sql, pss);
	}

	/**
	 * @param csc
	 * @param action
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#execute(org.springframework.jdbc.core.CallableStatementCreator,
	 *      org.springframework.jdbc.core.CallableStatementCallback)
	 */
	public <T> T execute(CallableStatementCreator csc, CallableStatementCallback<T> action) throws DataAccessException {
		return jdbc.execute(csc, action);
	}

	/**
	 * @param callString
	 * @param action
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#execute(java.lang.String,
	 *      org.springframework.jdbc.core.CallableStatementCallback)
	 */
	public <T> T execute(String callString, CallableStatementCallback<T> action) throws DataAccessException {
		return jdbc.execute(callString, action);
	}

	/**
	 * @param csc
	 * @param declaredParameters
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#call(org.springframework.jdbc.core.CallableStatementCreator,
	 *      java.util.List)
	 */
	public Map<String, Object> call(CallableStatementCreator csc, List<SqlParameter> declaredParameters)
	        throws DataAccessException {
		return jdbc.call(csc, declaredParameters);
	}

}
