package info.thinkingcloud.jersey.main.functions;

import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.meta.Parameter;
import info.thinkingcloud.jersey.core.utils.SimpleFunction;

import java.text.MessageFormat;
import java.util.Arrays;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;


@Service("setInterval")
@Function(doc = "The same setInterval function as javascript using timer.", parameters = {
        @Parameter(name = "callback", doc = "The callback function for setInterval.", type = "function"),
        @Parameter(name = "interval", type = "int", doc = "The call interval.") }, returns = "The handle for this job.")
public class SetIntervalFunction extends SimpleFunction {
	private static final long serialVersionUID = 7206468067490143973L;

	@Autowired
	private TaskScheduler scheduler;

	@Override
	public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
		if (args.length != 2 || !(args[0] instanceof org.mozilla.javascript.Function)) {
			throw new IllegalArgumentException(MessageFormat.format(
			        "The arguments {0} is not right for this function.", Arrays.toString(args)));
		}
		return scheduler.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				((org.mozilla.javascript.Function) args[0]).call(cx, scope, thisObj, args);
			}
		}, (int) Math.round((Double) args[1]));
	}
}
