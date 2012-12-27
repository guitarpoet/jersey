package com.thinkingcloud.tools.js.runner.main.functions;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.core.meta.Function;
import com.thinkingcloud.tools.js.runner.core.meta.Parameter;
import com.thinkingcloud.tools.js.runner.core.utils.SimpleFunction;

@Function(doc = "Get the thread local object", parameters = @Parameter(name = "obj", type = "object", doc = "The object to put into this thread's local"))
@Service("threadlocal")
public class ThreadLocalFunction extends SimpleFunction {

	private static final long serialVersionUID = 2008545180345763148L;

	private ThreadLocal<Object> local = new ThreadLocal<Object>();

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		if (args.length > 0)
			local.set(args[0]);
		else
			return local.get();
		return local;
	}
}
