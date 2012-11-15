package com.thinkingcloud.tools.js.runner.main.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.main.utils.CSVBuffer;
import com.thinkingcloud.tools.js.runner.main.utils.CSVIterator;

@Service("csv")
public class CSVUtils {
	private Logger logger = LoggerFactory.getLogger(CSVUtils.class);

	public CSVIterator iterate(String path) throws FileNotFoundException {
		File file = new File(path);
		if (!file.exists()) {
			logger.warn("File {} is not existed!", path);
			return null;
		}
		return new CSVIterator(new FileReader(file));
	}

	public CSVBuffer open(String path) throws IOException {
		return new CSVBuffer(path);
	}
}
