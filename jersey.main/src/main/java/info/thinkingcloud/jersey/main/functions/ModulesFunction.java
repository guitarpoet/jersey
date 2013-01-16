package info.thinkingcloud.jersey.main.functions;

import info.thinkingcloud.jersey.core.NewGlobal;
import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.meta.Module;
import info.thinkingcloud.jersey.core.utils.SimpleFunction;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;


@Function(doc = "Prints all the modules that have loaded.")
@Service("modules")
public class ModulesFunction extends SimpleFunction {
	private static final long serialVersionUID = 7113190643885516966L;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private NewGlobal global;

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		StringBuilder sb = new StringBuilder();
		for (Object key : global.getAllIds()) {
			Object value = global.get(key);
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