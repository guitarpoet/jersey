package com.thinkingcloud.tools.js.runner.main.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import jline.internal.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVReader;

import com.thinkingcloud.tools.js.runner.core.meta.Function;
import com.thinkingcloud.tools.js.runner.core.meta.Module;
import com.thinkingcloud.tools.js.runner.core.meta.Parameter;
import com.thinkingcloud.tools.js.runner.core.utils.BaseService;
import com.thinkingcloud.tools.js.runner.main.utils.CSVBuffer;
import com.thinkingcloud.tools.js.runner.main.utils.CSVIterator;

@Service("csv")
@Module(doc = "The csv utils service.")
public class CSVUtils extends BaseService {
	private Logger logger = LoggerFactory.getLogger(CSVUtils.class);

	@Function(doc = "Read the csv into array", parameters = @Parameter(name = "path", doc = "The path of the csv file", type = "String"), returns = "The array of the readed data.")
	public String[][] read(String path) throws IOException {
		CSVReader reader = new CSVReader(new FileReader(path));
		String[][] out = reader.readAll().toArray(new String[][] {});
		reader.close();
		return out;
	}

	public CSVIterator iterate(String path, String encoding) throws UnsupportedEncodingException, FileNotFoundException {
		File file = new File(path);
		if (!file.exists()) {
			logger.warn("File {} is not existed!", path);
			return null;
		}
		if (encoding != null)
			return new CSVIterator(new InputStreamReader(new FileInputStream(path), encoding));
		return new CSVIterator(new FileReader(path));
	}

	@Function(doc = "Iterate the csv file using iterator mode.", parameters = {
	        @Parameter(name = "path", type = "string", doc = "The path of the csv file"),
	        @Parameter(name = "encoding", doc = "The encoding of the file", optional = true, type = "string") }, returns = "The csv iterator.")
	public CSVIterator iterate(String path) throws FileNotFoundException, UnsupportedEncodingException {
		return iterate(path, null);
	}

	@Function(doc = "Open a csv buffer to write", parameters = @Parameter(name = "path", type = "string", doc = "The path to write the csv file."), returns = "The csv buffer")
	public CSVBuffer open(String path) throws IOException {
		return new CSVBuffer(path);
	}
}
