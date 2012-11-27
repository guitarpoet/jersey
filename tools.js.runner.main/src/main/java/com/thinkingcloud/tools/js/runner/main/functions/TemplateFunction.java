package com.thinkingcloud.tools.js.runner.main.functions;

import java.io.StringWriter;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.core.meta.Function;
import com.thinkingcloud.tools.js.runner.core.meta.Parameter;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service("template")
@Function(parameters = {
        @Parameter(name = "path", type = "string", doc = "The location for loading the template, using the resource pattern."),
        @Parameter(name = "parameters", type = "object", doc = "The parameters for tempate.") }, doc = "The temlate engine function using freemarker.", returns = "The processed template string.")
public class TemplateFunction extends SimpleFunction {

	private static final long serialVersionUID = -4713307857290517532L;

	@Autowired
	private Configuration freemarker;

	private static Logger logger = LoggerFactory.getLogger(TemplateFunction.class);

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		try {
			String name = (String) args[0];
			Template template = freemarker.getTemplate(name);
			if (template != null) {
				StringWriter writer = new StringWriter();
				template.process(args[1], writer);
				writer.flush();
				return writer.toString();
			}
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
