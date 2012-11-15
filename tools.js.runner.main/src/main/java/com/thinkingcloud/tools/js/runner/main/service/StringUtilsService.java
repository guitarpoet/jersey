package com.thinkingcloud.tools.js.runner.main.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.Perl5Pattern;
import org.dom4j.Document;
import org.dom4j.io.DOMReader;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.tools.shell.Global;
import org.mozilla.javascript.tools.shell.Main;
import org.springframework.stereotype.Service;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;

import au.com.bytecode.opencsv.CSVReader;

@Service
public class StringUtilsService {

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

	public Object eval(String js) {
		Context context = Context.getCurrentContext();
		Global g = Main.getGlobal();
		return context.evaluateString(g, js, "code", 1, null);
	}

	public Object json(String data) {
		return eval("(" + data + ")");
	}

	public String toString(Object[] arr) {
		return Arrays.toString(arr);
	}

	public String[][] csv(String file) throws IOException {
		FileReader reader = new FileReader(file);
		try {
			return new CSVReader(new FileReader(file)).readAll().toArray(new String[][] {});
		} finally {
			reader.close();
		}
	}

	public MatchResult match(String pattern, String str) throws MalformedPatternException {
		Perl5Compiler compiler = new Perl5Compiler();
		Perl5Pattern p = (Perl5Pattern) compiler.compile(pattern);
		Perl5Matcher matcher = new Perl5Matcher();
		if (matcher.matches(str, p)) {
			return matcher.getMatch();
		}
		return null;
	}

	public String numberfy(String value) {
		if (value.trim().equals(StringUtils.EMPTY))
			return "0";
		int first = -1;
		int last = value.length();
		for (int j = 0; j < value.length(); j++) {
			switch (value.charAt(j)) {
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case '.':
				if (first == -1)
					first = j;
				break;
			default:
				if (first != -1) {
					last = j;
					return value.substring(first, last);
				}
			}
		}
		if (first == -1)
			return "0";
		return value.substring(first, last);
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
}
