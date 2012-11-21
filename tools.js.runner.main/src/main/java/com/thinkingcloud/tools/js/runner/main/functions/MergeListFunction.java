package com.thinkingcloud.tools.js.runner.main.functions;

import java.util.ArrayList;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.stereotype.Service;

@Service("flat")
@SuppressWarnings("rawtypes")
public class MergeListFunction extends BaseFunction {

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
