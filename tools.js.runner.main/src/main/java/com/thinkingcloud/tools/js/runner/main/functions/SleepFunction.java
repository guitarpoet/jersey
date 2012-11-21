package com.thinkingcloud.tools.js.runner.main.functions;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.stereotype.Service;

@Service("sleep")
public class SleepFunction extends BaseFunction {

	private static final long serialVersionUID = -8924111525672860861L;

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		try {
			Thread.sleep(Math.round(Double.parseDouble(args[0].toString())));
		} catch (InterruptedException e) {
		}
		return null;
	}
}
