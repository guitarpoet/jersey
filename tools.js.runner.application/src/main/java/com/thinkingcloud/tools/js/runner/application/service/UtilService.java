package com.thinkingcloud.tools.js.runner.application.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import org.apache.commons.httpclient.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class UtilService {
	private static Logger logger = LoggerFactory.getLogger(UtilService.class);

	@Autowired
	private ResourceLoader resourceLoader;

	public Scriptable scope;

	@Autowired
	private HttpClient client;

	public void include(String resource) throws IOException {
		logger.info("");
		InputStream in = loadResource(resource);
		if (in != null) {
			Context.getCurrentContext().evaluateReader(scope, new InputStreamReader(in), resource, 1, null);
		}
	}

	public Object config(String key) {
		return System.getProperties().get(key);
	}

	public void require(String lib) throws IOException {
		include("classpath:scripts/" + lib + ".js");
	}

	public Date now() {
		return new Date();
	}

	public InputStream loadResource(String location) throws IOException {
		Resource resource = resourceLoader.getResource(location);
		return resource == null ? null : resource.getInputStream();
	}

	public BufferedStreamEnumeration enumerateResource(String location) throws IOException {
		return new BufferedStreamEnumeration(loadResource(location));
	}

	public Scriptable parseJSON(String json) {
		Context context = Context.getCurrentContext();
		return (Scriptable) context.evaluateString(scope, "(" + json + ")", "<json>", 1, null);
	}

	public JSONObject parseJSONData(String json) throws IOException {
		StringReader reader = new StringReader(json);
		JSONObject o = new JSONObject();
		StringBuilder key = new StringBuilder();
		StringBuilder value = new StringBuilder();
		char c = 0;
		int i = 0;
		int OBJECT = 0;
		int KEY = 1;
		int KEY_END = 2;
		int VALUE = 3;
		int VALUE_END = 4;
		int OBJECT_END = 5;
		int mode = -1;
		boolean escape = false;
		while ((i = reader.read()) != -1) {
			c = (char) i;
			switch (c) {
			case '\\':
				escape = true;
				break;
			case '{':
				if (mode == -1)
					mode = OBJECT;
				escape = false;
				break;
			case '}':
				mode = OBJECT_END;
				escape = false;
				break;
			case '"':
				if (!escape)
					mode++;
				escape = false;
				break;
			case ':':
				escape = false;
				if (mode == KEY_END) {
					continue;
				}
				break;
			}
			if (c == '"' && !escape) {
				escape = false;
				continue;
			}
			if (mode == KEY) {
				key.append(c);
			}
			if (mode == VALUE) {
				value.append(c);
			}
			if (mode == VALUE_END) {
				if (key.length() != 0)
					o.put(key.toString(), value.toString());
				key = new StringBuilder();
				value = new StringBuilder();
				mode = OBJECT;
			}
		}
		reader.close();
		return o;
	}

	public JSONObject parseJSONObject(String json) throws ParseException {
		JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
		return (JSONObject) parser.parse(json);
	}

	public String[] getKeys(JSONObject obj) {
		String[] arr = (String[]) obj.keySet().toArray(new String[0]);
		Arrays.sort(arr);
		return arr;
	}

	public void println(Object o) {
		System.out.println(o);
	}

	public void printf(String format, Object... args) {
		MessageFormat f = new MessageFormat(format);
		System.out.println(f.format(args));
	}

	public Scriptable toJS(Object o) {
		return (Scriptable) Context.javaToJS(o, scope);
	}

	public Object toJava(Object s) {
		return Context.jsToJava(s, Object.class);
	}

	public Set<Object> set() {
		return new HashSet<Object>();
	}

	/**
	 * Download any html page in utf-8 encoding.
	 * 
	 * @param url
	 * @param encoding
	 * @param headers
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	protected String download(String url, String encoding, Map<String, String> headers) throws HttpException,
	        IOException {
		logger.info("Ready to download {}", url);
		HttpGet get = new HttpGet(url);
		HttpResponse response = null;
		try {
			if (headers != null) {
				for (Map.Entry<String, String> e : headers.entrySet()) {
					get.addHeader(e.getKey(), e.getValue());
				}
			}
			response = client.execute(get);
			if (response.getEntity().getContentType() == null
			        || (!response.getEntity().getContentType().getValue().contains("text/html") && !response
			                .getEntity().getContentType().getValue().contains("json"))) {
				return null;
			}
			if (encoding == null) {
				return EntityUtils.toString(response.getEntity());
			} else {
				Charset charset = Charset.forName(encoding);
				long contentLength = response.getEntity().getContentLength();
				ByteBuffer buffer = null;
				if (contentLength == -1) {
					buffer = ByteBuffer.allocate(1024);
				} else {
					buffer = ByteBuffer.allocate((int) contentLength);
				}
				InputStream input = response.getEntity().getContent();
				byte[] bf = new byte[1024];
				int count = 0;
				int offset = 0;
				while ((count = input.read(bf)) != -1) {
					buffer.put(bf, offset, count);
					offset += count;
				}
				input.close();
				return charset.decode(buffer).toString();
			}
		} finally {
			EntityUtils.consume(response.getEntity());
		}
	}

	public String download(String url, Map<String, String> headers) throws HttpException, IOException {
		return download(url, null, headers);
	}

	public void sleep(int second) throws InterruptedException {
		Thread.sleep(1000 * second);
	}

	public Map<String, String> defaultHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("User-Agent",
		        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_4) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2");
		headers.put("Accept-Language", "en-us,en;q=0.5");
		return headers;
	}

	public String download(String url, String encoding) throws HttpException, IOException {
		return download(url, encoding, defaultHeaders());
	}

	public String download(String url) throws HttpException, IOException {
		return download(url, null, null);
	}

}
