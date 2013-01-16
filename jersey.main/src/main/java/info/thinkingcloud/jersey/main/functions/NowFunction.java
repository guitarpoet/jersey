package info.thinkingcloud.jersey.main.functions;

import info.thinkingcloud.jersey.core.NewGlobal;
import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.utils.SimpleFunction;

import java.util.Date;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("now")
@Function(parameters = {}, doc = "Get the datetime of now.", returns = "Date of now.")
public class NowFunction extends SimpleFunction {
	private static final long serialVersionUID = -7375690204111425447L;

	@Autowired
	private NewGlobal global;

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		return Context.javaToJS(new Date(), global);
	}
}