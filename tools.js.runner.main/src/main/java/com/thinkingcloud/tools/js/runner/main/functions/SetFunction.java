package com.thinkingcloud.tools.js.runner.main.functions;

import java.util.HashSet;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.stereotype.Service;

@Service("set")
public class SetFunction extends BaseFunction {
	private static final long serialVersionUID = 2557781333568017888L;

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		return new HashSet<Object>();
	}
}
