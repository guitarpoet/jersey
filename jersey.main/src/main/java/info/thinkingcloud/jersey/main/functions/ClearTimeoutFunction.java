package info.thinkingcloud.jersey.main.functions;

import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.meta.Parameter;
import info.thinkingcloud.jersey.core.utils.SimpleFunction;

import java.util.concurrent.ScheduledFuture;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.stereotype.Service;


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
