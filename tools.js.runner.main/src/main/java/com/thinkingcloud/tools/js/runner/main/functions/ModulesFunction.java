package com.thinkingcloud.tools.js.runner.main.functions;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.shell.Global;
import org.mozilla.javascript.tools.shell.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.core.meta.Function;
import com.thinkingcloud.tools.js.runner.core.meta.Module;
import com.thinkingcloud.tools.js.runner.core.utils.SimpleFunction;

@Function(doc = "Prints all the modules that have loaded.")
@Service("modules")
public class ModulesFunction extends SimpleFunction {
	private static final long serialVersionUID = 7113190643885516966L;

	@Autowired
	private ApplicationContext context;

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		StringBuilder sb = new StringBuilder();
		Global g = Main.getGlobal();
		for (Object key : g.getAllIds()) {
			Object value = g.get(key);
			if (value == null)
				continue;
			Module m = value.getClass().getAnnotation(Module.class);
			if (m != null) {
				sb.append(key).append(":\t");
				sb.append(m.doc());
				sb.append("\n");
			} else {
			}
		}
		return sb;
	}
}
