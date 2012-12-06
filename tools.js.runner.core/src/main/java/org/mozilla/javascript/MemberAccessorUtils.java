package org.mozilla.javascript;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MemberAccessorUtils {
	public static Method[] convert(NativeJavaMethod method) {
		List<Method> methods = new ArrayList<Method>();
		for (MemberBox box : method.methods) {
			methods.add(box.method());
		}
		return methods.toArray(new Method[methods.size()]);
	}
}
