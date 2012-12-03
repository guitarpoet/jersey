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

import com.thinkingcloud.tools.js.runner.core.meta.Function;
import com.thinkingcloud.tools.js.runner.core.meta.Module;
import com.thinkingcloud.tools.js.runner.core.meta.Parameter;
import com.thinkingcloud.tools.js.runner.core.utils.BaseService;
import com.thinkingcloud.tools.js.runner.main.utils.FileBuffer;
import com.thinkingcloud.tools.js.runner.main.utils.StreamLineIterator;

@Service("file")
@Module(doc = "The file utils.")
public class FileUtils extends BaseService {
	private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

	@Function(parameters = { @Parameter(name = "path", type = "string", doc = "The resource's path.") }, doc = "Iterate the stream using the path.", returns = "The stream iterator.")
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

	@Function(parameters = @Parameter(name = "path", type = "string", doc = "The resources's path"), doc = "Open the file to write.", returns = "The opened file buffer.")
	public FileBuffer open(String path) {
		return new FileBuffer(path);
	}

	@Function(parameters = @Parameter(name = "path", type = "string", doc = "The resources's path"), doc = "Read the file content into a string.", returns = "The file's content.")
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
