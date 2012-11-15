package com.thinkingcloud.tools.js.runner.main.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.main.utils.StreamLineIterator;

@Service("resource")
public class ResourcesUtils {

	@Autowired
	private ResourceLoader resourceLoader;

	public StreamLineIterator iterate(String path) throws IOException {
		Resource resource = resourceLoader.getResource(path);
		if (path == null)
			return null;
		return new StreamLineIterator(resource.getInputStream());
	}

	public String read(String path) throws FileNotFoundException, IOException {
		Resource resource = resourceLoader.getResource(path);
		if (path == null)
			return null;
		StringWriter writer = new StringWriter();
		IOUtils.copy(new InputStreamReader(resource.getInputStream()), writer);
		writer.flush();
		writer.close();
		return writer.toString();
	}
}
