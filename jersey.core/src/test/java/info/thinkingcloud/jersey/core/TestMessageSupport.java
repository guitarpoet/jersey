package info.thinkingcloud.jersey.core;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:app.xml")
public class TestMessageSupport {

	@Autowired
	private DummyService dummy;

	@BeforeClass
	public static void init() throws IOException {
		System.getProperties().load(
		        Thread.currentThread().getContextClassLoader().getResourceAsStream("app.properties"));
	}

	@Test
	public void testGetMessage() {
		assertThat(dummy.text("greeting", "Jack"), equalTo("Hello, Jack"));
		assertThat(dummy.text("test {0}", "Jack"), equalTo("test Jack"));
	}
}
