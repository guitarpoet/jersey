package com.thinkingcloud.tools.js.runner.drools;

import info.thinkingcloud.jersey.core.NewGlobal;

import java.util.Map;

import org.mozilla.javascript.Scriptable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("jsutils")
public class JavaScriptUtils {
	@Autowired
	private NewGlobal global;

	public void put(Map<Object, Object> obj, String name, Object value) {
		if (obj instanceof Scriptable) {
			((Scriptable) obj).put(name, (Scriptable) obj, value);
		} else {
			obj.put(name, value);
		}
	}
}
