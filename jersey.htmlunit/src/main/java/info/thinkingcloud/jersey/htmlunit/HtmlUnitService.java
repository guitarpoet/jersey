package info.thinkingcloud.jersey.htmlunit;

import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.meta.Module;
import info.thinkingcloud.jersey.core.meta.Parameter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@Service("htmlunit")
@Module(doc = "The module for provide the http unit service.")
public class HtmlUnitService {
	private WebClient client = new WebClient();

	@Function(doc = "Get the data using http get", parameters = {
	        @Parameter(name = "url", type = "string", doc = "The url for http get"),
	        @Parameter(name = "headers", type = "object", doc = "The http headers.", optional = true) })
	public HtmlPage get(String url) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		return client.getPage(url);
	}

	public HtmlPage get(String url, Map<String, String> headers) throws FailingHttpStatusCodeException, IOException {
		WebRequest request = new WebRequest(new URL(url), HttpMethod.GET);
		if (headers != null)
			request.setAdditionalHeaders(new HashMap<String, String>(headers));
		return client.getPage(request);
	}

	@Function(doc = "Get the data using http post", parameters = {
	        @Parameter(name = "url", type = "string", doc = "The url to post"),
	        @Parameter(name = "data", type = "object", doc = "The post data"),
	        @Parameter(name = "headers", type = "object", doc = "The http headers for post", optional = true) })
	public HtmlPage post(String url, Map<String, Object> data, Map<String, String> headers)
	        throws FailingHttpStatusCodeException, IOException {
		WebRequest request = new WebRequest(new URL(url), HttpMethod.POST);
		if (headers != null)
			request.setAdditionalHeaders(new HashMap<String, String>(headers));
		return client.getPage(request);
	}

	public HtmlPage post(String url, Map<String, Object> data) throws FailingHttpStatusCodeException, IOException {
		return post(url, data, null);
	}
}
