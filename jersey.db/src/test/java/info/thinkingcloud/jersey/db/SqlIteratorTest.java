package info.thinkingcloud.jersey.db;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class SqlIteratorTest {

	private static final String SQL = "select * from dummy";

	private DBService db;

	private SqlIterator iter;

	@Before
	public void setUp() throws Exception {
		db = mock(DBService.class);
	}

	@Test
	public void testIterate() {
		when(db.queryForLong("select count(*) from dummy")).thenReturn(1000l);
		iter = new SqlIterator(SQL, new Object[0], 100, db);
		assertThat(iter.hasNext(), is(true));

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 10; i++) {
			Map<String, Object> s = new HashMap<String, Object>();
			s.put("value", i);
			result.add(s);
		}
		when(db.pagerSql(SQL, 100, 0, 1000)).thenReturn("select * from dummy limit 100, 0");
		when(db.queryForListInner("select * from dummy limit 100, 0", new Object[] {})).thenReturn(result);

		Map<String, Object> get = iter.next();
		assertThat(0, is(get.get("value")));
	}
}
