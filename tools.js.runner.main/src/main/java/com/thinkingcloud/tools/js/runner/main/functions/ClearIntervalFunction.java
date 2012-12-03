package com.thinkingcloud.tools.js.runner.main.functions;

import java.util.concurrent.ScheduledFuture;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.core.meta.Function;
import com.thinkingcloud.tools.js.runner.core.meta.Parameter;
import com.thinkingcloud.tools.js.runner.core.utils.SimpleFunction;

@Service("clearInterval")
@Function(doc = "Clear the inverval timer using the handle", parameters = @Parameter(doc = "The interval's handle", type = "IntervalHandle", name = "handle"))
public class ClearIntervalFunction extends SimpleFunction {

	private static final long serialVersionUID = -2937093587290937584L;

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		ScheduledFuture<?> handle = (ScheduledFuture<?>) args[0];
		handle.cancel(true);
		return null;
	}
}
