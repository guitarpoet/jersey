package info.thinkingcloud.jersey.main;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import info.thinkingcloud.jersey.main.service.StringService;

import org.junit.Before;
import org.junit.Test;


public class SutilsTest {

	private StringService sutils;

	@Before
	public void init() {
		sutils = new StringService();
	}

	@Test
	public void testScan() {
		assertThat(sutils.scan("[0-9]+", "asdf12314.1234124ad123sfasdgasg"), is(equalTo(new String[] { "12314",
		        "1234124", "123" })));

	}

	@Test
	public void testNumberfy() {
		assertThat(sutils.numberfy("asdf12314.1234124ad123sfasdgasg"), is(equalTo("12314.1234124")));
	}
}
