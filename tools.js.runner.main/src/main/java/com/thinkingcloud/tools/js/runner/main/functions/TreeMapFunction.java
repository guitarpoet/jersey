package com.thinkingcloud.tools.js.runner.main.functions;

import java.util.Map;
import java.util.TreeMap;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.core.meta.Function;
import com.thinkingcloud.tools.js.runner.core.meta.Parameter;
import com.thinkingcloud.tools.js.runner.core.utils.SimpleFunction;

@Service("smap")
@Function(parameters = { @Parameter(optional = true, name = "origin", type = "map", doc = "The map to make this sorted map.") }, doc = "Create a new tree sorted map.", returns = "A tree map.")
public class TreeMapFunction extends SimpleFunction {

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
