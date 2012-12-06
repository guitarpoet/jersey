package com.thinkingcloud.tools.js.runner.main.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.Perl5Pattern;
import org.dom4j.Document;
import org.dom4j.io.DOMReader;
import org.mozilla.javascript.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;

import au.com.bytecode.opencsv.CSVReader;

import com.thinkingcloud.tools.js.runner.core.NewGlobal;
import com.thinkingcloud.tools.js.runner.core.meta.Function;
import com.thinkingcloud.tools.js.runner.core.meta.Module;
import com.thinkingcloud.tools.js.runner.core.meta.Parameter;
import com.thinkingcloud.tools.js.runner.core.utils.BaseService;
import com.thinkingcloud.tools.js.runner.main.utils.Slf4jLoggingPrintWriter;

@Service("sutils")
@Module(doc = "The string utils service.")
public class StringService extends BaseService {

	@Autowired
	private NewGlobal global;

	@Function(parameters = @Parameter(name = "url", doc = "The url to verify.", type = "string"), doc = "Verify the url, test if it is a valid url.")
	public boolean isValidUrl(String url) {
		try {
			new URL(url);
		} catch (MalformedURLException e) {
			return false;
		}
		return true;
	}

	@Function(parameters = { @Parameter(name = "container", type = "string", doc = "The container string to find."),
	        @Parameter(name = "str", type = "string", doc = "The string to find.") }, doc = "Test if the string contains other string.")
	public boolean contains(String container, String str) {
		return container.contains(str);
	}

	@Function(parameters = { @Parameter(name = "obj", doc = "The object to test", type = "string") }, doc = "Test if the object is a number.")
	public boolean isNumber(Object o) {
		return StringUtils.isNumeric(String.valueOf(o));
	}

	@Function(parameters = @Parameter(name = "str", type = "string", doc = "The string to test"), doc = "Test if the string is blank.")
	public boolean isBlank(String str) {
		return StringUtils.isBlank(str);
	}

	@Function(parameters = { @Parameter(name = "str", type = "string", doc = "The string to split."),
	        @Parameter(name = "sep", type = "string", doc = "The seperator.") }, doc = "Split the string using the seperator.")
	public String[] split(String str, String sep) {
		return str.split(sep);
	}

	@Function(parameters = @Parameter(name = "str", type = "string", doc = "The string to test."), doc = "Test if the string is an empty string.")
	public boolean isEmpty(String str) {
		return StringUtils.isEmpty(str);
	}

	@Function(parameters = @Parameter(name = "str", type = "string", doc = "The string to capitalize"), doc = "Capitalize the string.")
	public String capitalize(String str) {
		return StringUtils.capitalize(str);
	}

	@Function(parameters = @Parameter(name = "xml", type = "string", doc = "The xml string to tidy."), doc = "Tidy the xml using jtidy.", returns = "The tidyed xml")
	public String tidyXml(String xml) throws IOException {
		Tidy tidy = new Tidy();
		tidy.setCharEncoding(Configuration.UTF8);
		tidy.setIndentContent(true);
		tidy.setBreakBeforeBR(true);
		tidy.setXmlTags(true);
		tidy.setErrout(new Slf4jLoggingPrintWriter());
		tidy.setWraplen(1024);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		tidy.parse(new ByteArrayInputStream(xml.getBytes("utf-8")), output);
		return new String(output.toByteArray(), "utf-8");
	}

	@Function(parameters = @Parameter(name = "html", type = "string", doc = "The html string to tidy."), doc = "Tidy the html using jtidy.", returns = "The tidyed html.")
	public String tidyHtml(String html) throws IOException {
		Tidy tidy = new Tidy();
		tidy.setCharEncoding(Configuration.UTF8);
		tidy.setIndentContent(true);
		tidy.setBreakBeforeBR(true);
		tidy.setErrout(new Slf4jLoggingPrintWriter());
		tidy.setWraplen(1024);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		tidy.parse(new ByteArrayInputStream(html.getBytes("utf-8")), output);
		return new String(output.toByteArray(), "utf-8");
	}

