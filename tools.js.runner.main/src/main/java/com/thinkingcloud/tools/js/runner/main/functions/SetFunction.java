package com.thinkingcloud.tools.js.runner.main.functions;

import java.util.Collection;
import java.util.HashSet;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.main.utils.meta.Function;
import com.thinkingcloud.tools.js.runner.main.utils.meta.Parameter;

@Service("set")
@Function(parameters = { @Parameter(type = "list", optional = true, name = "origin", doc = "The original collection for create set.") }, doc = "Create a new set.", returns = "The new set.")
public class SetFunction extends SimpleFunction {
	private static final long serialVersionUID = 2557781333568017888L;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		if (args.length > 0)
			return new HashSet((Collection) args[0]);
		return new HashSet();
	}
}
