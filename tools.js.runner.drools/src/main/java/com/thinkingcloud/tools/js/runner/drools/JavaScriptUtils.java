package com.thinkingcloud.tools.js.runner.drools;

import java.util.Map;

import org.mozilla.javascript.Scriptable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.core.NewGlobal;

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