	@Function(parameters = @Parameter(name = "data", type = "string", doc = "The string to zip."), doc = "Zip the string using the zlib and encode the zip into base 64 encoded string.")
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

	@Function(parameters = @Parameter(name = "zip", type = "string", doc = "The string to unzip"), doc = "Unzip the string.")
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

	@Function(parameters = @Parameter(name = "script", type = "string", doc = "The script to evaluate."), doc = "Evaluate the script.")
	public Object eval(String js) {
		Context context = Context.getCurrentContext();
		return context.evaluateString(global, js, "code", 1, null);
	}

	@Function(parameters = @Parameter(name = "data", type = "string", doc = "The json data to parse."), doc = "Parse the string to json data.")
	public Object json(String data) {
		return eval("(" + data + ")");
	}

	@Function(parameters = @Parameter(name = "list", type = "list", doc = "The array to convert to string"), doc = "Convert the array to string.")
	public String toString(Object[] arr) {
		return Arrays.toString(arr);
	}

	@Function(parameters = @Parameter(name = "file", type = "string", doc = "The csv file to open."), doc = "Read the csv file into datas.")
	public String[][] csv(String file) throws IOException {
		FileReader reader = new FileReader(file);
		try {
			return new CSVReader(new FileReader(file)).readAll().toArray(new String[][] {});
		} finally {
			reader.close();
		}
	}

	@Function(doc = "Match the string using the pattern", returns = "The match result", parameters = {
	        @Parameter(name = "pattern", doc = "The regex pattern", type = "string"),
	        @Parameter(name = "str", type = "string", doc = "The string to be matched.") })
	public MatchResult match(String pattern, String str) throws MalformedPatternException {
		Perl5Compiler compiler = new Perl5Compiler();
		Perl5Pattern p = (Perl5Pattern) compiler.compile(pattern);
		Perl5Matcher matcher = new Perl5Matcher();
		if (matcher.matches(str, p)) {
			return matcher.getMatch();
		}
		return null;
	}

	public String readToEnd(Reader reader) throws IOException {
		StringWriter writer = new StringWriter();
		IOUtils.copy(reader, writer);
		writer.flush();
		reader.close();
		return writer.toString();
	}

	@Function(doc = "Scan the pattern in the value", parameters = {
	        @Parameter(name = "pattern", doc = "The pattern string", type = "string"),
	        @Parameter(name = "value", doc = "The string to scan", type = "string") })
	public String[] scan(String pattern, String value) {
		Scanner scanner = new Scanner(value);
		List<String> list = new ArrayList<String>();
		String str = null;
		while ((str = scanner.findInLine(pattern)) != null) {
			list.add(str);
		}
		return list.toArray(new String[0]);
	}

	@Function(doc = "Numberfy the string.", parameters = @Parameter(name = "value", type = "string", doc = "The string to numberfy."))
	public String numberfy(String value) {
		value.replace(",", "");
		String[] arr = scan("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?", value);
		if (arr.length == 0)
			return "0";
		return arr[0];
	}

	@Function(doc = "Parse the html to xml document", parameters = @Parameter(name = "xml", type = "string", doc = "The html string to parse"), returns = "The parsed dom4j document.")
	public Document parse(String html) throws IOException {
		Tidy tidy = new Tidy();
		tidy.setCharEncoding(Configuration.UTF8);
		tidy.setIndentContent(true);
		tidy.setBreakBeforeBR(true);
		tidy.setWraplen(1024);
		tidy.setErrout(new Slf4jLoggingPrintWriter());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DOMReader reader = new DOMReader();
		return reader.read(tidy.parseDOM(new ByteArrayInputStream(html.getBytes("utf-8")), out));
	}
}
