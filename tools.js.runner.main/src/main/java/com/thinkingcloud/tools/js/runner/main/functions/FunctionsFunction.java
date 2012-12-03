package com.thinkingcloud.tools.js.runner.main.functions;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.core.NewGlobal;
import com.thinkingcloud.tools.js.runner.core.meta.Function;
import com.thinkingcloud.tools.js.runner.core.utils.SimpleFunction;

@Service("functions")
@Function(doc = "List all the functions this shell provided.", returns = "All the functions this console can provide.")
public class FunctionsFunction extends SimpleFunction {

	private static final long serialVersionUID = 8131372544973432309L;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private NewGlobal global;

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		StringBuilder sb = new StringBuilder();
		for (BaseFunction function : context.getBeansOfType(BaseFunction.class).values()) {
			Function f = function.getClass().getAnnotation(Function.class);
			if (f != null)
				sb.append(function.getFunctionName()).append(":\t").append(f.doc()).append("\n");
		}
		for (Object key : global.getIds()) {
			Object value = global.get(key);
			if (value instanceof BaseFunction) {
				BaseFunction function = (BaseFunction) value;
				Function f = function.getClass().getAnnotation(Function.class);
				if (!(f != null || function.has("meta", function)))
					continue;
				sb.append(key).append(":\t");
				if (f != null) {
					sb.append(f.doc());
				} else {
					if (function.has("meta", function)) {
						Scriptable meta = (Scriptable) function.get("meta");
						sb.append(meta.get("doc", meta).toString());
					}
				}
				sb.append("\n");
			}
		}
		return sb;
	}
}
