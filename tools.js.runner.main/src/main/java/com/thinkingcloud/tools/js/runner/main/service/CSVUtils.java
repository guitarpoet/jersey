package com.thinkingcloud.tools.js.runner.main.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.core.meta.Function;
import com.thinkingcloud.tools.js.runner.core.meta.Module;
import com.thinkingcloud.tools.js.runner.core.meta.Parameter;
import com.thinkingcloud.tools.js.runner.main.utils.CSVBuffer;
import com.thinkingcloud.tools.js.runner.main.utils.CSVIterator;

@Service("csv")
@Module(doc = "The csv utils service.")
public class CSVUtils {
	private Logger logger = LoggerFactory.getLogger(CSVUtils.class);

	@Function(doc = "Iterate the csv file using iterator mode.", parameters = @Parameter(name = "path", type = "string", doc = "The path of the csv file"), returns = "The csv iterator.")
	public CSVIterator iterate(String path) throws FileNotFoundException {
		File file = new File(path);
		if (!file.exists()) {
			logger.warn("File {} is not existed!", path);
			return null;
		}
		return new CSVIterator(new FileReader(file));
	}

	@Function(doc = "Open a csv buffer to write", parameters = @Parameter(name = "path", type = "string", doc = "The path to write the csv file."), returns = "The csv buffer")
	public CSVBuffer open(String path) throws IOException {
		return new CSVBuffer(path);
	}
}
