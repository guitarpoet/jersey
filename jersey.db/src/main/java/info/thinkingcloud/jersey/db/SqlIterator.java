package info.thinkingcloud.jersey.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * The iterator to iterate the table.
 * 
 * @author jack
 * 
 */
public class SqlIterator implements Iterator<Map<String, Object>> {

	private static final Logger logger = LoggerFactory.getLogger(SqlIterator.class);

	private String sql;

	private int fetchSize;

	private int offset = 0;

	private int current = 0;

	private long total;

	private DBService db;

	private Object[] args;

	private List<Map<String, Object>> results;

	public SqlIterator(String sql, Object[] args, int fetchSize, DBService db) {
		this.sql = sql;
		this.fetchSize = fetchSize;
		this.db = db;
		this.args = args;

		String totalSql = "select count(*) " + sql.substring(sql.indexOf("from"));
		total = db.queryForLong(totalSql);
	}

	@Override
	public boolean hasNext() {
		return current < total;
	}

	public String[] scan(String pattern, String value) {
		Scanner scanner = new Scanner(value);
		List<String> list = new ArrayList<String>();
		String str = null;
		while ((str = scanner.findInLine(pattern)) != null) {
			list.add(str);
		}
		return list.toArray(new String[0]);
	}

	@Override
	public Map<String, Object> next() {
		if (results == null || current == offset + fetchSize) {
			logger.debug("Current {} is bigger than the fetched ones {}, we need to fetch again.", current, offset);
			ArrayList<Object> a = new ArrayList<Object>();
			for (Object o : args) {
				a.add(o);
			}

			if (results != null) {
				offset += fetchSize;
			}
			String pagerSql = db.pagerSql(sql, fetchSize, offset, (int) total);

			int paramCount = scan("\\?", pagerSql).length;
			switch (paramCount - a.size()) {
			case 0:
				break;
			case 1:
				a.add(fetchSize);
				break;
			default:
				a.add(fetchSize);
				a.add(offset);
				break;
			}

			results = db.queryForListInner(pagerSql, a.toArray());
		}
		Map<String, Object> ret = results.get(current - offset);
		current++;
		return ret;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Not supported!!!");
	}
}
