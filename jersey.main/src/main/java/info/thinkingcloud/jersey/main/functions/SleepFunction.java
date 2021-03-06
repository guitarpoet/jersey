package info.thinkingcloud.jersey.main.functions;

import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.meta.Parameter;
import info.thinkingcloud.jersey.core.utils.SimpleFunction;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.stereotype.Service;


@Service("sleep")
@Function(doc = "Sleep the main thread for given milliseconds.", parameters = { @Parameter(name = "miliseconds", type = "long", doc = "The miliseconds to let the thread to sleep.") })
public class SleepFunction extends SimpleFunction {

	private static final long serialVersionUID = -8924111525672860861L;

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		try {
			Thread.sleep(Math.round(Double.parseDouble(args[0].toString())));
		} catch (InterruptedException e) {
		}
		return null;
	}
}
