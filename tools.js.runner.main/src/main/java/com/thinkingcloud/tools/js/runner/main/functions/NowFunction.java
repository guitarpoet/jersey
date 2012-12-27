package com.thinkingcloud.tools.js.runner.main.functions;

import java.util.Date;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.core.NewGlobal;
import com.thinkingcloud.tools.js.runner.core.meta.Function;
import com.thinkingcloud.tools.js.runner.core.utils.SimpleFunction;

@Service("now")
@Function(parameters = {}, doc = "Get the datetime of now.", returns = "Date of now.")
public class NowFunction extends SimpleFunction {
	private static final long serialVersionUID = -7375690204111425447L;

	@Autowired
	private NewGlobal global;

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		return Context.javaToJS(new Date(), global);
	}
}