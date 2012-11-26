package com.thinkingcloud.tools.js.runner.main.functions;

import java.util.Date;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.main.utils.meta.Function;

@Service("now")
@Function(parameters = {}, doc = "Get the datetime of now.", returns = "Date of now.")
public class NowFunction extends SimpleFunction {
	private static final long serialVersionUID = -7375690204111425447L;

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		return new Date();
	}
}