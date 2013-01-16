package info.thinkingcloud.jersey.main.functions;

import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.meta.Parameter;
import info.thinkingcloud.jersey.core.utils.SimpleFunction;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.stereotype.Service;


@Function(doc = "Get the thread local object", parameters = @Parameter(name = "obj", type = "object", doc = "The object to put into this thread's local"))
@Service("threadlocal")
public class ThreadLocalFunction extends SimpleFunction {

	private static final long serialVersionUID = 2008545180345763148L;

	private ThreadLocal<Object> local = new ThreadLocal<Object>();

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		if (args.length > 0)
			local.set(args[0]);
		else
			return local.get();
		return local;
	}
}
