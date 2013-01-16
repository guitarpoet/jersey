package info.thinkingcloud.jersey.jsdb;

import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.meta.Module;
import info.thinkingcloud.jersey.core.utils.BaseService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.hibernate.cfg.Mappings;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.id.factory.IdentifierGeneratorFactory;
import org.hibernate.id.factory.internal.DefaultIdentifierGeneratorFactory;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.KeyValue;
import org.hibernate.mapping.PrimaryKey;
import org.hibernate.mapping.SimpleValue;
import org.hibernate.mapping.Table;
import org.hibernate.type.TypeResolver;
import org.springframework.stereotype.Service;


@SuppressWarnings("unchecked")
@Service("jsdb")
@Module(doc = "The sql generator to generate the database from the json schema.")
public class JsonSchemaDatabaseService extends BaseService {

	private Dialect dialect;

	private Map<String, Table> tableCache = new HashMap<String, Table>();

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

	@PostConstruct
	public void init() {
		this.dialect = Dialect.getDialect(System.getProperties());
	}

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

	@Function(doc = "The drop table sqls")
	public String[] sqlDrop(Object... schemas) {
		ArrayList<String> ret = new ArrayList<String>(schemas.length);
		for (Object schema : schemas) {
			ret.add((processTable((Map<String, Object>) schema).sqlDropString(dialect, null, null)));
		}
		return ret.toArray(new String[ret.size()]);
	}

	@Function(doc = "The create table sqls")
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
