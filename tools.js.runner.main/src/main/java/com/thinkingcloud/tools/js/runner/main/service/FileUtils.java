package com.thinkingcloud.tools.js.runner.main.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.main.utils.FileBuffer;
import com.thinkingcloud.tools.js.runner.main.utils.StreamLineIterator;

@Service("file")
public class FileUtils {
	private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

	public StreamLineIterator iterate(String path) throws FileNotFoundException {
		File file = getFile(path);
		if (file == null)
			return null;
		return new StreamLineIterator(new FileInputStream(file));
	}

	private File getFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			logger.warn("File {} is not existed!", path);
			return null;
		}
		return file;
	}

	public FileBuffer open(String path) {
		return new FileBuffer(path);
	}

	public String read(String path) throws FileNotFoundException, IOException {
		File file = getFile(path);
		if (file == null)
			return null;
		StringWriter writer = new StringWriter();
		IOUtils.copy(new FileReader(file), writer);
		writer.flush();
		writer.close();
		return writer.toString();
	}
}
