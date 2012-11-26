package com.thinkingcloud.tools.js.runner.main.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import freemarker.template.TemplateException;

public class BaseService {
	@Autowired
	private DocUtils doc;

	public String doc() throws IOException, TemplateException {
		return doc.serviceDoc(doc.generateServiceData(getClass()));
	}
}
