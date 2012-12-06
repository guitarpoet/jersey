package com.thinkingcloud.tools.js.runner.main.functions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.core.meta.Function;
import com.thinkingcloud.tools.js.runner.core.meta.Parameter;
import com.thinkingcloud.tools.js.runner.core.utils.SimpleFunction;

@Service("setTimeout")
@Function(doc = "Call the call back after the given milliseconds.", parameters = {
        @Parameter(name = "callback", type = "function", doc = "The callback for this timeout"),
        @Parameter(name = "timeout", type = "int", doc = "The time in milliseconds"),
        @Parameter(name = "args", multi = true, doc = "The args for the callback.") }, returns = "The handle for this timeout.")
public class SetTimeoutFunction extends SimpleFunction {

	private static final long serialVersionUID = 774431214487943404L;

	@Autowired
	private TaskScheduler scheduler;

	@Override
	public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, Object[] args) {
		final org.mozilla.javascript.Function f = (org.mozilla.javascript.Function) args[0];
		final List<Object> al = new ArrayList<Object>();
		for (int i = 2; i < args.length; i++) {
			al.add(args[i]);
		}
		if (f != null) {
			return scheduler.schedule(new Runnable() {
				@Override
				public void run() {
					f.call(cx, scope, thisObj, al.toArray());
				}
			}, new Date(new Date().getTime() + (int) Math.round((Double) args[1])));
		}
		return null;
	}
}
