package com.thinkingcloud.tools.js.runner.main.functions;

import java.util.HashMap;
import java.util.Map;

import net.minidev.json.JSONObject;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.core.meta.Function;
import com.thinkingcloud.tools.js.runner.core.meta.Parameter;
import com.thinkingcloud.tools.js.runner.core.utils.SimpleFunction;

@Service("config")
@Function(parameters = { @Parameter(name = "config", doc = "The name of the configuration", type = "string", optional = true) }, doc = "Read the configuration from system.properties or list all the properties", returns = "The configuration.")
public class ConfigFunction extends SimpleFunction {

	private static final long serialVersionUID = 3979797223325868923L;

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		if (args.length > 1) {
			System.getProperties().setProperty((String) args[0], (String) args[1]);
		} else if (args.length == 1) {
			return System.getProperties().get(args[0]);
		} else {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (Map.Entry<Object, Object> e : System.getProperties().entrySet()) {
				map.put(String.valueOf(e.getKey()), e.getValue());
			}
			return JSONObject.toJSONString(map);
		}
		return null;
	}
}
