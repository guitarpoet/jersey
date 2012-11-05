package com.thinkingcloud.tools.js.runner.application.service;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class StringUtilsServiceTest {

	private StringUtilsService service;

	private String sample = "You did not have to test your SQL by cutting and pasting it into a separate tool and replacing all the object and host variables yourself.  Just by visually selecting the string and running the command DBExecSQL";

	@Before
	public void setup() {
		service = new StringUtilsService();
	}

	@Test
	public void testZipAndUnzip() throws Exception {
		String answer = service.zip(sample);
		System.out.println(answer);
		System.out.println(service.unzip(answer));
		assertThat(sample, is(service.unzip(answer)));

	}
}
