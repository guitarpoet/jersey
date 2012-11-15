package com.thinkingcloud.tools.js.runner.main.functions;

import java.util.Arrays;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Service;

@Service("appContext")
public class AppContextFunction extends BaseFunction {

	private static final long serialVersionUID = 7576374988025300959L;

	private static Logger logger = LoggerFactory.getLogger(AppContextFunction.class);

	@Autowired
	private ApplicationContext context;

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		logger.info("The application context files are {}", Arrays.toString(args));
		return new FileSystemXmlApplicationContext((String[]) args, context);
	}
}
