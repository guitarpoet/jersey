package info.thinkingcloud.jersey.htmlunit;

import info.thinkingcloud.jersey.htmlunit.HtmlUnitService;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class JustTestHttpUnit {
	private HtmlUnitService service;

	@Before
	public void init() {
		service = new HtmlUnitService();
	}

	@Test
	public void testPage() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		HtmlPage page = service.get("http://freeproxylists.net/?page=2");
		System.out.println(page.asText());
	}
}