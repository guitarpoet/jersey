package com.thinkingcloud.tools.js.runner.main.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import freemarker.cache.TemplateLoader;

@Service
public class ResourceLocatorTemplateLoader implements TemplateLoader {

	@Autowired
	private ResourceLoader loader;

	@Override
	public Object findTemplateSource(String name) throws IOException {
		return loader.getResource(name);
	}

	@Override
	public long getLastModified(Object templateSource) {
		try {
			return ((Resource) templateSource).lastModified();
		} catch (IOException e) {
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
