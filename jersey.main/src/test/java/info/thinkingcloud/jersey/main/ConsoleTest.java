package info.thinkingcloud.jersey.main;

import java.io.IOException;

import freemarker.template.TemplateException;
import info.thinkingcloud.jersey.main.service.Console;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application_context.xml")
public class ConsoleTest {

	private static Logger logger = LoggerFactory.getLogger(ConsoleTest.class);
	@Autowired
	private Console console;

	@Test
	public void testForInspect() throws IOException, TemplateException {
		logger.info(console.inspect(this, 0));
	}
}
