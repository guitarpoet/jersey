package com.thinkingcloud.tools.js.runner.main.functions;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.stereotype.Service;

@Service("config")
public class ConfigFunction extends BaseFunction {

	private static final long serialVersionUID = 3979797223325868923L;

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		if (args.length > 1) {
			System.getProperties().setProperty((String) args[0], (String) args[1]);
		} else {
			return System.getProperties().get(args[0]);
		}
		return null;
	}
}
