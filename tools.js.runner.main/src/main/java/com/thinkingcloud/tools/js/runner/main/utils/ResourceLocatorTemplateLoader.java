package com.thinkingcloud.tools.js.runner.main.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import freemarker.cache.TemplateLoader;

@Service
public class ResourceLocatorTemplateLoader implements TemplateLoader {

	@Autowired
	private ResourceLoader loader;

	private static Logger logger = LoggerFactory.getLogger(ResourceLocatorTemplateLoader.class);

	@Override
	public Object findTemplateSource(String name) throws IOException {
		return loader.getResource(name);
	}

	@Override
	public long getLastModified(Object templateSource) {
		try {
			return ((Resource) templateSource).lastModified();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return 0;
	}

	@Override
	public Reader getReader(Object templateSource, String encoding) throws IOException {
		return new InputStreamReader(((Resource) templateSource).getInputStream());
	}

	@Override
	public void closeTemplateSource(Object templateSource) throws IOException {
		// Nothing.
	}
}
