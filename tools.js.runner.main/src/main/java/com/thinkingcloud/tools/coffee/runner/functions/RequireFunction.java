package com.thinkingcloud.tools.coffee.runner.functions;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class RequireFunction extends SimpleFunction {
	public RequireFunction() {
		super("require");
	}

	private static final long serialVersionUID = -7908751368918579064L;

	private static Logger logger = LoggerFactory.getLogger(RequireFunction.class);

	@Autowired
	private ApplicationContext context;

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		if (args.length == 0) {
			logger.error("There is nothing to require.");
			return null;
		}
		for (Object location : args) {
			String loc = location.toString();
			if (!(loc.startsWith("file:") || loc.startsWith("classpath:") || loc.startsWith("http:"))) {
				loc = "classpath:" + loc + ".js";
			}
			Resource resource = context.getResource(loc);
			InputStream in;
			try {
				if (resource != null) {
					in = resource.getInputStream();
					if (in != null) {
						return Context.getCurrentContext().evaluateReader(scope, new InputStreamReader(in), loc, 1,
						        null);
					}
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return null;
	}
}
