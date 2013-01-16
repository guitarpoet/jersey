package info.thinkingcloud.jersey.core.utils;

import info.thinkingcloud.jersey.core.meta.Function;

import java.io.IOException;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.FunctionObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import freemarker.template.TemplateException;

public abstract class SimpleFunction extends BaseFunction {
	private static final long serialVersionUID = -6435914576461747142L;

	@Autowired
	private DocUtils doc;

	public SimpleFunction() {
		FunctionObject fo;
		try {
			fo = new FunctionObject("doc", getClass().getMethod("doc", new Class<?>[0]), this);
			put("doc", this, fo);
		} catch (Throwable e) {
		}
	}

	@Override
	public Object getDefaultValue(Class<?> typeHint) {
		return toString();
	}

	@Override
	public int getArity() {
		Function f = this.getClass().getAnnotation(Function.class);
		if (f != null)
			return f.parameters().length;
		return 0;
	}

	@Override
	public String getFunctionName() {
		Service s = this.getClass().getAnnotation(Service.class);
		if (s != null) {
			return s.value();
		}
		return super.getFunctionName();
	}

	public String doc() throws IOException, TemplateException {
		return doc.functionDoc(getClass(), getFunctionName());
	}

	@Override
	public String toString() {
		return Context.getCurrentContext().decompileFunction(this, 0);
	}
}
