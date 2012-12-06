package com.thinkingcloud.tools.js.runner.main.functions;

import java.util.concurrent.ScheduledFuture;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.core.meta.Function;
import com.thinkingcloud.tools.js.runner.core.meta.Parameter;
import com.thinkingcloud.tools.js.runner.core.utils.SimpleFunction;

@Service("clearTimeout")
@Function(doc = "Clear the timout settings.", parameters = @Parameter(name = "handle", doc = "The handle for the setTimeout."))
public class ClearTimeoutFunction extends SimpleFunction {

	private static final long serialVersionUID = -1943407214512766430L;

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		ScheduledFuture<?> future = (ScheduledFuture<?>) args[0];
		if (future != null)
			future.cancel(true);
		return null;
	}
}
