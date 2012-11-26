package com.thinkingcloud.tools.js.runner.main.functions;

import java.util.ArrayList;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.main.utils.meta.Function;
import com.thinkingcloud.tools.js.runner.main.utils.meta.Parameter;

@Service("list")
@Function(parameters = { @Parameter(multi = true, name = "obj", type = "object", optional = true, doc = "The objects to insert into the arraylist.") }, doc = "Create an array list using the args.", returns = "An arraylist.")
public class ListFunction extends SimpleFunction {
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
