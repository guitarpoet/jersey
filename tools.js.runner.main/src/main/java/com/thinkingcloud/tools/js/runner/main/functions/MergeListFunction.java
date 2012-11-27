package com.thinkingcloud.tools.js.runner.main.functions;

import java.util.ArrayList;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.core.meta.Function;
import com.thinkingcloud.tools.js.runner.core.meta.Parameter;

@Service("flat")
@SuppressWarnings("rawtypes")
@Function(parameters = { @Parameter(name = "obj", type = "list or object", doc = "The list or object need to flat to 1 list.", multi = true) }, doc = "Flat all the list and args into 1 list.", returns = "Merged list")
public class MergeListFunction extends SimpleFunction {

	private static final long serialVersionUID = 818359013057155034L;

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		ArrayList<Object> list = new ArrayList<Object>();
		for (Object object : args) {
			if (object instanceof Iterable) {
				Iterable i = (Iterable) object;
				for (Object o : i) {
					list.add(o);
				}
			} else
				list.add(object);
		}
		return list;
	}
}
