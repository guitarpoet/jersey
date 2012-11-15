package com.thinkingcloud.tools.coffee.runner.functions;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ExitFunction extends SimpleFunction {

	private static final long serialVersionUID = 3033796323919573312L;

	@Autowired
	private ConfigurableApplicationContext context;

	public ExitFunction() {
		super("exit");
	}

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		context.close();
		return null;
	}
}
