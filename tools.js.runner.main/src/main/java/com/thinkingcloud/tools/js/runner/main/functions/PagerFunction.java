package com.thinkingcloud.tools.js.runner.main.functions;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.core.meta.Function;
import com.thinkingcloud.tools.js.runner.core.meta.Parameter;
import com.thinkingcloud.tools.js.runner.core.utils.SimpleFunction;
import com.thinkingcloud.tools.js.runner.main.utils.PagerObject;

@Service("pager")
@Function(doc = "Read the long text in a pager", parameters = @Parameter(name = "text", type = "string", doc = "The long text to read."))
public class PagerFunction extends SimpleFunction {

	private static final long serialVersionUID = -1258652099182902834L;

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		String text = null;
		if (args[0] instanceof Scriptable) {
			text = (String) Context.jsToJava(args[0], String.class);
		} else {
			text = String.valueOf(args[0]);
		}
		return new PagerObject(text);
	}
}
