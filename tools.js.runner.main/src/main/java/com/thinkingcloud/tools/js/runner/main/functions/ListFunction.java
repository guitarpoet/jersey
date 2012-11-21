package com.thinkingcloud.tools.js.runner.main.functions;

import java.util.ArrayList;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.stereotype.Service;

@Service("list")
public class ListFunction extends BaseFunction {
	private static final long serialVersionUID = -5433234360321485483L;

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		ArrayList<Object> list = new ArrayList<Object>();
		for (Object object : args) {
			list.add(object);
		}
		return list;
	}
}
