package com.thinkingcloud.tools.js.runner.application.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.lobobrowser.html.domimpl.HTMLDocumentImpl;
import org.lobobrowser.html.parser.DocumentBuilderImpl;
import org.lobobrowser.html.test.SimpleUserAgentContext;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;
import org.xml.sax.SAXException;

@Service
public class UtilService {
	private static Logger logger = LoggerFactory.getLogger(UtilService.class);

	@Autowired
	private ResourceLoader resourceLoader;

	public Scriptable scope;

	@Autowired
	private HttpClient client;

	public void include(String resource) throws IOException {
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

	protected GetMethod initGet(String url) {
		String u = null;
		if (url.startsWith("http")) {
			u = url;
		} else {
			u = url.startsWith("/") ? url : "/" + url;
		}
		GetMethod get = new GetMethod(u);
		get.setRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		get.setRequestHeader("User-Agent",
		        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_4) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2");
		return get;
	}

	public Date now() {
		return new Date();
	}

	public String getHtml(String url) throws HttpException, IOException {
		return getHtml(url, null);
	}

	public String getTidyHtml(String url) throws UnsupportedEncodingException, HttpException, IOException {
		return getTidyHtml(url, null);
	}

	public String getTidyHtml(String url, String encoding) throws UnsupportedEncodingException, HttpException,
	        IOException {
		return tidyXml(getHtml(url, encoding));
	}

	public String getHtml(String url, String encoding) throws HttpException, IOException {
		logger.info("Going to fetch url {}...", url);
		GetMethod get = initGet(url);
		client.executeMethod(get);
		if (get.getStatusCode() != 200)
			return null;
		return encoding == null ? get.getResponseBodyAsString() : Charset.forName(encoding)
		        .decode(Charset.forName(get.getResponseCharSet()).encode(get.getResponseBodyAsString())).toString();
	}

	public HTMLDocumentImpl getHtmlDocument(String url) throws HttpException, IOException, SAXException {
		SimpleUserAgentContext context = new SimpleUserAgentContext();
		DocumentBuilderImpl db = new DocumentBuilderImpl(context);
		GetMethod get = initGet(url);
		client.executeMethod(get);
		InputStream in = get.getResponseBodyAsStream();
		HTMLDocumentImpl doc = (HTMLDocumentImpl) db.parse(in);
		in.close();
		return doc;
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

	public String tidyXml(String xml) throws UnsupportedEncodingException {
		Tidy tidy = new Tidy();
		tidy.setCharEncoding(Configuration.UTF8);
		tidy.setIndentContent(true);
		tidy.setBreakBeforeBR(true);
		tidy.setXmlTags(true);
		tidy.setWraplen(1024);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		tidy.parse(new ByteArrayInputStream(xml.getBytes("utf-8")), output);
		return new String(output.toByteArray(), "utf-8");
	}
}
