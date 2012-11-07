package com.thinkingcloud.tools.js.runner.application.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.dom4j.Document;
import org.dom4j.io.DOMReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;

@Service
public class StringUtilsService {

	@Autowired
	private UtilService utils;

	@Autowired
	private HttpClient client;

	public boolean isValidUrl(String url) {
		try {
			new URL(url);
		} catch (MalformedURLException e) {
			return false;
		}
		return true;
	}

	public boolean contains(String container, String str) {
		return container.contains(str);
	}

	public boolean isNumber(Object o) {
		return StringUtils.isNumeric(String.valueOf(o));
	}

	public boolean isBlank(String str) {
		return StringUtils.isBlank(str);
	}

	public String[] split(String str, String sep) {
		return str.split(sep);
	}

	public boolean isEmpty(String str) {
		return StringUtils.isEmpty(str);
	}

	public String capitalize(String str) {
		return StringUtils.capitalize(str);
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

	public String zip(String data) throws IOException {
		ByteArrayOutputStream array = new ByteArrayOutputStream();
		DeflaterOutputStream out = (new DeflaterOutputStream(array));
		byte[] buffer = new byte[1024];
		int count;
		ByteArrayInputStream reader = new ByteArrayInputStream(data.getBytes("utf-8"));
		while ((count = reader.read(buffer)) != -1) {
			out.write(buffer, 0, count);
		}
		out.flush();
		out.close();
		return Base64.encodeBase64String(array.toByteArray());
	}

	public String unzip(String zip) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InflaterInputStream reader = new InflaterInputStream(new ByteArrayInputStream(Base64.decodeBase64(zip)));
		byte[] buffer = new byte[1024];
		int count;
		while ((count = reader.read(buffer)) != -1) {
			out.write(buffer, 0, count);
		}
		out.flush();
		out.close();
		return new String(out.toByteArray(), "utf-8");
	}

	public String fixUrl(String url, String base) {
		if (url.startsWith("http") || url.startsWith("ftp") || url.startsWith("javascript"))
			return url;
		StringBuilder sb = new StringBuilder();
		sb.append(base);
		if (!url.startsWith("/"))
			sb.append("/");
		sb.append(url);
		return sb.toString();
	}

	public Document parse(String html) throws UnsupportedEncodingException {
		Tidy tidy = new Tidy();
		tidy.setCharEncoding(Configuration.UTF8);
		tidy.setIndentContent(true);
		tidy.setBreakBeforeBR(true);
		tidy.setWraplen(1024);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DOMReader reader = new DOMReader();
		return reader.read(tidy.parseDOM(new ByteArrayInputStream(html.getBytes("utf-8")), out));
	}

	public Document getDocument(String url, Map<String, String> headers) throws IOException {
		return parse(utils.download(url, headers));
	}
}
