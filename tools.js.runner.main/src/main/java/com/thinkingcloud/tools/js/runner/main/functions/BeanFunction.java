package com.thinkingcloud.tools.js.runner.main.functions;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service("bean")
public class BeanFunction extends BaseFunction {
	private static final long serialVersionUID = -2786545415630825489L;

	private static Logger logger = LoggerFactory.getLogger(BeanFunction.class);

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		String name = (String) args[0];
		ApplicationContext context = null;
		if (args.length == 1) {
			try {
				context = (ApplicationContext) Context.jsToJava(scope, ApplicationContext.class);
			} catch (Throwable t) {
				logger.error(t.getMessage(), t);
			}
		} else {
			try {
				context = (ApplicationContext) Context.jsToJava(args[1], ApplicationContext.class);
			} catch (Throwable t) {
				logger.error(t.getMessage(), t);
			}
		}
		if (context == null)
			return null;
		return context.getBean(name);
	}
}
