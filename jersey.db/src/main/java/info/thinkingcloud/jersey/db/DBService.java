package info.thinkingcloud.jersey.db;

import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.meta.Module;
import info.thinkingcloud.jersey.core.meta.Parameter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.hibernate.cfg.Mappings;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.RowSelection;
import org.hibernate.id.factory.IdentifierGeneratorFactory;
import org.hibernate.id.factory.internal.DefaultIdentifierGeneratorFactory;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.KeyValue;
import org.hibernate.mapping.PrimaryKey;
import org.hibernate.mapping.SimpleValue;
import org.hibernate.mapping.Table;
import org.hibernate.type.TypeResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service("db")
@Module(doc = "The service for database access.")
@SuppressWarnings("unchecked")
public class DBService {

	private static final Logger logger = LoggerFactory.getLogger(DBService.class);

	@Autowired
	private JdbcTemplate jdbc;

	private Dialect dialect;

	private Map<String, Table> tableCache = new HashMap<String, Table>();

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

	@Function(doc = "Create the sql for paging using the query sql", parameters = {
	        @Parameter(name = "sql", doc = "The query sql", type = "string"),
	        @Parameter(name = "fetchSize", type = "int", doc = "The fetch size for pagination."),
	        @Parameter(name = "firstRow", type = "int", doc = "The offset for the first row"),
	        @Parameter(name = "Total", type = "int", doc = "The total size for the pagination.") }, returns = "The pagination sql.")
	public String pagerSql(String sql, int fetchSize, int firstRow, int total) {
		RowSelection rs = new RowSelection();
		rs.setFetchSize(fetchSize);
		rs.setFirstRow(firstRow);
		rs.setMaxRows(total);
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

	private Mappings mappings = (Mappings) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
	        new Class<?>[] { Mappings.class }, new InvocationHandler() {

		        private IdentifierGeneratorFactory idfactory = new DefaultIdentifierGeneratorFactory();

		        private TypeResolver typeResolver = new TypeResolver();

		        @Override
		        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			        if (method.getName().equals("getTypeResolver"))
				        return typeResolver;
			        if (method.getName().equals("getIdentifierGeneratorFactory"))
				        return idfactory;
			        return null;
		        }
	        });

	private Mapping mapping = (Mapping) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
	        new Class<?>[] { Mapping.class }, new InvocationHandler() {
		        @Override
		        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			        return mappings.getIdentifierGeneratorFactory();
		        }
	        });

	@SuppressWarnings("deprecation")
	public String limit(String query, int offset, int limit) {
		return this.dialect.getLimitString(query, offset, limit);
	}

	public Column handleColumn(Table table, String name, Map<String, Object> properties) {
		Column ret = new Column(name);
		String type = (String) properties.get("type");
		SimpleValue value = new SimpleValue(mappings, table);
		value.setIdentifierGeneratorStrategy("identity");
		value.setTypeName(type);
		if (properties.containsKey("length"))
			ret.setLength((int) (Math.ceil((Double) properties.get("length"))));
		if (properties.containsKey("default")) {
			value.setNullValue((String) properties.get("default"));
			ret.setDefaultValue((String) properties.get("default"));
		}
		if (properties.containsKey("nullable")) {
			ret.setNullable((Boolean) properties.get("nullable"));
		}
		if (properties.containsKey("unique")) {
			ret.setUnique((Boolean) properties.get("unique"));
		}
		ret.setValue(value);
		return ret;
	}

	public Table processTable(Map<String, Object> schema) {
		String name = (String) schema.get("name");
		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("Table name {" + name + "}is not valid.");
		}
		if (tableCache.containsKey(name))
			return tableCache.get(name);
		Table ret = new Table(name);
		if (schema.containsKey("properties")) {
			Map<String, Object> properties = (Map<String, Object>) schema.get("properties");
			for (Map.Entry<String, Object> e : properties.entrySet()) {
				Map<String, Object> props = (Map<String, Object>) e.getValue();
				Column column = handleColumn(ret, e.getKey(), props);
				ret.addColumn(column);
				if (props.containsKey("identifier")) {
					PrimaryKey key = new PrimaryKey();
					key.setTable(ret);
					key.addColumn(column);
					ret.setPrimaryKey(key);
					ret.setIdentifierValue((KeyValue) column.getValue());
				}
			}
		}
		if (schema.containsKey("references")) {
			for (Map<String, Object> props : (Iterable<Map<String, Object>>) schema.get("references")) {
				if (!props.containsKey("name")) {
					throw new IllegalArgumentException("The name for the table {" + name + "} foreign key is not set!");
				}
				if (!props.containsKey("key")) {
					throw new IllegalArgumentException("The column for the table {" + name
					        + "} foreign key is not set!");
				}
				if (!props.containsKey("table")) {
					throw new IllegalArgumentException("The table for the table {" + name + "} foreign key is not set!");
				}
				if (!props.containsKey("refer")) {
					throw new IllegalArgumentException("The refer for the table {" + name + "} foreign key is not set!");
				}

				Table referedTable = tableCache.get(props.get("table"));
				if (referedTable == null)
					throw new IllegalArgumentException(MessageFormat.format(
					        "The table {0} didn't have any definition.", props.get("table")));
				ret.createForeignKey((String) props.get("name"),
				        Arrays.asList(getColumn(ret, (String) props.get("key"))), (String) props.get("table"),
				        Arrays.asList(getColumn(referedTable, (String) props.get("refer")))).setReferencedTable(
				        referedTable);
			}
		}
		tableCache.put(name, ret);
		return ret;
	}

	public Column getColumn(Table table, String name) {
		for (Iterator<Column> i = table.getColumnIterator(); i.hasNext();) {
			Column c = i.next();
			if (c.getName().equals(name))
				return c;
		}
		return null;
	}

	@Function(doc = "The drop table sqls", parameters = @Parameter(name = "schema", doc = "The schema to generate the drop table sql", type = "string", multi = true))
	public String[] sqlDrop(Object... schemas) {
		ArrayList<String> ret = new ArrayList<String>(schemas.length);
		for (Object schema : schemas) {
			ret.add((processTable((Map<String, Object>) schema).sqlDropString(dialect, null, null)));
		}
		return ret.toArray(new String[ret.size()]);
	}

	@Function(doc = "The create table sqls", parameters = @Parameter(name = "schema", doc = "The schema to generate the create table sql", type = "object", multi = true))
	public String[] sqlCreate(Object... schemas) {
		ArrayList<String> ret = new ArrayList<String>(schemas.length);
		for (Object schema : schemas) {
			Table table = processTable((Map<String, Object>) schema);
			ret.add(table.sqlCreateString(dialect, mapping, null, null));
			for (Iterator<ForeignKey> i = table.getForeignKeyIterator(); i.hasNext();) {
				ret.add(i.next().sqlCreateString(dialect, mapping, null, null));
			}
		}
		return ret.toArray(new String[ret.size()]);
	}
}
