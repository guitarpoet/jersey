package com.thinkingcloud.tools.js.runner.main.functions;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.core.meta.Function;
import com.thinkingcloud.tools.js.runner.core.meta.Parameter;
import com.thinkingcloud.tools.js.runner.core.utils.SimpleFunction;

@Service("require")
@Function(parameters = { @Parameter(name = "location", doc = "The location to require the resource.", type = "string", multi = true) }, doc = "Require the library using the resouce location, support file: and classpath: protocol.", returns = "The plugin has required.")
public class RequireFunction extends SimpleFunction {

	private static final long serialVersionUID = -7908751368918579064L;

	private static Logger logger = LoggerFactory.getLogger(RequireFunction.class);

	@Autowired
	private ApplicationContext context;

	public static Map<String, Object> loaded = new HashMap<String, Object>();

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
						if (loaded.containsKey(loc))
							return loaded.get(loc);
						Object obj = Context.getCurrentContext().evaluateReader(scope, new InputStreamReader(in), loc,
						        1, null);
						loaded.put(loc, obj);
						return obj;
					}
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return null;
	}
}
