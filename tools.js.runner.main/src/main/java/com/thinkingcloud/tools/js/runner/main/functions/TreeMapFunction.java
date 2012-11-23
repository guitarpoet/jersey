package com.thinkingcloud.tools.js.runner.main.functions;

import java.util.Map;
import java.util.TreeMap;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.stereotype.Service;

@Service("smap")
public class TreeMapFunction extends BaseFunction {

	private static final long serialVersionUID = -5175831436130163425L;

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		if (args.length > 0 && args[0] instanceof Map) {
			return new TreeMap<String, Object>((Map) args[0]);
		}
		return new TreeMap<String, Object>();
	}
}
